package yetzio.yetcalc.views.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import yetzio.yetcalc.R
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.dialogs.showUnitConvSearchDialog
import yetzio.yetcalc.enums.UnitType
import yetzio.yetcalc.models.UnitConvViewModel
import yetzio.yetcalc.utils.copyToClipboard
import yetzio.yetcalc.utils.isNetworkAvailable
import yetzio.yetcalc.views.UnitConvActivity
import yetzio.yetcalc.widget.CalcText
import java.net.URL
import java.util.Calendar
import java.util.Currency
import java.util.Locale
import java.util.TimeZone

class CurrencyFragment : Fragment() {

    var API = ""

    // Text Container - First
    private lateinit var firstConvField: TextInputLayout
    private lateinit var firstConvTV: CalcText

    // Dropdown Container - First
    private lateinit var firstConvDropField: TextInputLayout
    private lateinit var firstConvDropDownField: AutoCompleteTextView

    // Text Container - Second
    private lateinit var secondConvField: TextInputLayout
    private lateinit var secondConvTV: CalcText

    // Dropdown Container - Second
    private lateinit var secondConvDropField: TextInputLayout
    private lateinit var secondConvDropDownField: AutoCompleteTextView

    private lateinit var switchUnitsButton: MaterialButton

    private lateinit var pViewModel: UnitConvViewModel

    private lateinit var p_preferences: SharedPreferences
    private lateinit var p_editor: SharedPreferences.Editor

    private lateinit var selectedDateFormat: String
    var unitConvDialog: AlertDialog? = null

    private var tryAgainBT: MaterialButton? = null

    private lateinit var firstTextWatcher: TextWatcher
    private lateinit var secondTextWatcher: TextWatcher
    private var dateConvWatcher: TextWatcher? = null

    private val mCoroutineScope = CoroutineScope(Dispatchers.Main)

    private var dateText: MaterialTextView? = null
    private var datePkr: FloatingActionButton? = null

    private var convRate = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        pViewModel = (activity as? UnitConvActivity)?.mViewModel!!
        p_preferences = (activity as? UnitConvActivity)?.preferences!!
        p_editor = (activity as? UnitConvActivity)?.editor!!

        selectedDateFormat = p_preferences.getString(SharedPrefs.DATEFMTKEY, "YYYY-MM-DD").toString()

        val view: View?
        if(isNetworkAvailable(context?.applicationContext)){
            view = createConv(inflater, container)
        }
        else{
            view = inflater.inflate(R.layout.no_internet, container, false)

            tryAgainBT = view.findViewById(R.id.tryAgainIntBt)
            tryAgainBT?.setOnClickListener {
                refreshFragment()
            }
        }

