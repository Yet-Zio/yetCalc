package yetzio.yetcalc.views

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import yetzio.yetcalc.R
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.config.CalcBaseActivity
import yetzio.yetcalc.config.CalcView
import yetzio.yetcalc.enums.NumberSystem
import yetzio.yetcalc.models.ProgramCalcViewModel
import yetzio.yetcalc.utils.getScreenOrientation
import yetzio.yetcalc.utils.getThemeColor
import yetzio.yetcalc.utils.hideLayoutWithAnimationF
import yetzio.yetcalc.utils.setVibOnClick
import yetzio.yetcalc.utils.showLayoutWithAnimationF
import yetzio.yetcalc.widget.CalcText
import yetzio.yetcalc.widget.CalcTextListener

class ProgramCalcActivity : CalcBaseActivity(), View.OnClickListener{

    // Buttons list
    private var button_list = ArrayList<MaterialButton>()

    private val mCoroutineScope = CoroutineScope(Dispatchers.Main)

    private lateinit var acbutton: MaterialButton
    private lateinit var leftshbutton: MaterialButton
    private lateinit var rightshbutton: MaterialButton

    private lateinit var divideop: MaterialButton
    private lateinit var mulop: MaterialButton
    private lateinit var minusop: MaterialButton
    private lateinit var plusop: MaterialButton

    // Bitwise operators
    private lateinit var andbt: MaterialButton
    private lateinit var orbt: MaterialButton
    private lateinit var notbt: MaterialButton
    private lateinit var nandbt: MaterialButton
    private lateinit var norbt: MaterialButton
    private lateinit var xorbt: MaterialButton
    private lateinit var xnorbt: MaterialButton
    private lateinit var ushrbt: MaterialButton

    private lateinit var leftbracbt: MaterialButton
    private lateinit var rightbracbt: MaterialButton

    private lateinit var equalop: MaterialButton
    private lateinit var equalopsecond: MaterialButton

    private lateinit var doublezerobt: MaterialButton
    private lateinit var percentbutton: MaterialButton

    private lateinit var num0bt: MaterialButton
    private lateinit var num0second: MaterialButton

    private lateinit var bkspacebt: MaterialButton
    private lateinit var num7bt: MaterialButton
    private lateinit var num8bt: MaterialButton
    private lateinit var num9bt: MaterialButton
    private lateinit var num4bt: MaterialButton
    private lateinit var num5bt: MaterialButton
    private lateinit var num6bt: MaterialButton
    private lateinit var num1bt: MaterialButton
    private lateinit var num2bt: MaterialButton
    private lateinit var num3bt: MaterialButton

    // Hexadecimal characters
    private lateinit var Abutton: MaterialButton
    private lateinit var Bbutton: MaterialButton
    private lateinit var Cbutton: MaterialButton
    private lateinit var Dbutton: MaterialButton
    private lateinit var Ebutton: MaterialButton
    private lateinit var Fbutton: MaterialButton

    private lateinit var rolbutton: MaterialButton
    private lateinit var rorbutton: MaterialButton

    private lateinit var textexpression: CalcText
    private lateinit var textres: MaterialTextView

    // Drop Container
    private lateinit var dropContainer: FrameLayout
    private lateinit var dropLineContainer: LinearLayout
    private lateinit var dropImgView: ImageView

    // Main Containers - Portrait
    private lateinit var droppedBtnLyt: FrameLayout
    private lateinit var mainbuttonlyt: FrameLayout

    // Number System Containers
    private lateinit var hexContainer: LinearLayout
    private lateinit var decContainer: LinearLayout
    private lateinit var octContainer: LinearLayout
    private lateinit var binContainer: LinearLayout

    private lateinit var hexHead: MaterialTextView
    private lateinit var decHead: MaterialTextView
    private lateinit var octHead: MaterialTextView
    private lateinit var binHead: MaterialTextView

    private lateinit var hexRes: MaterialTextView
    private lateinit var decRes: MaterialTextView
    private lateinit var octRes: MaterialTextView
    private lateinit var binRes: MaterialTextView

