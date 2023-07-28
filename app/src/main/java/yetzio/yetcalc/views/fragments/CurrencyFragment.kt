package yetzio.yetcalc.views.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.airbnb.paris.Paris
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import yetzio.yetcalc.R
import yetzio.yetcalc.model.UnitConvViewModel
import yetzio.yetcalc.utils.isNetworkAvailable
import yetzio.yetcalc.views.UnitConvActivity
import yetzio.yetcalc.views.fragments.adapters.ViewPagerAdapter
import java.net.URL
import java.util.*
import kotlin.properties.Delegates


class CurrencyFragment : Fragment() {

    var API = ""

    private var firstConv: EditText? = null
    private var secondConv: EditText? = null
    private var dateText: TextView? = null
    private var datePkr: ImageView? = null

    private var spinner: Spinner? = null
    private var spinner2: Spinner? = null
    private var baseCur = "INR"
    private var convCur = "USD"
    private var convRate = 0f

    private var firstConvWatcher: TextWatcher? = null
    private var secondConvWatcher: TextWatcher? = null
    private var dateConvWatcher: TextWatcher? = null

    private val mCoroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var pViewModel: UnitConvViewModel

    private var tryAgainBT: Button? = null
    private lateinit var pTheme: String
    private var pDark by Delegates.notNull<Boolean>()
    private var pLight by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        pTheme = (activity as? UnitConvActivity)?.theme.toString()
        pDark = (activity as? UnitConvActivity)?.dark!!
        pLight = (activity as? UnitConvActivity)?.light!!

        var v: View?
        if(isNetworkAvailable(context?.applicationContext)){
            v = createConv(inflater, container)
        }
        else{
            v = if(pLight){
                inflater.inflate(R.layout.no_internetlight, container, false)
            } else{
                inflater.inflate(R.layout.no_internet, container, false)
            }
            tryAgainBT = v.findViewById(R.id.nointtryagain)
            tryAgainBT?.setOnClickListener{
                refreshFragment()
            }
        }

