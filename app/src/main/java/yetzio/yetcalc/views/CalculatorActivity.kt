package yetzio.yetcalc.views

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import yetzio.yetcalc.R
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.config.CalcBaseActivity
import yetzio.yetcalc.enums.AngleMode
import yetzio.yetcalc.enums.CalcMode
import yetzio.yetcalc.models.CalcViewModel
import yetzio.yetcalc.utils.getScreenOrientation
import yetzio.yetcalc.utils.hideLayoutWithAnimationF
import yetzio.yetcalc.utils.setVibOnClick
import yetzio.yetcalc.utils.showLayoutWithAnimationF
import yetzio.yetcalc.widget.CalcText
import yetzio.yetcalc.widget.CalcTextListener

class CalculatorActivity : CalcBaseActivity(), View.OnClickListener {
    private lateinit var mviewModel: CalcViewModel
    private val mCoroutineScope = CoroutineScope(Dispatchers.Main)
    private val IOCoroutineScope = CoroutineScope(Dispatchers.IO)
    private var variable_charlist = ArrayList<String>()

    private lateinit var settingsButton: MaterialButton

    private lateinit var histLauncher: ActivityResultLauncher<Intent>
    private lateinit var settingsLauncher: ActivityResultLauncher<Intent>

    private lateinit var textexpression: CalcText
    private lateinit var textres: MaterialTextView
    private var result: String = ""

    // Buttons list
    private var button_list = ArrayList<MaterialButton>()

    // Buttons - All
    private lateinit var gradbt: MaterialButton
    private lateinit var lambertW0bt: MaterialButton
    private lateinit var histOthBt: MaterialButton

    private lateinit var anglebt: MaterialButton
    private lateinit var othconstbt: MaterialButton
    private lateinit var varBt: MaterialButton

    private lateinit var varSelector: TextInputLayout
    private lateinit var calcModeSelector: TextInputLayout

    private lateinit var angleconvbt: MaterialButton
    private lateinit var factbt: MaterialButton
    private lateinit var sinbt: MaterialButton
    private lateinit var cosbt: MaterialButton
    private lateinit var tanbt: MaterialButton
    private lateinit var ebt: MaterialButton
    private lateinit var lnbt: MaterialButton
    private lateinit var logbt: MaterialButton

    // Buttons - Normal Buttons
    private lateinit var sqrootbt: MaterialButton
    private lateinit var pibt: MaterialButton
    private lateinit var leftbracbt: MaterialButton
    private lateinit var rightbracbt: MaterialButton
    private lateinit var commabt: MaterialButton
    private lateinit var spacebt: MaterialButton
    private lateinit var acbt: MaterialButton
    private lateinit var powerbt: MaterialButton
    private lateinit var percentbt: MaterialButton
    private lateinit var dividebt: MaterialButton
    private lateinit var mulbt: MaterialButton
    private lateinit var minusbt: MaterialButton
    private lateinit var plusbt: MaterialButton
    private lateinit var equalbt: MaterialButton
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
    private lateinit var num0bt: MaterialButton
    private lateinit var pointbt: MaterialButton

    private lateinit var angleViewbt: MaterialButton

    // Drop Misc Buttons
    private lateinit var num0second: MaterialButton
    private lateinit var point0second: MaterialButton
    private lateinit var equalbtsecond: MaterialButton

    // Drop Container
    private lateinit var dropContainer: FrameLayout
    private lateinit var dropLineContainer: LinearLayout
    private lateinit var dropImgView: ImageView

    // Main Containers - Portrait
    private lateinit var droppedBtnLyt: FrameLayout
    private lateinit var mainbuttonlyt: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCalculationPrefs()

        mviewModel = ViewModelProvider(this)[CalcViewModel::class.java]
        mviewModel.hapticPref = preferences.getBoolean(SharedPrefs.HAPTICKEY, true)

        modeSelector = findViewById(R.id.modeselector)
        setupModeSelector()

        angleViewbt = findViewById(R.id.currentAngleView)
        settingsButton = findViewById(R.id.settingsButton)