        return view
    }

    private fun refreshFragment(){
        (activity as? UnitConvActivity)?.refreshCurrentFragment()
    }

    private fun formatDate(dayormonth: String): String {
        return if (dayormonth.length > 1) dayormonth else "0$dayormonth"
    }

    private fun restoreDateConf(){
        if(pViewModel.current_date.value != ""){
            println("Date changed: ${pViewModel.current_date}")
            dateText?.text = pViewModel.current_date.value
        }
    }

    private fun setupDatePicker(){
        datePkr?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.DatePickerTheme)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = selection

                val mYear = calendar.get(Calendar.YEAR)
                val mMonth = calendar.get(Calendar.MONTH) + 1
                val mDay = calendar.get(Calendar.DAY_OF_MONTH)

                // Format and set the selected date
                pViewModel.current_date.value = "$mYear-${formatDate(mMonth.toString())}-${formatDate(mDay.toString())}"
                println("Date pick: ${pViewModel.current_date}")

                dateText?.text = when (selectedDateFormat) {
                    "DD-MM-YYYY" -> "${formatDate(mDay.toString())}-${formatDate(mMonth.toString())}-$mYear"
                    "DD-YYYY-MM" -> "${formatDate(mDay.toString())}-$mYear-${formatDate(mMonth.toString())}"
                    "YYYY-DD-MM" -> "$mYear-${formatDate(mDay.toString())}-${formatDate(mMonth.toString())}"
                    "MM-DD-YYYY" -> "${formatDate(mMonth.toString())}-${formatDate(mDay.toString())}-$mYear"
                    "MM-YYYY-DD" -> "${formatDate(mMonth.toString())}-$mYear-${formatDate(mDay.toString())}"
                    else -> "$mYear-${formatDate(mMonth.toString())}-${formatDate(mDay.toString())}"
                }
            }
        }
    }

    private fun createConv(inflater: LayoutInflater, container: ViewGroup?): View?{
        val view = inflater.inflate(R.layout.fragment_currency, container, false)

        val currentLocale: Locale = Locale.getDefault()
        val currency : Currency = Currency.getInstance(currentLocale)
        val currencyCode = currency.currencyCode

        val saveRecentUnitPref = p_preferences.getBoolean(SharedPrefs.SAVE_RECENT_UNITKEY, true)
        val initFirst = p_preferences.getString(SharedPrefs.CURRENCY_FIRST, currencyCode).toString()
        val initSecond = p_preferences.getString(SharedPrefs.CURRENCY_SECOND, "USD").toString()

        if(saveRecentUnitPref){
            pViewModel._firstcur.value = initFirst
            pViewModel._secondcur.value = initSecond
        }
        else{
            pViewModel._firstcur.value = currencyCode
            pViewModel._secondcur.value = "USD"
        }

        firstConvField = view.findViewById(R.id.firstConvFieldContainer)
        firstConvTV = view.findViewById(R.id.firstConvField)

        firstConvDropField = view.findViewById(R.id.firstConvDropDown)
        firstConvDropDownField = view.findViewById(R.id.firstConvDropDownField)

        secondConvField = view.findViewById(R.id.secondConvFieldContainer)
        secondConvTV = view.findViewById(R.id.secondConvField)

        secondConvDropField = view.findViewById(R.id.secondConvDropDown)
        secondConvDropDownField = view.findViewById(R.id.secondConvDropDownField)

        dateText = view.findViewById(R.id.datecurrencyconv)
        datePkr = view.findViewById(R.id.calenderFloatButton)

        switchUnitsButton = view.findViewById(R.id.switchUnitsButton)

        firstConvDropDownField.setText(initFirst)
        secondConvDropDownField.setText(initSecond)

        restoreDateConf()

        setupClipboardEvent()
        setupSelections()
        setupDatePicker()
        textChanged()

        return view
    }

    private fun fetchApiResults(id: Int){
        val lowerConv = pViewModel._secondcur.value?.lowercase()
        val baseCur = pViewModel._firstcur.value?.lowercase()
        // thanks to https://github.com/fawazahmed0/exchange-api
        API = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/${baseCur}.json"

        if(dateText != null){
            if(dateText!!.text.isNotEmpty() && dateText!!.text.isNotBlank()){
                API = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@${pViewModel.current_date.value}/v1/currencies/${baseCur}.json"
            }
        }

        mCoroutineScope.launch {
            try {
                val resDeferred = mCoroutineScope.async(Dispatchers.IO){
                    val apiresult = URL(API).readText()
                    val jsonObj = baseCur?.let { JSONObject(apiresult).getJSONObject(it) }

                    if (lowerConv != null) {
                        jsonObj?.getDouble(lowerConv)?.toFloat()
                    }
                    else{
                        0f
                    }
                }

                convRate = resDeferred.await() ?: 0f

                when(id){
                    R.id.firstConvField -> {
                        val res = ((firstConvTV.text.toString().toFloat()) * convRate).toString()

                        if(res != secondConvTV.text.toString()){
                            secondConvTV.removeTextChangedListener(secondTextWatcher)
                            dateText?.removeTextChangedListener(dateConvWatcher)
                            secondConvTV.setText(res)
                            secondConvTV.addTextChangedListener(secondTextWatcher)
                            dateText?.addTextChangedListener(dateConvWatcher)
                        }
                    }
                    R.id.secondConvField -> {
                        val res = ((secondConvTV.text.toString().toFloat()) / convRate).toString()
                        if(res != firstConvTV.text.toString()){
                            firstConvTV.removeTextChangedListener(firstTextWatcher)
                            dateText?.removeTextChangedListener(dateConvWatcher)
                            firstConvTV.setText(res)
                            firstConvTV.addTextChangedListener(firstTextWatcher)
                            dateText?.addTextChangedListener(dateConvWatcher)
                        }
                    }
                }
            }
            catch (e: Exception){
                println("yetCalc: Currency conversion failed")
            }
        }
    }

    private fun getApiResults(id: Int){
        when(id){
            R.id.firstConvField -> {
                if(firstConvTV.text!!.isNotEmpty() && firstConvTV.text!!.isNotBlank()){
                    if(pViewModel._firstcur.value != pViewModel._secondcur.value){
                        fetchApiResults(id)
                    }
                }
            }
            R.id.secondConvField -> {
                if(secondConvTV.text!!.isNotEmpty() && secondConvTV.text!!.isNotBlank()){
                    if(pViewModel._firstcur.value != pViewModel._secondcur.value){
                        fetchApiResults(id)
                    }
                }
            }
        }
    }

    private fun setupClipboardEvent(){
        firstConvField.setStartIconOnClickListener {
            parentFragment?.activity?.applicationContext?.copyToClipboard(firstConvTV.text.toString())
        }

        secondConvField.setStartIconOnClickListener {
            parentFragment?.activity?.applicationContext?.copyToClipboard(secondConvTV.text.toString())
        }
    }

    private fun setupSelections(){
        firstConvDropField.setEndIconOnClickListener {
            pViewModel.currentUnit.value = UnitType.CURRENCY
            pViewModel.first.value = true
            unitConvDialog = showUnitConvSearchDialog(requireContext(), requireActivity().applicationContext, resources.getStringArray(R.array.currencies_one).toCollection(ArrayList()), pViewModel, viewLifecycleOwner)
        }

        firstConvDropDownField.setOnClickListener {
            pViewModel.currentUnit.value = UnitType.CURRENCY
            pViewModel.first.value = true
            unitConvDialog = showUnitConvSearchDialog(requireContext(), requireActivity().applicationContext, resources.getStringArray(R.array.currencies_one).toCollection(ArrayList()), pViewModel, viewLifecycleOwner)
        }

        secondConvDropField.setEndIconOnClickListener {
            pViewModel.currentUnit.value = UnitType.CURRENCY
            pViewModel.first.value = false
            unitConvDialog = showUnitConvSearchDialog(requireContext(), requireActivity().applicationContext, resources.getStringArray(R.array.currencies_one).toCollection(ArrayList()), pViewModel, viewLifecycleOwner)
        }

        secondConvDropDownField.setOnClickListener {
            pViewModel.currentUnit.value = UnitType.CURRENCY
            pViewModel.first.value = false
            unitConvDialog = showUnitConvSearchDialog(requireContext(), requireActivity().applicationContext, resources.getStringArray(R.array.currencies_one).toCollection(ArrayList()), pViewModel, viewLifecycleOwner)
        }

        switchUnitsButton.setOnClickListener {
            val temp = pViewModel._firstcur.value
            pViewModel._firstcur.value = pViewModel._secondcur.value
            pViewModel._secondcur.value = temp
        }

        pViewModel._firstcur.observe(viewLifecycleOwner) { newValue ->
            p_editor.putString(SharedPrefs.CURRENCY_FIRST, newValue)
            p_editor.apply()

            firstConvDropDownField.setText(newValue)
            unitConvDialog?.dismiss()

            if(isNetworkAvailable(context?.applicationContext)){
                firstConvTV.id.let { getApiResults(it) }
                secondConvTV.id.let { getApiResults(it) }
            }
            else{
                Toast.makeText(context?.applicationContext, "No Internet!", Toast.LENGTH_SHORT).show()
            }
        }

        pViewModel._secondcur.observe(viewLifecycleOwner) { newValue ->
            p_editor.putString(SharedPrefs.CURRENCY_SECOND, newValue)
            p_editor.apply()

            secondConvDropDownField.setText(newValue)
            unitConvDialog?.dismiss()

            if(isNetworkAvailable(context?.applicationContext)){
                firstConvTV.id.let { getApiResults(it) }
            }
            else{
                Toast.makeText(context?.applicationContext, "No Internet!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun textChanged(){
        firstTextWatcher = object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Main", "beforeTextChanged")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!isNetworkAvailable(context?.applicationContext)){
                    refreshFragment()
                }
                Log.d("Main", "onTextChanged")
            }

            override fun afterTextChanged(p0: Editable?) {
                try{
                    firstConvTV.id.let { getApiResults(it) }
                }catch (e: Exception){
                    Log.e("Main:", "$e")
                }
            }

        }


        secondTextWatcher = object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Main", "beforeTextChanged")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!isNetworkAvailable(context?.applicationContext)){
                    refreshFragment()
                }
                Log.d("Main", "onTextChanged")
            }

            override fun afterTextChanged(p0: Editable?) {
                try{
                    getApiResults(secondConvTV.id)
                }catch (e: Exception){
                    Log.e("Main:", "$e")
                }
            }
        }

        dateConvWatcher = object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Main", "beforeTextChanged")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!isNetworkAvailable(context?.applicationContext)){
                    refreshFragment()
                }
                Log.d("Main", "onTextChanged")
            }

            override fun afterTextChanged(p0: Editable?) {
                try{
                    getApiResults(firstConvTV.id)
                    getApiResults(secondConvTV.id)
                }catch (e: Exception){
                    Log.e("Main:", "$e")
                }
            }
        }

        firstConvTV.addTextChangedListener(firstTextWatcher)
        secondConvTV.addTextChangedListener(secondTextWatcher)
        dateText?.addTextChangedListener(dateConvWatcher)
    }
}