        return v
    }

    private fun refreshFragment(){
        val pViewPager = (activity as? UnitConvActivity)?.viewPager
        val pViewPagerADP = pViewPager?.adapter as ViewPagerAdapter
        pViewPagerADP.mFragList[0] = CurrencyFragment()
        pViewPager.adapter = pViewPagerADP
    }

    private fun createConv(inflater: LayoutInflater, container: ViewGroup?): View{
        val v = inflater.inflate(R.layout.fragment_currency, container, false)
        pViewModel = (activity as? UnitConvActivity)?.mViewModel!!

        firstConv = v.findViewById(R.id.et_firstConversion)
        secondConv = v.findViewById(R.id.et_secondConversion)
        dateText = v.findViewById(R.id.datecurrencyconv)

        datePkr = v.findViewById(R.id.datepickerCurrency)

        spinner = v.findViewById(R.id.spinner_firstConversion)
        spinner2 = v.findViewById(R.id.spinner_secondConversion)

        restoreDateConf()

        setupSpinner()
        setupDatePicker()
        textChanged()

        if(pLight){
            Paris.style(firstConv).apply(R.style.ConvTextStyleLight)
            Paris.style(secondConv).apply(R.style.ConvTextStyleLight)

            Paris.style(spinner).apply(R.style.yetSpinnerStyleLight)
            Paris.style(spinner2).apply(R.style.yetSpinnerStyleLight)

            Paris.style(dateText).apply(R.style.DateHintStyleLight)
            Paris.style(datePkr).apply(R.style.DatePickerImgSrcStyleLight)

            Paris.style(v.findViewById<LinearLayout>(R.id.ll_parent)).apply(R.style.GenericLight)
            Paris.style(v.findViewById<LinearLayout>(R.id.cardll_parent)).apply(R.style.GenericLight)
        }
        else{
            Paris.style(v.findViewById<LinearLayout>(R.id.ll_parent)).apply(R.style.GenericDark)
            Paris.style(v.findViewById<LinearLayout>(R.id.cardll_parent)).apply(R.style.GenericDark)
        }
        return v
    }

    private fun restoreDateConf(){
        if(pViewModel.current_date != ""){
            println("Date changed: ${pViewModel.current_date}")
            dateText?.text = pViewModel.current_date
        }
    }

    private fun fetchApiResults(id: Int){
        API = "https://api.exchangerate.host/convert?from=$baseCur&to=$convCur"

        if(dateText != null){
            if(dateText!!.text.isNotEmpty() && dateText!!.text.isNotBlank()){
                API += "&date=${dateText!!.text}"
            }
        }

        mCoroutineScope.launch {
            try{
                val resDeferred = mCoroutineScope.async(Dispatchers.IO){
                    val apiresult = URL(API).readText()
                    val jsonObj = JSONObject(apiresult)

                    jsonObj.getJSONObject("info").getString("rate").toFloat()
                }

                convRate = resDeferred.await()

                when(id){
                    R.id.et_firstConversion -> {
                        val res = ((firstConv!!.text.toString().toFloat()) * convRate).toString()
                        if(res != secondConv?.text.toString()) {
                            secondConv?.removeTextChangedListener(secondConvWatcher)
                            dateText?.removeTextChangedListener(dateConvWatcher)
                            secondConv?.setText(res)
                            secondConv?.addTextChangedListener(secondConvWatcher)
                            dateText?.addTextChangedListener(dateConvWatcher)

                        }
                    }
                    R.id.et_secondConversion -> {
                        val res = ((secondConv!!.text.toString().toFloat()) / convRate).toString()
                        if(res != firstConv?.text.toString()){
                            firstConv?.removeTextChangedListener(firstConvWatcher)
                            dateText?.removeTextChangedListener(dateConvWatcher)
                            firstConv?.setText(res)
                            firstConv?.addTextChangedListener(firstConvWatcher)
                            dateText?.addTextChangedListener(dateConvWatcher)
                        }
                    }
                }

            }catch (e: Exception){
                Log.e("Main:", "$e")
            }
        }
    }

    private fun getApiResults(id: Int){
        when(id){
            R.id.et_firstConversion -> {
                if(firstConv != null){
                    if(firstConv!!.text.isNotEmpty() && firstConv!!.text.isNotBlank()){
                        if(baseCur == convCur){
                            //Toast.makeText(context?.applicationContext, "Cannot convert the same currency!", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            fetchApiResults(id)
                        }
                    }
                }
            }
            R.id.et_secondConversion -> {
                if(secondConv != null){
                    if(secondConv!!.text.isNotEmpty() && secondConv!!.text.isNotBlank()){
                        if(baseCur == convCur){
                            //Toast.makeText(context?.applicationContext, "Cannot convert the same currency!", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            fetchApiResults(id)
                        }
                    }
                }
            }
        }
    }

    private fun setupSpinner(){
        activity?.let {
            if(pLight){
                ArrayAdapter.createFromResource(it, R.array.currencies_one, R.layout.spinner_itemlight)
                    .also { adapter ->
                        spinner?.adapter= adapter
                    }
            }
            else{
                ArrayAdapter.createFromResource(it, R.array.currencies_one, R.layout.spinner_item)
                    .also { adapter ->
                        spinner?.adapter= adapter
                    }
            }
        }

        activity?.let {
            if(pLight){
                ArrayAdapter.createFromResource(it, R.array.currencies_two, R.layout.spinner_itemlight)
                    .also { adapter2 ->
                        spinner2?.adapter= adapter2
                    }
            }
            else{
                ArrayAdapter.createFromResource(it, R.array.currencies_two, R.layout.spinner_item)
                    .also { adapter2 ->
                        spinner2?.adapter= adapter2
                    }
            }
        }

        spinner?.onItemSelectedListener = (object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, spview: View?, pos: Int, id: Long) {
                if(isNetworkAvailable(context?.applicationContext)){
                    baseCur = parent?.getItemAtPosition(pos).toString()
                    getApiResults(firstConv?.id!!)
                    getApiResults(secondConv?.id!!)
                }
                else{
                    Toast.makeText(context?.applicationContext, "No Internet!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })

        spinner2?.onItemSelectedListener = (object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, spview: View?, pos: Int, id: Long) {
                if(isNetworkAvailable(context?.applicationContext)){
                    convCur = parent?.getItemAtPosition(pos).toString()
                    getApiResults(firstConv?.id!!)
                }
                else{
                    Toast.makeText(context?.applicationContext, "No Internet!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setupDatePicker(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        datePkr?.setOnClickListener{
            val dpd =
                activity?.let { it1 ->
                    if(pDark){
                        DatePickerDialog(it1, R.style.DatePickerTheme, DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, mDay ->
                            pViewModel.current_date = "$mYear-$mMonth-$mDay"
                            println("Date pick: ${pViewModel.current_date}")
                            dateText?.text = "$mYear-$mMonth-$mDay"
                        }, year, month, day)
                    }
                    else{
                        DatePickerDialog(it1, R.style.DatePickerThemeLight, DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, mDay ->
                            pViewModel.current_date = "$mYear-$mMonth-$mDay"
                            println("Date pick: ${pViewModel.current_date}")
                            dateText?.text = "$mYear-$mMonth-$mDay"
                        }, year, month, day)
                    }
                }

            dpd?.show()
        }
    }

    private fun textChanged(){

        firstConvWatcher = object: TextWatcher{
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
                    getApiResults(firstConv?.id!!)
                }catch (e: Exception){
                    Log.e("Main:", "$e")
                }
            }

        }


        secondConvWatcher = object: TextWatcher{
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
                    getApiResults(secondConv?.id!!)
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
                    getApiResults(firstConv?.id!!)
                    getApiResults(secondConv?.id!!)
                }catch (e: Exception){
                    Log.e("Main:", "$e")
                }
            }
        }

        firstConv?.addTextChangedListener(firstConvWatcher)
        secondConv?.addTextChangedListener(secondConvWatcher)
        dateText?.addTextChangedListener(dateConvWatcher)

    }
}