        textexpression = findViewById(R.id.textexpression)
        textexpression.showSoftInputOnFocus = false

        textexpression.addListener(object: CalcTextListener{
            // evaluates expression on paste event
            override fun onUpdate() {
                evaluate_expr()
            }

            override fun onCutText() {
                if(textexpression.text?.isNotEmpty()!!){
                    evaluate_expr()
                }
                else{
                    textres.setText("")
                    result = textres.text.toString()
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
            point0second = findViewById(R.id.pointbuttonsecond)
            equalbtsecond = findViewById(R.id.equalopsecond)

            button_list.addAll(arrayListOf(num0second, point0second, equalbtsecond))
        }

        anglebt = findViewById(R.id.anglebutton)
        othconstbt = findViewById(R.id.otherconstbutton)

        gradbt = findViewById(R.id.gradbutton)
        lambertW0bt = findViewById(R.id.lambertWzerobutton)

        setupVarModeSelector()

        // Register buttons
        sqrootbt = findViewById(R.id.sqrtbutton)
        pibt = findViewById(R.id.pibutton)
        leftbracbt = findViewById(R.id.leftbracbutton)
        rightbracbt = findViewById(R.id.rightbracbutton)
        acbt = findViewById(R.id.acbutton)
        powerbt = findViewById(R.id.powerbutton)
        percentbt = findViewById(R.id.percentbutton)
        dividebt = findViewById(R.id.divideop)
        mulbt = findViewById(R.id.mulop)
        minusbt = findViewById(R.id.minusop)
        plusbt = findViewById(R.id.plusop)
        equalbt = findViewById(R.id.equalop)
        bkspacebt = findViewById(R.id.backspacebutton)

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
        pointbt = findViewById(R.id.pointbutton)
        histOthBt = findViewById(R.id.histOthBt)

        commabt = findViewById(R.id.commabutton)
        spacebt = findViewById(R.id.spacebutton)
        angleconvbt = findViewById(R.id.angleconvbutton)
        factbt = findViewById(R.id.factbutton)
        sinbt = findViewById(R.id.sinbutton)
        cosbt = findViewById(R.id.cosbutton)
        tanbt = findViewById(R.id.tanbutton)
        ebt = findViewById(R.id.eulernumbutton)
        lnbt = findViewById(R.id.naturallogbt)
        logbt = findViewById(R.id.commonlogbt)

        // Add buttons to button list
        button_list.addAll(arrayListOf(sqrootbt, pibt, leftbracbt,
            rightbracbt, acbt, powerbt,
            percentbt, dividebt, mulbt,
            minusbt, plusbt, equalbt,
            bkspacebt, num7bt, num8bt,
            num9bt, num4bt, num5bt,
            num6bt, num1bt, num2bt,
            num3bt, num0bt, pointbt,
            angleViewbt, settingsButton, commabt, spacebt,
            varBt, anglebt, gradbt,
            lambertW0bt, angleconvbt, factbt,
            sinbt, cosbt, tanbt,
            ebt, othconstbt, lnbt,
            logbt, histOthBt))

        setupCalcModeSelector()

        // Set on click listener for every button
        setOnClickListeners()

        setOnLongClickListeners()

        restoreModesAndConfiguration(savedInstanceState)

        histLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    if(data.hasExtra("expression")){
                        addExpression(data.getStringExtra("expression").toString())
                        evaluate_expr()
                    }
                    else if(data.hasExtra("result")){
                        addExpression(data.getStringExtra("result").toString())
                        evaluate_expr()
                    }
                }
            }
        }

        settingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val almInt = preferences.getBoolean(SharedPrefs.ALMINTKEY, true)
            val canInt = preferences.getBoolean(SharedPrefs.CANINTKEY, false)
            val precisionChoice = preferences.getString(SharedPrefs.PRECKEY, "Default precision")
            mviewModel.hapticPref = preferences.getBoolean(SharedPrefs.HAPTICKEY, true)

