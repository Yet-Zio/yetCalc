package yetzio.yetcalc.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.airbnb.paris.Paris
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import yetzio.yetcalc.R
import yetzio.yetcalc.component.UnitConv
import yetzio.yetcalc.model.UnitConvViewModel
import yetzio.yetcalc.views.UnitConvActivity
import kotlin.properties.Delegates

class AngleFragment : Fragment() {

    private var firstConv: EditText? = null
    private var secondConv: EditText? = null

    private var spinner: Spinner? = null
    private var spinner2: Spinner? = null

    private var firstConvWatcher: TextWatcher? = null
    private var secondConvWatcher: TextWatcher? = null

    private val mCoroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var pViewModel: UnitConvViewModel
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

        val v = inflater.inflate(R.layout.fragment_unitconversions, container, false)

        pViewModel = (activity as? UnitConvActivity)?.mViewModel!!

        firstConv = v.findViewById(R.id.et_firstConversion)
        secondConv = v.findViewById(R.id.et_secondConversion)

        spinner = v.findViewById(R.id.spinner_firstConversion)
        spinner2 = v.findViewById(R.id.spinner_secondConversion)

        setupSpinner()
        textChanged()

        if(pLight){
            Paris.style(firstConv).apply(R.style.ConvTextStyleLight)
            Paris.style(secondConv).apply(R.style.ConvTextStyleLight)

            Paris.style(spinner).apply(R.style.yetSpinnerStyleLight)
            Paris.style(spinner2).apply(R.style.yetSpinnerStyleLight)

            Paris.style(v.findViewById<LinearLayout>(R.id.ll_parent)).apply(R.style.GenericLight)
            Paris.style(v.findViewById<LinearLayout>(R.id.cardll_parent)).apply(R.style.GenericLight)
        }
        else{
            Paris.style(v.findViewById<LinearLayout>(R.id.ll_parent)).apply(R.style.GenericDark)
            Paris.style(v.findViewById<LinearLayout>(R.id.cardll_parent)).apply(R.style.GenericDark)
        }
        return v
    }

    fun convert(id: Int){
        mCoroutineScope.launch {
            try {
                when(id){
                    R.id.et_firstConversion -> {
                        val res = UnitConv.Angle.convert(pViewModel._angleftpos, pViewModel._anglesdpos
                            , firstConv?.text.toString().toDouble())

                        if(res.toString() != secondConv?.text.toString()){
                            secondConv?.removeTextChangedListener(secondConvWatcher)
                            secondConv?.setText(res.toString())
                            secondConv?.addTextChangedListener(secondConvWatcher)
                        }
                    }
                    R.id.et_secondConversion -> {
                        val res = UnitConv.Angle.convert(pViewModel._anglesdpos, pViewModel._angleftpos
                            , secondConv?.text.toString().toDouble())

                        if(res.toString() != firstConv?.text.toString()){
                            firstConv?.removeTextChangedListener(firstConvWatcher)
                            firstConv?.setText(res.toString())
                            firstConv?.addTextChangedListener(firstConvWatcher)
                        }
                    }
                }
            } catch (e: Exception){
                Log.e("Main:", "$e")
            }
        }
    }

    fun getConversionResults(id: Int){
        when(id){
            R.id.et_firstConversion -> {
                if(firstConv != null){
                    if(firstConv!!.text.isNotEmpty() && firstConv!!.text.isNotBlank()){
                        convert(id)
                    }
                }
            }
            R.id.et_secondConversion -> {
                if(secondConv != null){
                    if(secondConv!!.text.isNotEmpty() && secondConv!!.text.isNotBlank()){
                        convert(id)
                    }
                }
            }
        }
    }

    fun setupSpinner(){
        activity?.let {
            if(pLight){
                ArrayAdapter.createFromResource(it, R.array.anglelist, R.layout.spinner_itemlight)
                    .also { adapter ->
                        spinner?.adapter = adapter
                    }
            }
            else{
                ArrayAdapter.createFromResource(it, R.array.anglelist, R.layout.spinner_item)
                    .also { adapter ->
                        spinner?.adapter = adapter
                    }
            }

        }

        activity?.let {
            if(pLight){
                ArrayAdapter.createFromResource(it, R.array.anglelist, R.layout.spinner_itemlight)
                    .also { adapter ->
                        spinner2?.adapter = adapter
                    }
            }
            else{
                ArrayAdapter.createFromResource(it, R.array.anglelist, R.layout.spinner_item)
                    .also { adapter ->
                        spinner2?.adapter = adapter
                    }
            }
        }

        spinner?.onItemSelectedListener = (object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, vw: View?, pos: Int, id: Long) {
                if(!pViewModel._anglespinInit){
                    pViewModel._anglespinInit = true
                    pViewModel._angleftpos = pos
                    pViewModel._anglesdpos = 0

                    getConversionResults(firstConv?.id!!)
                    getConversionResults(secondConv?.id!!)
                }
                else{
                    pViewModel._angleftpos = pos
                    pViewModel._anglesdpos = spinner2?.selectedItemPosition!!

                    getConversionResults(firstConv?.id!!)
                    getConversionResults(secondConv?.id!!)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })

        spinner2?.onItemSelectedListener = (object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, vw: View?, pos: Int, id: Long) {
                if(!pViewModel._sanglespinInit){
                    pViewModel._sanglespinInit = true
                    pViewModel._angleftpos = 0
                    pViewModel._anglesdpos = pos

                    getConversionResults(firstConv?.id!!)
                }
                else{
                    pViewModel._angleftpos = spinner?.selectedItemPosition!!
                    pViewModel._anglesdpos = pos

                    getConversionResults(firstConv?.id!!)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun textChanged(){
        firstConvWatcher = object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Main", "beforeTextChanged")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Main", "onTextChanged")
            }

            override fun afterTextChanged(p0: Editable?) {
                try{
                    getConversionResults(firstConv?.id!!)
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
                Log.d("Main", "onTextChanged")
            }

            override fun afterTextChanged(p0: Editable?) {
                try{
                    getConversionResults(secondConv?.id!!)
                }catch (e: Exception){
                    Log.e("Main:", "$e")
                }
            }
        }

        firstConv?.addTextChangedListener(firstConvWatcher)
        secondConv?.addTextChangedListener(secondConvWatcher)
    }
}