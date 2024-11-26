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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import yetzio.yetcalc.R
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.component.UnitConv
import yetzio.yetcalc.dialogs.showUnitConvSearchDialog
import yetzio.yetcalc.enums.UnitType
import yetzio.yetcalc.models.UnitConvViewModel
import yetzio.yetcalc.utils.copyToClipboard
import yetzio.yetcalc.utils.getUnitsList
import yetzio.yetcalc.views.UnitConvActivity
import yetzio.yetcalc.widget.CalcText

class AngleFragment : Fragment() {

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

    var unitConvDialog: AlertDialog? = null
    private val mCoroutineScope = CoroutineScope(Dispatchers.Main)

    private lateinit var firstTextWatcher: TextWatcher
    private lateinit var secondTextWatcher: TextWatcher

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_unitconversions, container, false)

        pViewModel = (activity as? UnitConvActivity)?.mViewModel!!
        p_preferences = (activity as? UnitConvActivity)?.preferences!!
        p_editor = (activity as? UnitConvActivity)?.editor!!

        val saveRecentUnitPref = p_preferences.getBoolean(SharedPrefs.SAVE_RECENT_UNITKEY, true)
        val initFirst = p_preferences.getInt(SharedPrefs.ANGLE_FIRST, 0)
        val initSecond = p_preferences.getInt(SharedPrefs.ANGLE_SECOND, 0)

        if(saveRecentUnitPref){
            pViewModel._angleftpos.value = initFirst
            pViewModel._anglesdpos.value = initSecond
        }
        else{
            pViewModel._angleftpos.value = 0
            pViewModel._anglesdpos.value = 0
        }

        firstConvField = view.findViewById(R.id.firstConvFieldContainer)!!
        firstConvTV = view.findViewById(R.id.firstConvField)!!

        firstConvDropField = view.findViewById(R.id.firstConvDropDown)
        firstConvDropDownField = view.findViewById(R.id.firstConvDropDownField)

        secondConvField = view.findViewById(R.id.secondConvFieldContainer)
        secondConvTV = view.findViewById(R.id.secondConvField)

        secondConvDropField = view.findViewById(R.id.secondConvDropDown)
        secondConvDropDownField = view.findViewById(R.id.secondConvDropDownField)

        switchUnitsButton = view.findViewById(R.id.switchUnitsButton)

        firstConvDropDownField.setText(requireContext().getUnitsList(UnitType.ANGLE)[initFirst])
        secondConvDropDownField.setText(requireContext().getUnitsList(UnitType.ANGLE)[initSecond])

        setupClipboardEvent()
        setupSelections()
        textChanged()

        return view
    }

    private fun setupClipboardEvent(){
        firstConvField.setStartIconOnClickListener {
            parentFragment?.activity?.applicationContext?.copyToClipboard(firstConvTV.text.toString())
        }

        secondConvField.setStartIconOnClickListener {
            parentFragment?.activity?.applicationContext?.copyToClipboard(secondConvTV.text.toString())
        }
    }

    private fun convert(id: Int){
        mCoroutineScope.launch {
            try {
                when(id){
                    R.id.firstConvField -> {
                        val res = pViewModel._angleftpos.value?.let {
                            pViewModel._anglesdpos.value?.let { it1 ->
                                UnitConv.Angle.convert(
                                    it, it1, firstConvTV.text.toString().toDouble())
                            }
                        }

                        if(res.toString() != secondConvTV.text.toString()){
                            secondConvTV.removeTextChangedListener(secondTextWatcher)
                            secondConvTV.setText(res.toString())
                            secondConvTV.addTextChangedListener(secondTextWatcher)
                        }
                    }
                    R.id.secondConvField -> {
                        val res = pViewModel._anglesdpos.value?.let {
                            pViewModel._angleftpos.value?.let { it1 ->
                                UnitConv.Angle.convert(
                                    it, it1, secondConvTV.text.toString().toDouble())
                            }
                        }

                        if(res.toString() != firstConvTV.text.toString()){
                            firstConvTV.removeTextChangedListener(firstTextWatcher)
                            firstConvTV.setText(res.toString())
                            firstConvTV.addTextChangedListener(firstTextWatcher)
                        }
                    }
                }
            } catch (e: Exception){
                Log.e("Main:", "$e")
            }
        }
    }

    private fun getConversionResults(id: Int){
        when(id){
            R.id.firstConvField -> {
                if(firstConvTV.text!!.isNotEmpty() && firstConvTV.text!!.isNotBlank()){
                    if(pViewModel._angleftpos.value != pViewModel._anglesdpos.value){
                        convert(id)
                    }
                }
            }
            R.id.secondConvField -> {
                if(secondConvTV.text!!.isNotEmpty() && secondConvTV.text!!.isNotBlank()){
                    if(pViewModel._angleftpos.value != pViewModel._anglesdpos.value){
                        convert(id)
                    }
                }
            }
        }
    }

    private fun setupSelections(){
        firstConvDropField.setEndIconOnClickListener {
            pViewModel.currentUnit.value = UnitType.ANGLE
            pViewModel.first.value = true
            unitConvDialog = showUnitConvSearchDialog(requireContext(), requireActivity().applicationContext, resources.getStringArray(R.array.anglelist).toCollection(ArrayList()), pViewModel, viewLifecycleOwner)
        }

        firstConvDropDownField.setOnClickListener {
            pViewModel.currentUnit.value = UnitType.ANGLE
            pViewModel.first.value = true
            unitConvDialog = showUnitConvSearchDialog(requireContext(), requireActivity().applicationContext, resources.getStringArray(R.array.anglelist).toCollection(ArrayList()), pViewModel, viewLifecycleOwner)
        }

        secondConvDropField.setEndIconOnClickListener {
            pViewModel.currentUnit.value = UnitType.ANGLE
            pViewModel.first.value = false
            unitConvDialog = showUnitConvSearchDialog(requireContext(), requireActivity().applicationContext, resources.getStringArray(R.array.anglelist).toCollection(ArrayList()), pViewModel, viewLifecycleOwner)
        }

        secondConvDropDownField.setOnClickListener {
            pViewModel.currentUnit.value = UnitType.ANGLE
            pViewModel.first.value = false
            unitConvDialog = showUnitConvSearchDialog(requireContext(), requireActivity().applicationContext, resources.getStringArray(R.array.anglelist).toCollection(ArrayList()), pViewModel, viewLifecycleOwner)
        }

        switchUnitsButton.setOnClickListener {
            val temp = pViewModel._angleftpos.value
            pViewModel._angleftpos.value = pViewModel._anglesdpos.value
            pViewModel._anglesdpos.value = temp
        }

        pViewModel._angleftpos.observe(viewLifecycleOwner) { newValue ->
            if (newValue != null) {
                p_editor.putInt(SharedPrefs.ANGLE_FIRST, newValue)
            }
            p_editor.apply()

            firstConvDropDownField.setText(requireContext().getUnitsList(UnitType.ANGLE)[newValue!!])
            unitConvDialog?.dismiss()

            firstConvTV.id.let { getConversionResults(it) }
            secondConvTV.id.let { getConversionResults(it) }
        }

        pViewModel._anglesdpos.observe(viewLifecycleOwner) { newValue ->
            if (newValue != null) {
                p_editor.putInt(SharedPrefs.ANGLE_SECOND, newValue)
            }
            p_editor.apply()

            secondConvDropDownField.setText(requireContext().getUnitsList(UnitType.ANGLE)[newValue!!])
            unitConvDialog?.dismiss()

            firstConvTV.id.let { getConversionResults(it) }
        }
    }

    private fun textChanged(){
        firstTextWatcher = object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Main", "beforeTextChanged")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Main", "onTextChanged")
            }

            override fun afterTextChanged(p0: Editable?) {
                try{
                    firstConvTV.id.let { getConversionResults(it) }
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
                Log.d("Main", "onTextChanged")
            }

            override fun afterTextChanged(p0: Editable?) {
                try{
                    getConversionResults(secondConvTV.id)
                }catch (e: Exception){
                    Log.e("Main:", "$e")
                }
            }
        }

        firstConvTV.addTextChangedListener(firstTextWatcher)
        secondConvTV.addTextChangedListener(secondTextWatcher)
    }
}
