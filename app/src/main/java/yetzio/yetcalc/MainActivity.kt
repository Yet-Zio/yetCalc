package yetzio.yetcalc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.airbnb.paris.Paris
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import yetzio.yetcalc.component.AngleMode
import yetzio.yetcalc.component.CalcMode
import yetzio.yetcalc.component.Calculator
import yetzio.yetcalc.component.SpinnerItemAdapter
import yetzio.yetcalc.model.CalcViewModel
import yetzio.yetcalc.utils.getModesList
import yetzio.yetcalc.utils.getScreenOrientation
import yetzio.yetcalc.utils.setVibOnClick
import yetzio.yetcalc.utils.showThemeDialog
import yetzio.yetcalc.views.HistoryActivity
import yetzio.yetcalc.views.ProgramCalcActivity
import yetzio.yetcalc.views.SettingsActivity
import yetzio.yetcalc.views.UnitConvActivity
import yetzio.yetcalc.widget.CalcText
import yetzio.yetcalc.widget.CalcTextListener

val Calc = Calculator()

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener{
    private lateinit var mviewModel: CalcViewModel
    private val mCoroutineScope = CoroutineScope(Dispatchers.Main)
    private val IOCoroutineScope = CoroutineScope(Dispatchers.IO)
    private var variable_charlist = ArrayList<String>()

    private lateinit var histLauncher: ActivityResultLauncher<Intent>
    private lateinit var settingsLauncher: ActivityResultLauncher<Intent>

    private lateinit var textexpression: CalcText
    private lateinit var textres: TextView
    private var result: String = ""
    private lateinit var toolbar: Toolbar

    // Button list
    private var button_list = ArrayList<Button>()

    // Buttons - All
    private lateinit var gradbt: Button
    private lateinit var lambertW0bt: Button
    private lateinit var histOthBt: ImageButton

    private lateinit var anglebt: Button
    private lateinit var othconstbt: Button
    private lateinit var varBt: Button
    private lateinit var varSpinner: Spinner
    private lateinit var modeSpinner: Spinner
    private lateinit var angleconvbt: Button
    private lateinit var factbt: Button
    private lateinit var sinbt: Button
    private lateinit var cosbt: Button
    private lateinit var tanbt: Button
    private lateinit var ebt: Button
    private lateinit var lnbt: Button
    private lateinit var logbt: Button
    // Buttons - Portrait
    private lateinit var sqrootbt: Button
    private lateinit var pibt: Button
    private lateinit var leftbracbt: Button
    private lateinit var rightbracbt: Button
    private lateinit var commabt: Button
    private lateinit var spacebt: Button
    private lateinit var acbt: Button
    private lateinit var powerbt: Button
    private lateinit var percentbt: Button
    private lateinit var dividebt: Button
    private lateinit var mulbt: Button
    private lateinit var minusbt: Button
    private lateinit var plusbt: Button
    private lateinit var equalbt: Button
    private lateinit var bkspacebt: Button
    private lateinit var num7bt: Button
    private lateinit var num8bt: Button
    private lateinit var num9bt: Button
    private lateinit var num4bt: Button
    private lateinit var num5bt: Button
    private lateinit var num6bt: Button
    private lateinit var num1bt: Button
    private lateinit var num2bt: Button
    private lateinit var num3bt: Button
    private lateinit var num0bt: Button
    private lateinit var pointbt: Button

    private lateinit var angleViewbt: Button
    private lateinit var modeselecSpin: Spinner

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var calculationPref: SharedPreferences

    private lateinit var theme: String
    private var dark = false
    private var light = false

    override fun onCreate(savedInstanceState: Bundle?){
        initPrefs()
        initCalculationPrefs()
        theme = preferences.getString(getString(R.string.key_theme), getString(R.string.system_theme)).toString()

        if(theme == getString(R.string.system_theme)){
            val nightModeFlags: Int = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    dark = true
                    light = false
                    setTheme(R.style.yetCalcActivityThemeDark)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    dark = false
                    light = true
                    setTheme(R.style.yetCalcActivityThemeLight)
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    dark = true
                    light = false
                    setTheme(R.style.yetCalcActivityThemeDark)
                }
            }
        }
        else if(theme == getString(R.string.dark_theme)){
            dark = true
            light = false
            setTheme(R.style.yetCalcActivityThemeDark)
        }
        else{
            dark = false
            light = true
            setTheme(R.style.yetCalcActivityThemeLight)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mviewModel = ViewModelProvider(this)[CalcViewModel::class.java]
        if(getScreenOrientation(applicationContext) == Configuration.ORIENTATION_PORTRAIT){
            if(mviewModel.spinnerPos > 0){
                mviewModel.initSpinner = false
            }
        }
        mviewModel.varinitSpinner = false

        if(!mviewModel.historyinit){
            Calc.m_history.ctx = applicationContext
            mviewModel.historyinit = true
        }

        toolbar = findViewById(R.id.app_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        angleViewbt = findViewById(R.id.currentAngleView)

        textexpression = findViewById(R.id.textexpression)
        textexpression.showSoftInputOnFocus = false

        textexpression.addListener(object : CalcTextListener {
            // evaluates expression on paste event
            override fun onUpdate() {
                evaluate_expr()
            }

            override fun onCutText() {
                if(!textexpression.text.isEmpty()){
                    evaluate_expr()
                }
                else{
                    textres.setText("")
                    result = textres.text.toString()
                }
            }
        })

        textres = findViewById(R.id.textresult)

        modeselecSpin = findViewById(R.id.modeselector)
        val spinnerModesList = if(dark){
            getModesList("")
        }
        else{
            getModesList("light")
        }

        val modeAdp: SpinnerItemAdapter = if(dark){
            SpinnerItemAdapter(this, spinnerModesList, "default")
        } else{
            SpinnerItemAdapter(this, spinnerModesList, "light")
        }

        modeselecSpin.adapter = modeAdp
        modeselecSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if(pos == 1){
                    startActivity(Intent(applicationContext, UnitConvActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                }
                else if(pos == 2){
                    startActivity(Intent(applicationContext, ProgramCalcActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        if(getScreenOrientation(this) == Configuration.ORIENTATION_LANDSCAPE){
            anglebt = findViewById(R.id.anglebutton)
            othconstbt = findViewById(R.id.otherconstbutton)

            gradbt = findViewById(R.id.gradbutton)
            lambertW0bt = findViewById(R.id.lambertWzerobutton)

            varBt = findViewById(R.id.varbutton)

            varSpinner = findViewById(R.id.variable_spinner)
            variable_charlist.add(getString(R.string.drop_downarr))
            variable_charlist.addAll(getString(R.string.alphabets).split(",") as ArrayList<String>)
            val specialSpSymList = getString(R.string.specialsym).split(",") as ArrayList<String>

            variable_charlist.addAll(specialSpSymList)
            val varadapter = if(light){
                ArrayAdapter(this, R.layout.spinner_itemlight, variable_charlist)
            }
            else{
                ArrayAdapter(this, R.layout.spinner_item, variable_charlist)
            }
            varSpinner.adapter = varadapter
            varSpinner.tag = "def"
            varSpinner.onItemSelectedListener = this

            modeSpinner = findViewById(R.id.calcmode_spinner)
            if(light){
                ArrayAdapter.createFromResource(this, R.array.calcmode_array
                    , R.layout.spinner_itemlight).also {adapter ->
                    modeSpinner.adapter = adapter
                    modeSpinner.onItemSelectedListener = this
                }
            }
            else{
                ArrayAdapter.createFromResource(this, R.array.calcmode_array
                    , R.layout.spinner_item).also {adapter ->
                    modeSpinner.adapter = adapter
                    modeSpinner.onItemSelectedListener = this
                }
            }

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

            button_list.add(commabt)
            button_list.add(spacebt)
            button_list.add(varBt)
            button_list.add(anglebt)
            button_list.add(gradbt)
            button_list.add(lambertW0bt)
            button_list.add(angleconvbt)
            button_list.add(factbt)
            button_list.add(sinbt)
            button_list.add(cosbt)
            button_list.add(tanbt)
            button_list.add(ebt)
            button_list.add(othconstbt)
            button_list.add(lnbt)
            button_list.add(logbt)
        }

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

        // Set theme
        if(light){
            Paris.style(modeselecSpin).apply(R.style.AppModeSpinnerStyleLight)
            Paris.style(angleViewbt).apply(R.style.AngleViewStyleLight)

            if(getScreenOrientation(applicationContext) == Configuration.ORIENTATION_LANDSCAPE){
                Paris.style(findViewById<RelativeLayout>(R.id.spinnerborderlyt)).apply(R.style.yetSpinnerBorderStyleLight)
                Paris.style(findViewById<ImageView>(R.id.spinnerdropdownsrc)).apply(R.style.yetSpinnerImgSrcStyleLight)

                Paris.style(modeSpinner).apply(R.style.yetSpinnerStyleLight)
                Paris.style(varSpinner).apply(R.style.yetSpinnerStyleLight)

                Paris.style(textres).apply(R.style.yetCalcTVDarkResLDLight)
                Paris.style(textexpression).apply(R.style.yetCalcTVDarkLDLight)

                Paris.style(gradbt).apply(R.style.yetCalcBorderlessButtonsTopLeftLDLight)
                Paris.style(lambertW0bt).apply(R.style.yetCalcBorderlessButtonsTopLeftLDLight)

                Paris.style(spacebt).apply(R.style.yetCalcBorderlessButtonsTopRightLDLight)
                Paris.style(commabt).apply(R.style.yetCalcBorderlessButtonsTopRightLDLight)
                Paris.style(leftbracbt).apply(R.style.yetCalcBorderlessButtonsTopRightLDLight)
                Paris.style(rightbracbt).apply(R.style.yetCalcBorderlessButtonsTopRightLDLight)
                Paris.style(varBt).apply(R.style.yetCalcBorderlessButtonsTopRightLDLightItalic)

                Paris.style(anglebt).apply(R.style.yetCalcBorderlessButtonsLDLight)
                Paris.style(sqrootbt).apply(R.style.yetCalcBorderlessButtonsLDLight)
                Paris.style(histOthBt).apply(R.style.yetCalcBorderlessButtonsHistLDLight)
                Paris.style(pibt).apply(R.style.yetCalcBorderlessButtonsLDLight)

                Paris.style(othconstbt).apply(R.style.yetCalcBorderlessButtonsLDLight)
                Paris.style(angleconvbt).apply(R.style.yetCalcBorderlessButtonsLDLight)
                Paris.style(factbt).apply(R.style.yetCalcBorderlessButtonsLDLight)

                Paris.style(sinbt).apply(R.style.yetCalcBorderlessButtonsLDLight)
                Paris.style(cosbt).apply(R.style.yetCalcBorderlessButtonsLDLight)
                Paris.style(tanbt).apply(R.style.yetCalcBorderlessButtonsLDLight)

                Paris.style(ebt).apply(R.style.yetCalcBorderlessButtonsLDLight)
                Paris.style(lnbt).apply(R.style.yetCalcBorderlessButtonsLDLight)
                Paris.style(logbt).apply(R.style.yetCalcBorderlessButtonsLDLight)

                Paris.style(num0bt).apply(R.style.yetCalcNumberButtonLDLight)
                Paris.style(num1bt).apply(R.style.yetCalcNumberButtonLDLight)
                Paris.style(num2bt).apply(R.style.yetCalcNumberButtonLDLight)
                Paris.style(num3bt).apply(R.style.yetCalcNumberButtonLDLight)
                Paris.style(num4bt).apply(R.style.yetCalcNumberButtonLDLight)
                Paris.style(num5bt).apply(R.style.yetCalcNumberButtonLDLight)
                Paris.style(num6bt).apply(R.style.yetCalcNumberButtonLDLight)
                Paris.style(num7bt).apply(R.style.yetCalcNumberButtonLDLight)
                Paris.style(num8bt).apply(R.style.yetCalcNumberButtonLDLight)
                Paris.style(num9bt).apply(R.style.yetCalcNumberButtonLDLight)
                Paris.style(pointbt).apply(R.style.yetCalcNumberButtonLDLight)
                Paris.style(bkspacebt).apply(R.style.yetCalcNumberButtonLDLight)

                Paris.style(acbt).apply(R.style.yetCalcACButtonLDLight)

                Paris.style(dividebt).apply(R.style.yetCalcOPButtonLDLight)
                Paris.style(mulbt).apply(R.style.yetCalcOPButtonLDLight)
                Paris.style(powerbt).apply(R.style.yetCalcOPButtonLDLight)
                Paris.style(minusbt).apply(R.style.yetCalcOPButtonLDLight)
                Paris.style(percentbt).apply(R.style.yetCalcOPButtonLDLight)
                Paris.style(plusbt).apply(R.style.yetCalcOPButtonLDLight)

                Paris.style(equalbt).apply(R.style.yetCalcEqualButtonLDLight)
            }
            else{
                Paris.style(sqrootbt).apply(R.style.yetCalcBorderlessButtonsLight)
                Paris.style(histOthBt).apply(R.style.yetCalcBorderlessButtonsHistLight)
                Paris.style(pibt).apply(R.style.yetCalcBorderlessButtonsLight)
                Paris.style(leftbracbt).apply(R.style.yetCalcBorderlessButtonsLight)
                Paris.style(rightbracbt).apply(R.style.yetCalcBorderlessButtonsLight)

                Paris.style(textres).apply(R.style.yetCalcTVDarkResLight)
                Paris.style(textexpression).apply(R.style.yetCalcTVDarkLight)

                Paris.style(acbt).apply(R.style.yetCalcACButtonLight)

                Paris.style(powerbt).apply(R.style.yetCalcOPButtonLight)
                Paris.style(percentbt).apply(R.style.yetCalcOPButtonLight)
                Paris.style(dividebt).apply(R.style.yetCalcOPButtonLight)
                Paris.style(mulbt).apply(R.style.yetCalcOPButtonLight)
                Paris.style(minusbt).apply(R.style.yetCalcOPButtonLight)
                Paris.style(plusbt).apply(R.style.yetCalcOPButtonLight)

                Paris.style(equalbt).apply(R.style.yetCalcEqualButtonLight)

                Paris.style(num0bt).apply(R.style.yetCalcNumberButtonLight)
                Paris.style(num1bt).apply(R.style.yetCalcNumberButtonLight)
                Paris.style(num2bt).apply(R.style.yetCalcNumberButtonLight)
                Paris.style(num3bt).apply(R.style.yetCalcNumberButtonLight)
                Paris.style(num4bt).apply(R.style.yetCalcNumberButtonLight)
                Paris.style(num5bt).apply(R.style.yetCalcNumberButtonLight)
                Paris.style(num6bt).apply(R.style.yetCalcNumberButtonLight)
                Paris.style(num7bt).apply(R.style.yetCalcNumberButtonLight)
                Paris.style(num8bt).apply(R.style.yetCalcNumberButtonLight)
                Paris.style(num9bt).apply(R.style.yetCalcNumberButtonLight)

                Paris.style(pointbt).apply(R.style.yetCalcNumberButtonLight)
                Paris.style(bkspacebt).apply(R.style.yetCalcNumberButtonLight)
            }
        }
        else if(dark){
            Paris.style(histOthBt).apply(R.style.yetCalcBorderlessButtonsHist)
        }
        else{
            // for future themes
        }

        // Add buttons to button list

        button_list.add(sqrootbt)
        button_list.add(pibt)
        button_list.add(leftbracbt)
        button_list.add(rightbracbt)
        button_list.add(acbt)
        button_list.add(powerbt)
        button_list.add(percentbt)
        button_list.add(dividebt)
        button_list.add(mulbt)
        button_list.add(minusbt)
        button_list.add(plusbt)
        button_list.add(equalbt)
        button_list.add(bkspacebt)

        button_list.add(num7bt)
        button_list.add(num8bt)
        button_list.add(num9bt)
        button_list.add(num4bt)
        button_list.add(num5bt)
        button_list.add(num6bt)
        button_list.add(num1bt)
        button_list.add(num2bt)
        button_list.add(num3bt)
        button_list.add(num0bt)
        button_list.add(pointbt)
        button_list.add(angleViewbt)

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
            val almInt = calculationPref.getBoolean("almostintkey", true)
            val canInt = calculationPref.getBoolean("canonintkey", false)
            val precisionChoice = calculationPref.getString("precisionkey", "Default precision")

            Calc.almostInt = almInt
            Calc.canonInt = canInt
            if (precisionChoice != null) {
                Calc.precision = precisionChoice
            }
            println("Current precision: ${Calc.precision}")

            if(textexpression.text.isNotEmpty())
                evaluate_expr()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("CDA", "onBackPressed Called")
                val setIntent = Intent(Intent.ACTION_MAIN)
                setIntent.addCategory(Intent.CATEGORY_HOME)
                setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(setIntent)
            }
        })

    }

    private fun setOnLongClickListeners() {
        val prefMgr = PreferenceManager.getDefaultSharedPreferences(this)
        val hapticPref = prefMgr.getBoolean("hapticfdkey", true)

        bkspacebt.isHapticFeedbackEnabled = hapticPref

        bkspacebt.setOnLongClickListener {
            clearFields()
            true
        }

    }

    private fun initPrefs(){
        preferences = getSharedPreferences("CalcPrefs", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    private fun initCalculationPrefs(){
        calculationPref = PreferenceManager.getDefaultSharedPreferences(this)
        val almInt = calculationPref.getBoolean("almostintkey", true)
        val canInt = calculationPref.getBoolean("canonintkey", false)
        val precisionChoice = calculationPref.getString("precisionkey", "Default precision")

        Calc.almostInt = almInt
        Calc.canonInt = canInt
        if (precisionChoice != null) {
            Calc.precision = precisionChoice
        }
        println("Current precision: ${Calc.precision}")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(getString(R.string.text_res), result)

        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if(light){
            menuInflater.inflate(R.menu.menulight, menu)
        }
        else{
            menuInflater.inflate(R.menu.menu, menu)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val item_id = item.itemId

        when(item_id){
            R.id.historyopt -> {
                val hisintent = Intent(this, HistoryActivity::class.java)
                histLauncher.launch(hisintent)
            }
            R.id.selecthemeopt -> { showThemeDialog(this) }
            R.id.settingsopt -> {
                val setIntent = Intent(this, SettingsActivity::class.java)
                settingsLauncher.launch(setIntent)
            }
            R.id.helpopt -> {
                val helpuri = Uri.parse("https://github.com/Yet-Zio/yetCalc/blob/main/HELP.md")
                val helpIntent = Intent(Intent.ACTION_VIEW, helpuri)
                startActivity(helpIntent)
            }
        }
        return true
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {

        val spinner = parent as Spinner
        lateinit var modeitem: String

        when(spinner.id){
            R.id.variable_spinner -> {
                if(!mviewModel.varinitSpinner){
                    varBt.text = getString(R.string.deftext)
                    mviewModel.varinitSpinner = true
                }
                else{
                    if(varSpinner.tag.equals("nodef")){
                        varSpinner.tag = "def"
                    }
                    else{
                        modeitem = parent.getItemAtPosition(pos).toString()
                        varBt.text = modeitem
                        varSpinner.tag = "nodef"
                        varSpinner.setSelection(0)
                    }
                }
            }
            R.id.calcmode_spinner -> {
                if(!mviewModel.initSpinner){
                    modeSpinner.setSelection(mviewModel.spinnerPos)
                    modeitem = parent.getItemAtPosition(mviewModel.spinnerPos).toString()
                    mviewModel.initSpinner = true
                }
                else{
                    mviewModel.spinnerPos = pos
                    modeitem = parent.getItemAtPosition(mviewModel.spinnerPos).toString()
                    println(modeitem)
                }

                if(modeitem != mviewModel.calcMode.str){
                    mviewModel.calcMode = CalcMode.getMode(modeitem)
                    println(mviewModel.calcMode)
                    calcModeSetter()
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun restoreModesAndConfiguration(savedInstanceState: Bundle?){
        if(savedInstanceState != null){
            textres.text = savedInstanceState.getString(getString(R.string.text_res))
            result = textres.text.toString()
        }

        if(mviewModel.angleMode == AngleMode.DEGREE){
            angleViewbt.setText(R.string.degreetext)
            if(getScreenOrientation(this) == Configuration.ORIENTATION_LANDSCAPE)
                setAngleButtonsFirstMode()
        }
        else if(mviewModel.angleMode == AngleMode.RADIAN){
            angleViewbt.setText(R.string.radiantext)
            if(getScreenOrientation(this) == Configuration.ORIENTATION_LANDSCAPE)
                setAngleButtonsSecondMode()
        }

        if(getScreenOrientation(this) == Configuration.ORIENTATION_LANDSCAPE){
            calcModeSetter()
        }
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

    private fun setOnClickListeners(){
        for (bt in button_list){
            bt.setOnClickListener(this)
        }
        histOthBt.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val buttonId = view?.id

        if(buttonId!= null){
            if(getScreenOrientation(this) == Configuration.ORIENTATION_PORTRAIT){
                portraitButtonHandler(buttonId)
            }
            else{
                landscapeButtonHandler(buttonId)
            }
        }

        setVibOnClick(applicationContext)
    }

    private fun setAngleButtonsFirstMode(){
        anglebt.setText(AngleMode.RADIAN.str)
        angleconvbt.setText(getString(R.string.radconvtext))
    }

    private fun setAngleButtonsSecondMode(){
        anglebt.setText(AngleMode.DEGREE.str)
        angleconvbt.setText(getString(R.string.degconvtext))
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

        if(!textexpression.text.isEmpty())
            evaluate_expr()
    }

    private fun angleModeSetter(){
        if(mviewModel.angleMode == AngleMode.DEGREE){
            mviewModel.angleMode= AngleMode.RADIAN
            Calc.angleMode = mviewModel.angleMode
            angleViewbt.setText(mviewModel.angleMode.str)

            if(getScreenOrientation(this) == Configuration.ORIENTATION_LANDSCAPE)
                setAngleButtonsSecondMode()
        }
        else{
            mviewModel.angleMode = AngleMode.DEGREE
            Calc.angleMode = mviewModel.angleMode
            angleViewbt.setText(mviewModel.angleMode.str)

            if(getScreenOrientation(this) == Configuration.ORIENTATION_LANDSCAPE)
                setAngleButtonsFirstMode()
        }

        if(!textexpression.text.isEmpty())
            evaluate_expr()
    }

    private fun addExpression(ex: String){
        val start = Math.max(textexpression.selectionStart, 0)
        val end = Math.max(textexpression.selectionEnd, 0)
        textexpression.text.replace(Math.min(start, end), Math.max(start, end)
            , ex, 0, ex.length)
    }

    private fun portraitButtonHandler(id: Int){
        when(id){
            R.id.sqrtbutton -> {
                addExpression(getString(R.string.sqrootval))
                evaluate_expr()
            }
            else -> {
                handleGeneralButtons(id)
                handleNumberButtons(id)
            }
        }
    }

    private fun landscapeButtonHandler(id: Int){
        handleLandscapeButton(id)
        handleGeneralButtons(id)
        handleNumberButtons(id)
    }

    private fun handleLandscapeButton(id: Int){
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
                            addExpression(getString(R.string.cuberoottext))
                            evaluate_expr()
                        }
                        CalcMode.FOURTHMODE -> {
                            addExpression(getString(R.string.fourthroottext))
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


    private fun handleGeneralButtons(id: Int){
        when(id){
            R.id.histOthBt -> {
                val hisintent = Intent(this, HistoryActivity::class.java)
                histLauncher.launch(hisintent)
            }
            R.id.currentAngleView -> {
                angleModeSetter()
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
                if (textexpression.text.isNotEmpty() && textres.text.isNotEmpty()) {
                    val expression = textexpression.text.toString()
                    val resultText = textres.text.toString()

                    IOCoroutineScope.launch {
                        Calc.addToHistory(expression, resultText)
                    }

                    textexpression.setText(resultText)
                    textres.text = ""
                    textexpression.setSelection(textexpression.text.length)

                    val prefMgr = PreferenceManager.getDefaultSharedPreferences(this)
                    val leftjustres = prefMgr.getBoolean("leftjustifyres", true)

                    if (leftjustres) {
                        val maxScrollX = textexpression.layout.getLineLeft(0).toInt().coerceAtLeast(0)
                        textexpression.post {
                            textexpression.scrollTo(maxScrollX, 0)
                        }
                    }

                    result = resultText
                }
            }
            R.id.backspacebutton -> {
                val cursorPos = textexpression.selectionStart - 1
                val textexprstr = textexpression.text.toString()

                if(!textexprstr.isEmpty()){
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

                        if(!textexpression.text.toString().isEmpty()){
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

    private fun clearFields() {
        textexpression.setText("")
        textres.setText("")
        result = textres.text.toString()
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