            Calc.almostInt = almInt
            Calc.canonInt = canInt
            if (precisionChoice != null) {
                Calc.precision = precisionChoice
            }
            println("Current precision: ${Calc.precision}")

            if(textexpression.text!!.isNotEmpty())
                evaluate_expr()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val setIntent = Intent(Intent.ACTION_MAIN)
                setIntent.addCategory(Intent.CATEGORY_HOME)
                setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(setIntent)
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(getString(R.string.text_res), result)

        super.onSaveInstanceState(outState)
    }

    private fun restoreModesAndConfiguration(savedInstanceState: Bundle?){
        if(savedInstanceState != null){
            textres.text = savedInstanceState.getString(getString(R.string.text_res))
            result = textres.text.toString()
        }

        if(mviewModel.angleMode == AngleMode.DEGREE){
            angleViewbt.setText(R.string.degreetext)
            setAngleButtonsFirstMode()
        }
        else if(mviewModel.angleMode == AngleMode.RADIAN){
            angleViewbt.setText(R.string.radiantext)
            setAngleButtonsSecondMode()
        }

        calcModeSetter()
    }

    private fun setAngleButtonsFirstMode(){
        anglebt.setText(AngleMode.RADIAN.str)
        angleconvbt.setText(getString(R.string.radconvtext))
    }

    private fun setAngleButtonsSecondMode(){
        anglebt.setText(AngleMode.DEGREE.str)
        angleconvbt.setText(getString(R.string.degconvtext))
    }

    private fun initCalculationPrefs(){
        val almInt = preferences.getBoolean(SharedPrefs.ALMINTKEY, true)
        val canInt = preferences.getBoolean(SharedPrefs.CANINTKEY, false)
        val precisionChoice = preferences.getString(SharedPrefs.PRECKEY, "Default precision")

        Calc.almostInt = almInt
        Calc.canonInt = canInt
        if (precisionChoice != null) {
            Calc.precision = precisionChoice
        }
        println("Current precision: ${Calc.precision}")
    }

    private fun setupVarModeSelector(){
        varBt = findViewById(R.id.varbutton)

        varSelector = findViewById(R.id.varModeDropdown)
        variable_charlist.addAll(getString(R.string.alphabets).split(",") as ArrayList<String>)
        val specialSymList = getString(R.string.specialsym).split(",") as ArrayList<String>

        variable_charlist.addAll(specialSymList)

        val varTV = (varSelector.editText as? AutoCompleteTextView)

        val varadapter = ArrayAdapter(this, R.layout.list_item_alt, variable_charlist)
        varTV?.setAdapter(varadapter)

        varTV?.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = varadapter.getItem(position)

            varBt.text = selectedItem
            mviewModel.varModePos = position
            varTV.setText(selectedItem, false)
        }

        if(mviewModel.varModePos != null){
            varBt.text = variable_charlist[mviewModel.varModePos!!]
            varTV?.setText(variable_charlist[mviewModel.varModePos!!], false)
        }
        else{
            varTV?.setText("x", false)
        }
    }

    private fun setupCalcModeSelector(){
        calcModeSelector = findViewById(R.id.calcmodeDropdown)

        val calcModeAdapter = ArrayAdapter.createFromResource(this,
            R.array.calcmode_array,
            R.layout.list_item_alt
        )

        val calcModeTV = (calcModeSelector.editText as? AutoCompleteTextView)

        calcModeTV?.setAdapter(calcModeAdapter)

        calcModeTV?.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = calcModeAdapter.getItem(position)

            calcModeTV.setText(selectedItem, false)
            mviewModel.calcModePos = position
            mviewModel.calcMode = CalcMode.getMode(selectedItem.toString())
            calcModeSetter()
        }

        if(mviewModel.calcModePos != null){
            calcModeTV?.setText(resources.getStringArray(R.array.calcmode_array)[mviewModel.calcModePos!!], false)
            calcModeSetter()
        }
        else{
            calcModeTV?.setText(resources.getStringArray(R.array.calcmode_array)[0], false)
            calcModeSetter()
        }
    }

    private fun calcModeSetter(){
        if(mviewModel.calcMode == CalcMode.FIRSTMODE){
            mviewModel.lastSetFactBt = mviewModel.calcMode
            setCalcButtonsFirstMode()
        }
        else if(mviewModel.calcMode == CalcMode.SECONDMODE){
            mviewModel.lastSetFactBt = mviewModel.calcMode
            setCalcButtonsSecondMode()
        }
        else if(mviewModel.calcMode == CalcMode.THIRDMODE){
            mviewModel.lastSetFactBt = mviewModel.calcMode
            setCalcButtonsThirdMode()
        }
        else if(mviewModel.calcMode == CalcMode.FOURTHMODE){
            mviewModel.lastSetFactBt = mviewModel.calcMode
            setCalcButtonsFourthMode()
        }
        else if(mviewModel.calcMode == CalcMode.FIFTHMODE){
            mviewModel.lastSetFactBt = mviewModel.calcMode
            setCalcButtonsFifthMode()
        }
        else if(mviewModel.calcMode == CalcMode.SIXTHMODE){
            setCalcButtonsSixthMode()
        }
        else if(mviewModel.calcMode == CalcMode.SEVENTHMODE){
            setCalcButtonsSeventhMode()
        }
        else if(mviewModel.calcMode == CalcMode.EIGHTMODE){
            setCalcButtonsEigthMode()
        }
        else if(mviewModel.calcMode == CalcMode.NINTHMODE){
            setCalcButtonsNinthMode()
        }

        if(textexpression.text!!.isNotEmpty())
            evaluate_expr()
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

    private fun clearFields() {
        textexpression.setText("")
        textres.setText("")
        result = textres.text.toString()
    }

    private fun evaluate_expr(){
        // Because calculations are CPU-intensive, the default dispatcher is recommended.
        mCoroutineScope.launch {
            val resDeferred = mCoroutineScope.async(Dispatchers.Default){
                Calc.calculate(textexpression.text.toString())
            }

            val res = resDeferred.await()
            textres.text = res
            result = textres.text.toString()
        }
    }

    override fun onClick(view: View?) {
        val buttonId = view?.id

        if(buttonId!= null){
            if(getScreenOrientation(this) == Configuration.ORIENTATION_PORTRAIT){
                portraitButtonHandler(buttonId)
            }
            else{
                normalButtonHandler(buttonId)
            }
        }

        setVibOnClick(applicationContext)
    }

    private fun setCalcButtonsFirstMode(){
        sqrootbt.setText(getString(R.string.sqroot))
        othconstbt.setText(getString(R.string.goldenratio))
        factbt.setText(getString(R.string.factorialtext))
        sinbt.setText(getString(R.string.sintext))
        cosbt.setText(getString(R.string.costext))
        tanbt.setText(getString(R.string.tantext))
        ebt.setText(getString(R.string.eulersnumber))
        lnbt.setText(getString(R.string.naturallogtext))
        logbt.setText(getString(R.string.commonlogtext))
    }

    private fun setCalcButtonsSecondMode(){
        sqrootbt.setText(getString(R.string.squaretext))
        othconstbt.setText(getString(R.string.eulermascheroni))
        factbt.setText(getString(R.string.modulotext))
        sinbt.setText(getString(R.string.cosectext))
        cosbt.setText(getString(R.string.sectext))
        tanbt.setText(getString(R.string.cottext))
        ebt.setText(getString(R.string.eulersnumber))
        lnbt.setText(getString(R.string.eraisedtext))
        logbt.setText(getString(R.string.tenraisedtext))
    }

    private fun setCalcButtonsThirdMode(){
        sqrootbt.setText(getString(R.string.modulustext))
        othconstbt.setText(getString(R.string.parabolicconst))
        factbt.setText(getString(R.string.cuberoottext))
        sinbt.setText(getString(R.string.invsintext))
        cosbt.setText(getString(R.string.invcostext))
        tanbt.setText(getString(R.string.invtantext))
        ebt.setText(getString(R.string.invcosectext))
        lnbt.setText(getString(R.string.invsectext))
        logbt.setText(getString(R.string.invcottext))
    }

    private fun setCalcButtonsFourthMode(){
        sqrootbt.setText(getString(R.string.signum_text))
        othconstbt.setText(getString(R.string.speedoflight))
        factbt.setText(getString(R.string.fourthroottext))
        sinbt.setText(getString(R.string.sinhtext))
        cosbt.setText(getString(R.string.coshtext))
        tanbt.setText(getString(R.string.tanhtext))
        ebt.setText(getString(R.string.cschtext))
        lnbt.setText(getString(R.string.sechtext))
        logbt.setText(getString(R.string.cothtext))
    }

    private fun setCalcButtonsFifthMode(){
        sqrootbt.setText(getString(R.string.sinc_text))
        othconstbt.setText(getString(R.string.plancksconst))
        factbt.setText(getString(R.string.powertower))
        sinbt.setText(getString(R.string.invsinhtext))
        cosbt.setText(getString(R.string.invcoshtext))
        tanbt.setText(getString(R.string.invtanhtext))
        ebt.setText(getString(R.string.invcosechtext))
        lnbt.setText(getString(R.string.invsechtext))
        logbt.setText(getString(R.string.invcothtext))
    }

    private fun setCalcButtonsSixthMode(){
        sqrootbt.setText(getString(R.string.npr_text))
        othconstbt.setText(getString(R.string.boltzmannconst))
        sinbt.setText(getString(R.string.integraltext))
        cosbt.setText(getString(R.string.derivativetext))
        tanbt.setText(getString(R.string.leftdertext))
        ebt.setText(getString(R.string.nthdertext))
        lnbt.setText(getString(R.string.fwd_difftext))
        logbt.setText(getString(R.string.rightdertext))
    }

    private fun setCalcButtonsSeventhMode(){
        sqrootbt.setText(getString(R.string.ncr_text))
        othconstbt.setText(getString(R.string.viswanathconst))
        sinbt.setText(getString(R.string.backdifftext))
        cosbt.setText(getString(R.string.eqsolvtext))
        tanbt.setText(getString(R.string.stirlingnumonetext))
        ebt.setText(getString(R.string.berntext))
        lnbt.setText(getString(R.string.worptext))
        logbt.setText(getString(R.string.strlingnumtwotext))
    }

    private fun setCalcButtonsEigthMode(){
        sqrootbt.setText(getString(R.string.kdelta_text))
        othconstbt.setText(getString(R.string.plasticconst))
        sinbt.setText(getString(R.string.sigmatext))
        cosbt.setText(getString(R.string.prodtext))
        tanbt.setText(getString(R.string.avgtext))
        ebt.setText(getString(R.string.varitext))
        lnbt.setText(getString(R.string.stditext))
        logbt.setText(getString(R.string.meantext))
    }

    private fun setCalcButtonsNinthMode(){
        sqrootbt.setText(getString(R.string.gamma))
        othconstbt.setText(getString(R.string.sgngamma))
        sinbt.setText(getString(R.string.loggamma))
        cosbt.setText(getString(R.string.erf))
        tanbt.setText(getString(R.string.erfc))
        ebt.setText(getString(R.string.digamma))
        lnbt.setText(getString(R.string.erfInv))
        logbt.setText(getString(R.string.erfcInv))
    }

    private fun angleModeSetter(){
        if(mviewModel.angleMode == AngleMode.DEGREE){
            mviewModel.angleMode= AngleMode.RADIAN
            Calc.angleMode = mviewModel.angleMode
            angleViewbt.setText(mviewModel.angleMode.str)

            setAngleButtonsSecondMode()
        }
        else{
            mviewModel.angleMode = AngleMode.DEGREE
            Calc.angleMode = mviewModel.angleMode
            angleViewbt.setText(mviewModel.angleMode.str)

            setAngleButtonsFirstMode()
        }

        if(textexpression.text!!.isNotEmpty())
            evaluate_expr()
    }

    private fun addExpression(ex: String){
        val start = Math.max(textexpression.selectionStart, 0)
        val end = Math.max(textexpression.selectionEnd, 0)
        textexpression.text?.replace(Math.min(start, end), Math.max(start, end), ex, 0, ex.length)
    }

    private fun setResult(){
        if (textexpression.text!!.isNotEmpty() && textres.text.isNotEmpty()) {
            val expression = textexpression.text.toString()
            val resultText = textres.text.toString()

            IOCoroutineScope.launch {
                Calc.addToHistory(expression, resultText)
            }

            textexpression.setText(resultText)
            textres.text = ""
            textexpression.setSelection(textexpression.text!!.length)

            val leftjustres = preferences.getBoolean(SharedPrefs.LEFTJUSTRES, true)

            if (leftjustres) {
                val maxScrollX = textexpression.layout.getLineLeft(0).toInt().coerceAtLeast(0)
                textexpression.post {
                    textexpression.scrollTo(maxScrollX, 0)
                }
            }

            result = resultText
        }
    }

    private fun portraitButtonHandler(id: Int){
        when(id){
            R.id.numberzerosecond -> {
                addExpression(getString(R.string.zero_text))
                evaluate_expr()
            }
            R.id.pointbuttonsecond -> {
                addExpression(getString(R.string.point_text))
                evaluate_expr()
            }
            R.id.equalopsecond -> {
                setResult()
            }
            else -> {
                normalButtonHandler(id)
            }
        }
    }

    private fun normalButtonHandler(id: Int){
        handleNormalButtons(id)
        handleGeneralButtons(id)
        handleNumberButtons(id)
    }

    private fun handleNormalButtons(id: Int){
        when(id){
            R.id.anglebutton -> {
                angleModeSetter()
            }
            R.id.commabutton -> {
                addExpression(getString(R.string.commachar))
                evaluate_expr()
            }
            R.id.spacebutton -> {
                addExpression(getString(R.string.spacechar))
                evaluate_expr()
            }
            R.id.varbutton -> {
                addExpression(varBt.text.toString())
                evaluate_expr()
            }
            R.id.sqrtbutton -> {
                if(mviewModel.calcMode == CalcMode.FIRSTMODE){
                    addExpression(getString(R.string.sqrootval))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SECONDMODE){
                    addExpression(getString(R.string.square_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.THIRDMODE){
                    addExpression(getString(R.string.abs_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FOURTHMODE){
                    addExpression(getString(R.string.signum_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FIFTHMODE){
                    addExpression(getString(R.string.sinc_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SIXTHMODE){
                    addExpression(getString(R.string.npr_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SEVENTHMODE){
                    addExpression(getString(R.string.ncr_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.EIGHTMODE){
                    addExpression(getString(R.string.kdelta_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.NINTHMODE){
                    addExpression(getString(R.string.gamma_func))
                    evaluate_expr()
                }
            }
            R.id.angleconvbutton -> {
                if(mviewModel.angleMode == AngleMode.DEGREE){
                    addExpression(getString(R.string.rad_func))
                    evaluate_expr()
                }
                else{
                    addExpression(getString(R.string.deg_func))
                    evaluate_expr()
                }
            }
            R.id.sinbutton -> {
                if(mviewModel.calcMode == CalcMode.FIRSTMODE){
                    addExpression(getString(R.string.sine_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SECONDMODE){
                    addExpression(getString(R.string.cosec_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.THIRDMODE){
                    addExpression(getString(R.string.invsin_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FOURTHMODE){
                    addExpression(getString(R.string.sinh_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FIFTHMODE){
                    addExpression(getString(R.string.invsinh_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SIXTHMODE){
                    addExpression(getString(R.string.integralfunc))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SEVENTHMODE){
                    addExpression(getString(R.string.backward_difffunc))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.EIGHTMODE){
                    addExpression(getString(R.string.sigma_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.NINTHMODE){
                    addExpression(getString(R.string.loggamma_func))
                    evaluate_expr()
                }
            }
            R.id.cosbutton -> {
                if(mviewModel.calcMode == CalcMode.FIRSTMODE){
                    addExpression(getString(R.string.cosine_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SECONDMODE){
                    addExpression(getString(R.string.sec_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.THIRDMODE){
                    addExpression(getString(R.string.invcos_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FOURTHMODE){
                    addExpression(getString(R.string.cosh_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FIFTHMODE){
                    addExpression(getString(R.string.invcosh_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SIXTHMODE){
                    addExpression(getString(R.string.derfunc))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SEVENTHMODE){
                    addExpression(getString(R.string.eqsolvefunc))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.EIGHTMODE){
                    addExpression(getString(R.string.prod_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.NINTHMODE){
                    addExpression(getString(R.string.erf_func))
                    evaluate_expr()
                }
            }
            R.id.tanbutton -> {
                if(mviewModel.calcMode == CalcMode.FIRSTMODE){
                    addExpression(getString(R.string.tan_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SECONDMODE){
                    addExpression(getString(R.string.cot_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.THIRDMODE){
                    addExpression(getString(R.string.invtan_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FOURTHMODE){
                    addExpression(getString(R.string.tanh_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FIFTHMODE){
                    addExpression(getString(R.string.invtanh_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SIXTHMODE){
                    addExpression(getString(R.string.leftderfunc))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SEVENTHMODE){
                    addExpression(getString(R.string.stirlingnumfunc))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.EIGHTMODE){
                    addExpression(getString(R.string.avg_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.NINTHMODE){
                    addExpression(getString(R.string.erfc_func))
                    evaluate_expr()
                }
            }
            R.id.eulernumbutton -> {
                if(mviewModel.calcMode == CalcMode.FIRSTMODE || mviewModel.calcMode == CalcMode.SECONDMODE){
                    addExpression(getString(R.string.eulersnumber))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.THIRDMODE){
                    addExpression(getString(R.string.invcsc_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FOURTHMODE){
                    addExpression(getString(R.string.csch_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FIFTHMODE){
                    addExpression(getString(R.string.invcsch_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SIXTHMODE){
                    addExpression(getString(R.string.nderfunc))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SEVENTHMODE){
                    addExpression(getString(R.string.bernfunc))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.EIGHTMODE){
                    addExpression(getString(R.string.vari_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.NINTHMODE){
                    addExpression(getString(R.string.digamma_func))
                    evaluate_expr()
                }
            }
            R.id.naturallogbt -> {
                if(mviewModel.calcMode == CalcMode.FIRSTMODE){
                    addExpression(getString(R.string.naturallog_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SECONDMODE){
                    addExpression(getString(R.string.eraised_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.THIRDMODE){
                    addExpression(getString(R.string.invsec_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FOURTHMODE){
                    addExpression(getString(R.string.sech_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FIFTHMODE){
                    addExpression(getString(R.string.invsech_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SIXTHMODE){
                    addExpression(getString(R.string.fwd_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SEVENTHMODE){
                    addExpression(getString(R.string.worpfunc))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.EIGHTMODE){
                    addExpression(getString(R.string.stdi_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.NINTHMODE){
                    addExpression(getString(R.string.erfInv_func))
                    evaluate_expr()
                }
            }
            R.id.commonlogbt -> {
                if(mviewModel.calcMode == CalcMode.FIRSTMODE){
                    addExpression(getString(R.string.commonlog_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SECONDMODE){
                    addExpression(getString(R.string.tenraised_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.THIRDMODE){
                    addExpression(getString(R.string.invcot_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FOURTHMODE){
                    addExpression(getString(R.string.coth_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FIFTHMODE){
                    addExpression(getString(R.string.invcoth_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SIXTHMODE){
                    addExpression(getString(R.string.rightderfunc))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SEVENTHMODE){
                    addExpression(getString(R.string.stirlingnum2func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.EIGHTMODE){
                    addExpression(getString(R.string.mean_func))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.NINTHMODE){
                    addExpression(getString(R.string.erfcInv_func))
                    evaluate_expr()
                }
            }
            R.id.otherconstbutton -> {
                if(mviewModel.calcMode == CalcMode.FIRSTMODE){
                    addExpression(getString(R.string.goldenratio_text))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SECONDMODE){
                    addExpression(getString(R.string.eulermascheroni_text))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.THIRDMODE){
                    addExpression(getString(R.string.parabolicconstant))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FOURTHMODE){
                    addExpression(getString(R.string.lightspeed))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FIFTHMODE){
                    addExpression(getString(R.string.plancksconstant))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SIXTHMODE){
                    addExpression(getString(R.string.boltzmannconstnum))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SEVENTHMODE){
                    addExpression(getString(R.string.viswanathconstant))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.EIGHTMODE){
                    addExpression(getString(R.string.plasticconstant))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.NINTHMODE){
                    addExpression(getString(R.string.sgngamma_func))
                    evaluate_expr()
                }
            }
            R.id.factbutton -> {
                if(mviewModel.calcMode == CalcMode.FIRSTMODE){
                    addExpression(getString(R.string.factorialtext))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.SECONDMODE){
                    addExpression(getString(R.string.moduloop))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.THIRDMODE){
                    addExpression(getString(R.string.cuberootval))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FOURTHMODE){
                    addExpression(getString(R.string.fourthrootval))
                    evaluate_expr()
                }
                else if(mviewModel.calcMode == CalcMode.FIFTHMODE){
                    addExpression(getString(R.string.powertower))
                    evaluate_expr()
                }
                else{
                    when (mviewModel.lastSetFactBt) {
                        CalcMode.FIRSTMODE -> {
                            addExpression(getString(R.string.factorialtext))
                            evaluate_expr()
                        }
                        CalcMode.SECONDMODE -> {
                            addExpression(getString(R.string.moduloop))
                            evaluate_expr()
                        }
                        CalcMode.THIRDMODE -> {
                            addExpression(getString(R.string.cuberootval))
                            evaluate_expr()
                        }
                        CalcMode.FOURTHMODE -> {
                            addExpression(getString(R.string.fourthrootval))
                            evaluate_expr()
                        }
                        CalcMode.FIFTHMODE -> {
                            addExpression(getString(R.string.powertower))
                            evaluate_expr()
                        }
                        else -> {
                            addExpression(getString(R.string.factorialtext))
                            evaluate_expr()
                        }
                    }
                }
            }
            R.id.gradbutton -> {
                addExpression(getString(R.string.grad_func))
                evaluate_expr()
            }
            R.id.lambertWzerobutton -> {
                addExpression(getString(R.string.lambwzero_func))
                evaluate_expr()
            }
        }
    }

    fun handleGeneralButtons(id: Int){
        when(id){
            R.id.histOthBt -> {
                val hisintent = Intent(this, HistoryActivity::class.java)
                histLauncher.launch(hisintent)
            }
            R.id.currentAngleView -> {
                angleModeSetter()
            }
            R.id.settingsButton -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                settingsLauncher.launch(settingsIntent)
            }
            R.id.leftbracbutton -> {
                addExpression(getString(R.string.leftbracket))
                evaluate_expr()
            }
            R.id.rightbracbutton -> {
                addExpression(getString(R.string.rightbracket))
                evaluate_expr()
            }
            R.id.pibutton -> {
                addExpression(getString(R.string.pi_text))
                evaluate_expr()
            }
            R.id.acbutton -> {
                clearFields()
            }
            R.id.powerbutton -> {
                addExpression(getString(R.string.power_text))
                evaluate_expr()
            }
            R.id.percentbutton -> {
                addExpression(getString(R.string.percent_bttext))
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
            R.id.pointbutton -> {
                addExpression(getString(R.string.point_text))
                evaluate_expr()
            }
            R.id.equalop -> {
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
                            result = textres.text.toString()
                        }
                    }catch (e: Exception){
                        println("LOG: ATBKSPACE ${e.message}")
                    }
                }
            }
        }
    }

    private fun handleNumberButtons(id: Int){
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
        }
    }
}