    private lateinit var settingsLauncher: ActivityResultLauncher<Intent>
    private lateinit var settingsBt: MaterialButton

    private lateinit var mviewModel: ProgramCalcViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        currentView = CalcView.PROGRAMMER
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program_calc)

        modeSelector = findViewById(R.id.modeselector)
        setupModeSelector()

        mviewModel = ViewModelProvider(this)[ProgramCalcViewModel::class.java]
        mviewModel.hapticPref = preferences.getBoolean(SharedPrefs.HAPTICKEY, true)
        settingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            mviewModel.hapticPref = preferences.getBoolean(SharedPrefs.HAPTICKEY, true)
        }
        settingsBt = findViewById(R.id.settingsButton)
        settingsBt.setOnClickListener {
            launchSettings()
        }

        textexpression = findViewById(R.id.textexpression)
        textexpression.showSoftInputOnFocus = false

        textexpression.addListener(object: CalcTextListener {
            // evaluates expression on paste event
            override fun onUpdate() {
                evaluate_expr()
            }

            override fun onCutText() {
                if(textexpression.text?.isNotEmpty()!!){
                    evaluate_expr()
                }
                else{
                    mviewModel.result = ""
                    textres.setText("")
                }
            }
        })

        textres = findViewById(R.id.textres)

        if(getScreenOrientation(applicationContext) == Configuration.ORIENTATION_PORTRAIT){
            dropContainer = findViewById(R.id.dropContainer)
            dropImgView = findViewById(R.id.dropArrow)
            droppedBtnLyt = findViewById(R.id.altButtonContainer)
            mainbuttonlyt = findViewById(R.id.buttonContainer)

            dropLineContainer = findViewById(R.id.dropLineContainer)

            var rotAnimator = ObjectAnimator()
            rotAnimator.setDuration(1000)

            dropContainer.setOnClickListener { view ->
                if (!droppedBtnLyt.isVisible) {
                    rotAnimator = ObjectAnimator.ofFloat(dropImgView, "rotation", 0f, 180f)
                    rotAnimator.start()
                    hideLayoutWithAnimationF(mainbuttonlyt, dropLineContainer, mainbuttonlyt, droppedBtnLyt)
                    showLayoutWithAnimationF(droppedBtnLyt, dropLineContainer)
                } else {
                    rotAnimator = ObjectAnimator.ofFloat(dropImgView, "rotation", 180f, 0f)
                    rotAnimator.start()
                    hideLayoutWithAnimationF(droppedBtnLyt, dropLineContainer, mainbuttonlyt, droppedBtnLyt)
                    showLayoutWithAnimationF(mainbuttonlyt, dropLineContainer)
                }
            }

            num0second = findViewById(R.id.numberzerosecond)
            equalopsecond = findViewById(R.id.equalopsecond)
            button_list.addAll(arrayListOf(num0second, equalopsecond))
        }

        num7bt = findViewById(R.id.numberseven)
        num8bt = findViewById(R.id.numbereight)
        num9bt = findViewById(R.id.numbernine)
        num4bt = findViewById(R.id.numberfour)
        num5bt = findViewById(R.id.numberfive)
        num6bt = findViewById(R.id.numbersix)
        num1bt = findViewById(R.id.numberone)
        num2bt = findViewById(R.id.numbertwo)
        num3bt = findViewById(R.id.numberthree)
        num0bt = findViewById(R.id.numberzero)

        doublezerobt = findViewById(R.id.doublezerobt)
        percentbutton = findViewById(R.id.percentbutton)

        equalop = findViewById(R.id.equalop)
        bkspacebt = findViewById(R.id.backspacebutton)

        acbutton = findViewById(R.id.acbutton)
        leftshbutton = findViewById(R.id.leftshiftbutton)
        rightshbutton = findViewById(R.id.rightshiftbutton)

        divideop = findViewById(R.id.divideop)
        mulop = findViewById(R.id.mulop)
        minusop = findViewById(R.id.minusop)
        plusop = findViewById(R.id.plusop)

        andbt = findViewById(R.id.andbutton)
        orbt = findViewById(R.id.orbutton)
        notbt = findViewById(R.id.notbutton)
        nandbt = findViewById(R.id.nandbutton)
        norbt = findViewById(R.id.norbutton)
        xorbt = findViewById(R.id.xorbutton)
        xnorbt = findViewById(R.id.xnorbutton)
        ushrbt = findViewById(R.id.ushrbutton)
        rolbutton = findViewById(R.id.rolbutton)
        rorbutton = findViewById(R.id.rorbutton)

        leftbracbt = findViewById(R.id.leftbracbutton)
        rightbracbt = findViewById(R.id.rightbracbutton)

        Abutton = findViewById(R.id.Abutton)
        Bbutton = findViewById(R.id.Bbutton)
        Cbutton = findViewById(R.id.Cbutton)
        Dbutton = findViewById(R.id.Dbutton)
        Ebutton = findViewById(R.id.Ebutton)
        Fbutton = findViewById(R.id.Fbutton)

        hexContainer = findViewById(R.id.hexContainer)
        decContainer = findViewById(R.id.decContainer)
        octContainer = findViewById(R.id.octContainer)
        binContainer = findViewById(R.id.binContainer)

        hexHead = findViewById(R.id.HEXhead)
        decHead = findViewById(R.id.DEChead)
        octHead = findViewById(R.id.OCThead)
        binHead = findViewById(R.id.BINhead)

        hexRes = findViewById(R.id.HEXres)
        decRes = findViewById(R.id.DECres)
        octRes = findViewById(R.id.OCTres)
        binRes = findViewById(R.id.BINres)

        button_list.addAll(arrayListOf(
            rolbutton,
            num7bt, num8bt, num9bt,
            num4bt, num5bt, num6bt,
            num1bt, num2bt, num3bt,
            num0bt, doublezerobt, percentbutton,
            equalop, bkspacebt, rorbutton,
            acbutton, leftbracbt, rightbracbt,
            leftshbutton, rightshbutton, ushrbt,
            divideop, mulop, minusop, plusop,
            andbt, orbt, notbt, nandbt, norbt, xorbt, xnorbt,
            Abutton, Bbutton, Cbutton, Dbutton, Ebutton, Fbutton
        ))

        setOnClickListeners()
        setOnLongClickListeners()
        setupNumberSystemContainers()

        restoreModesAndConfiguration(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val setIntent = Intent(Intent.ACTION_MAIN)
                setIntent.addCategory(Intent.CATEGORY_HOME)
                setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(setIntent)
            }
        })
    }

    private fun launchSettings(){
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        settingsLauncher.launch(settingsIntent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(getString(R.string.text_res), mviewModel.result)

        super.onSaveInstanceState(outState)
    }

    private fun restoreModesAndConfiguration(savedInstanceState: Bundle?){
        if(savedInstanceState != null){
            textres.text = savedInstanceState.getString(getString(R.string.text_res))
            mviewModel.result = textres.text.toString()
        }
    }

    fun setOnClickListeners(){
        for (bt in button_list){
            bt.setOnClickListener(this)
        }
    }

    private fun setOnLongClickListeners() {
        bkspacebt.setOnLongClickListener {
            if(mviewModel.hapticPref){
                bkspacebt.isHapticFeedbackEnabled = true
            }
            else{
                bkspacebt.isHapticFeedbackEnabled = false
            }
            clearFields()
            true
        }
    }

    override fun onClick(view: View?) {
        val buttonId = view?.id

        if(buttonId!= null){
            buttonHandler(buttonId)
        }

        setVibOnClick(applicationContext)
    }

    private fun setupNumberSystemContainers(){
        setCurrentNumberSystem()

        if(mviewModel.init){
            decRes.text = mviewModel.decResult
            hexRes.text = mviewModel.hexResult
            octRes.text = mviewModel.octResult
            binRes.text = mviewModel.binResult
        }
        mviewModel.init = true

        decContainer.setOnClickListener {
            mviewModel.numberSys.value = NumberSystem.DEC
            println("DEC now active")
        }

        hexContainer.setOnClickListener {
            mviewModel.numberSys.value = NumberSystem.HEX
            println("HEX now active")
        }

        octContainer.setOnClickListener {
            mviewModel.numberSys.value = NumberSystem.OCT
            println("OCT now active")
        }

        binContainer.setOnClickListener {
            mviewModel.numberSys.value = NumberSystem.BIN
            println("BIN now active")
        }

        mviewModel.numberSys.observe(this, Observer { _ ->
            println("Number sys changed")
            setCurrentNumberSystem()
        })
    }

    private fun setCurrentNumberSystem(){
        when(mviewModel.numberSys.value){
            NumberSystem.DEC -> {
                decHead.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))
                decRes.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

                hexHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                hexRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                octHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                octRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                binHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                binRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                setResultonNumSys()
                setDecimalButtons()
            }
            NumberSystem.HEX -> {
                hexHead.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))
                hexRes.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

                decHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                decRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                octHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                octRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                binHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                binRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                setResultonNumSys()
                setHexaDecimalButtons()
            }
            NumberSystem.OCT -> {
                octHead.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))
                octRes.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

                decHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                decRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                hexHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                hexRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                binHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                binRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                setResultonNumSys()
                setOctalButtons()
            }
            NumberSystem.BIN -> {
                binHead.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))
                binRes.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

                decHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                decRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                hexHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                hexRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                octHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                octRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                setResultonNumSys()
                setBinaryButtons()
            }

            else -> {
                decHead.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))
                decRes.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

                hexHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                hexRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                octHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                octRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                binHead.setTextColor(getThemeColor(R.attr.disabledTextColor))
                binRes.setTextColor(getThemeColor(R.attr.disabledTextColor))

                setResultonNumSys()
                setDecimalButtons()
            }
        }
    }

    private fun setHexaDecimalButtons(){
        Abutton.isEnabled = true
        Abutton.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        Bbutton.isEnabled = true
        Bbutton.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        Cbutton.isEnabled = true
        Cbutton.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        Dbutton.isEnabled = true
        Dbutton.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        Ebutton.isEnabled = true
        Ebutton.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        Fbutton.isEnabled = true
        Fbutton.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num1bt.isEnabled = true
        num1bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num2bt.isEnabled = true
        num2bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num3bt.isEnabled = true
        num3bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num4bt.isEnabled = true
        num4bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num5bt.isEnabled = true
        num5bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num6bt.isEnabled = true
        num6bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num7bt.isEnabled = true
        num7bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num8bt.isEnabled = true
        num8bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num9bt.isEnabled = true
        num9bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))
    }

    private fun setDecimalButtons(){
        Abutton.isEnabled = false
        Abutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Bbutton.isEnabled = false
        Bbutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Cbutton.isEnabled = false
        Cbutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Dbutton.isEnabled = false
        Dbutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Ebutton.isEnabled = false
        Ebutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Fbutton.isEnabled = false
        Fbutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        num1bt.isEnabled = true
        num1bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num2bt.isEnabled = true
        num2bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num3bt.isEnabled = true
        num3bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num4bt.isEnabled = true
        num4bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num5bt.isEnabled = true
        num5bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num6bt.isEnabled = true
        num6bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num7bt.isEnabled = true
        num7bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num8bt.isEnabled = true
        num8bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num9bt.isEnabled = true
        num9bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))
    }

    private fun setOctalButtons(){
        Abutton.isEnabled = false
        Abutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Bbutton.isEnabled = false
        Bbutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Cbutton.isEnabled = false
        Cbutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Dbutton.isEnabled = false
        Dbutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Ebutton.isEnabled = false
        Ebutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Fbutton.isEnabled = false
        Fbutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        num1bt.isEnabled = true
        num1bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num2bt.isEnabled = true
        num2bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num3bt.isEnabled = true
        num3bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num4bt.isEnabled = true
        num4bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num5bt.isEnabled = true
        num5bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num6bt.isEnabled = true
        num6bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num7bt.isEnabled = true
        num7bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num8bt.isEnabled = false
        num8bt.setTextColor(getThemeColor(R.attr.disabledTextColor))

        num9bt.isEnabled = false
        num9bt.setTextColor(getThemeColor(R.attr.disabledTextColor))
    }

    private fun setBinaryButtons(){
        Abutton.isEnabled = false
        Abutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Bbutton.isEnabled = false
        Bbutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Cbutton.isEnabled = false
        Cbutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Dbutton.isEnabled = false
        Dbutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Ebutton.isEnabled = false
        Ebutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        Fbutton.isEnabled = false
        Fbutton.setTextColor(getThemeColor(R.attr.disabledTextColor))

        num1bt.isEnabled = true
        num1bt.setTextColor(getThemeColor(R.attr.calcTextDefaultColor))

        num2bt.isEnabled = false
        num2bt.setTextColor(getThemeColor(R.attr.disabledTextColor))

        num3bt.isEnabled = false
        num3bt.setTextColor(getThemeColor(R.attr.disabledTextColor))

        num4bt.isEnabled = false
        num4bt.setTextColor(getThemeColor(R.attr.disabledTextColor))

        num5bt.isEnabled = false
        num5bt.setTextColor(getThemeColor(R.attr.disabledTextColor))

        num6bt.isEnabled = false
        num6bt.setTextColor(getThemeColor(R.attr.disabledTextColor))

        num7bt.isEnabled = false
        num7bt.setTextColor(getThemeColor(R.attr.disabledTextColor))

        num8bt.isEnabled = false
        num8bt.setTextColor(getThemeColor(R.attr.disabledTextColor))

        num9bt.isEnabled = false
        num9bt.setTextColor(getThemeColor(R.attr.disabledTextColor))
    }

    private fun setResult(){
        if (textexpression.text!!.isNotEmpty() && textres.text.isNotEmpty()) {
            val resultText = textres.text.toString()

            textexpression.setText(resultText)
            textres.text = ""
            textexpression.setSelection(textexpression.text!!.length)

            val leftjustres = preferences.getBoolean(SharedPrefs.LEFTJUSTPGRES, true)

            if (leftjustres) {
                val maxScrollX = textexpression.layout.getLineLeft(0).toInt().coerceAtLeast(0)
                textexpression.post {
                    textexpression.scrollTo(maxScrollX, 0)
                }
            }

            mviewModel.result = resultText
        }
    }

    private fun setResultonNumSys(){
        if (textexpression.text!!.isNotEmpty()) {
            val resultText = when(mviewModel.numberSys.value){
                NumberSystem.DEC -> decRes.text.toString()
                NumberSystem.HEX -> hexRes.text.toString()
                NumberSystem.OCT -> octRes.text.toString()
                NumberSystem.BIN -> binRes.text.toString()
                else -> decRes.text.toString()
            }

            textexpression.setText(resultText)
            textres.text = ""
            textexpression.setSelection(textexpression.text!!.length)

            val leftjustres = preferences.getBoolean(SharedPrefs.LEFTJUSTPGRES, true)

            if (leftjustres) {
                val maxScrollX = textexpression.layout.getLineLeft(0).toInt().coerceAtLeast(0)
                textexpression.post {
                    textexpression.scrollTo(maxScrollX, 0)
                }
            }

            mviewModel.result = resultText
        }
    }

    private fun clearFields() {
        textexpression.setText("")
        textres.setText("")
        mviewModel.result = textres.text.toString()

        decRes.text = ""
        hexRes.text = ""
        octRes.text = ""
        binRes.text = ""

        mviewModel.decResult = ""
        mviewModel.hexResult = ""
        mviewModel.octResult = ""
        mviewModel.binResult = ""
    }

    private fun addExpression(ex: String, tostart: Boolean = false){
        if(tostart){
            val start = Math.max(textexpression.selectionStart, 0)
            val end = Math.max(textexpression.selectionEnd, 0)
            if (start == end) {
                textexpression.text?.replace(0, 0, ex, 0, ex.length)
            } else {
                textexpression.text?.replace(Math.min(start, end), Math.max(start, end), ex, 0, ex.length)
            }
        }
        else{
            val start = Math.max(textexpression.selectionStart, 0)
            val end = Math.max(textexpression.selectionEnd, 0)
            textexpression.text?.replace(Math.min(start, end), Math.max(start, end), ex, 0, ex.length)
        }
    }

    private fun evaluate_expr(){
        // Because calculations are CPU-intensive, the default dispatcher is recommended.
        mCoroutineScope.launch {
            val resDeferred = mCoroutineScope.async(Dispatchers.Default){
                mviewModel.numberSys.value?.let {
                    PGCalc.calculate(textexpression.text.toString(),
                        it
                    )
                }
            }

            val res = resDeferred.await()
            textres.text = res
            mviewModel.result = textres.text.toString()

            res?.let {
                decRes.text = convertToBase(it, mviewModel.numberSys.value?.radix!!, 10)?.uppercase() ?: "NaN"
                mviewModel.decResult = decRes.text.toString()

                hexRes.text = convertToBase(it, mviewModel.numberSys.value?.radix!!, 16)?.uppercase() ?: "NaN"
                mviewModel.hexResult = hexRes.text.toString()

                octRes.text = convertToBase(it, mviewModel.numberSys.value?.radix!!, 8)?.uppercase() ?: "NaN"
                mviewModel.octResult = octRes.text.toString()

                binRes.text = convertToBase(it, mviewModel.numberSys.value?.radix!!, 2)?.uppercase() ?: "NaN"
                mviewModel.binResult = binRes.text.toString()
            }
        }
    }

    fun convertToBase(numberString: String, fromBase: Int, toBase: Int): String? {
        return try {
            val decimalValue = numberString.toLong(fromBase)

            when (toBase) {
                2 -> decimalValue.toString(NumberSystem.BIN.radix)
                8 -> decimalValue.toString(NumberSystem.OCT.radix)
                10 -> decimalValue.toString(NumberSystem.DEC.radix)
                16 -> decimalValue.toString(NumberSystem.HEX.radix)
                else -> null
            }
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun buttonHandler(id: Int){
        normalButtonHandler(id)
        hexButtonHandler(id)
        numberButtonHandler(id)
    }

    private fun normalButtonHandler(id: Int){
        when(id){
            R.id.leftbracbutton -> {
                addExpression(getString(R.string.leftbracket))
                evaluate_expr()
            }
            R.id.rightbracbutton -> {
                addExpression(getString(R.string.rightbracket))
                evaluate_expr()
            }
            R.id.doublezerobt -> {
                addExpression(getString(R.string.doublezero_text))
                evaluate_expr()
            }
            R.id.percentbutton -> {
                addExpression(getString(R.string.percent_bttext))
                evaluate_expr()
            }
            R.id.acbutton -> {
                clearFields()
            }
            R.id.leftshiftbutton -> {
                addExpression(" " + getString(R.string.LSHstr) + " ")
                evaluate_expr()
            }
            R.id.rightshiftbutton -> {
                addExpression(" " + getString(R.string.RSHstr) + " ")
                evaluate_expr()
            }
            R.id.andbutton -> {
                addExpression(" " + getString(R.string.ANDstr) + " ")
                evaluate_expr()
            }
            R.id.orbutton -> {
                addExpression(" " + getString(R.string.ORstr) + " ")
                evaluate_expr()
            }
            R.id.notbutton -> {
                addExpression(" " + getString(R.string.NOTstr) + " ", tostart = true)
                evaluate_expr()
            }
            R.id.nandbutton -> {
                addExpression(" " + getString(R.string.NANDstr) + " ")
                evaluate_expr()
            }
            R.id.norbutton -> {
                addExpression(" " + getString(R.string.NORstr) + " ")
                evaluate_expr()
            }
            R.id.xorbutton -> {
                addExpression(" " + getString(R.string.XORstr) + " ")
                evaluate_expr()
            }
            R.id.xnorbutton -> {
                addExpression(" " + getString(R.string.XNORstr) + " ")
                evaluate_expr()
            }
            R.id.ushrbutton -> {
                addExpression(" " + getString(R.string.URSHstr) + " ")
                evaluate_expr()
            }
            R.id.rolbutton -> {
                addExpression(" " + getString(R.string.RoLstr) + " ")
                evaluate_expr()
            }
            R.id.rorbutton -> {
                addExpression(" " + getString(R.string.RoRstr) + " ")
                evaluate_expr()
            }
            R.id.divideop -> {
                addExpression(getString(R.string.divide_expr))
                evaluate_expr()
            }
            R.id.mulop -> {
                addExpression(getString(R.string.mul_expr))
                evaluate_expr()
            }
            R.id.minusop -> {
                addExpression(getString(R.string.minusop))
                evaluate_expr()
            }
            R.id.plusop -> {
                addExpression(getString(R.string.plusop))
                evaluate_expr()
            }
            R.id.equalop -> {
                setResult()
            }
            R.id.equalopsecond -> {
                setResult()
            }
            R.id.backspacebutton -> {
                val cursorPos = textexpression.selectionStart - 1
                val textexprstr = textexpression.text.toString()

                if(textexprstr.isNotEmpty()){
                    try {
                        if(textexpression.hasSelection()){
                            addExpression("")
                        }
                        else{
                            val rem1 = textexprstr.substring(0, cursorPos)
                            val rem2 = textexprstr.substring(cursorPos+1)
                            textexpression.setText(rem1 + rem2)
                            textexpression.setSelection(cursorPos)
                        }

                        if(textexpression.text.toString().isNotEmpty()){
                            evaluate_expr()
                        }
                        else{
                            textres.setText("")
                            mviewModel.result = textres.text.toString()
                        }
                    }catch (e: Exception){
                        println("LOG: ATBKSPACE ${e.message}")
                    }
                }
            }
        }
    }

    private fun hexButtonHandler(id: Int){
        when(id){
            R.id.Abutton -> {
                addExpression(getString(R.string.a))
                evaluate_expr()
            }
            R.id.Bbutton -> {
                addExpression(getString(R.string.b))
                evaluate_expr()
            }
            R.id.Cbutton -> {
                addExpression(getString(R.string.c))
                evaluate_expr()
            }
            R.id.Dbutton -> {
                addExpression(getString(R.string.d))
                evaluate_expr()
            }
            R.id.Ebutton -> {
                addExpression(getString(R.string.e))
                evaluate_expr()
            }
            R.id.Fbutton -> {
                addExpression(getString(R.string.f))
                evaluate_expr()
            }
        }
    }

    private fun numberButtonHandler(id: Int){
        when(id){
            R.id.numberseven -> {
                addExpression(getString(R.string.numberseven))
                evaluate_expr()
            }
            R.id.numbereight -> {
                addExpression(getString(R.string.numbereight))
                evaluate_expr()
            }
            R.id.numbernine -> {
                addExpression(getString(R.string.numbernine))
                evaluate_expr()
            }
            R.id.numberfour -> {
                addExpression(getString(R.string.numberfour))
                evaluate_expr()
            }
            R.id.numberfive -> {
                addExpression(getString(R.string.numberfive))
                evaluate_expr()
            }
            R.id.numbersix -> {
                addExpression(getString(R.string.numbersix))
                evaluate_expr()
            }
            R.id.numberone -> {
                addExpression(getString(R.string.numberone))
                evaluate_expr()
            }
            R.id.numbertwo -> {
                addExpression(getString(R.string.numbertwo))
                evaluate_expr()
            }
            R.id.numberthree -> {
                addExpression(getString(R.string.numberthree))
                evaluate_expr()
            }
            R.id.numberzero -> {
                addExpression(getString(R.string.zero_text))
                evaluate_expr()
            }
            R.id.numberzerosecond -> {
                addExpression(getString(R.string.zero_text))
                evaluate_expr()
            }
        }
    }
}