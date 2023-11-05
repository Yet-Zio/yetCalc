package yetzio.yetcalc.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.airbnb.paris.Paris
import yetzio.yetcalc.MainActivity
import yetzio.yetcalc.R
import yetzio.yetcalc.component.NumberSystem
import yetzio.yetcalc.component.Operator
import yetzio.yetcalc.component.SpinnerItemAdapter
import yetzio.yetcalc.model.ProgramCalcViewModel
import yetzio.yetcalc.utils.getModesList
import yetzio.yetcalc.utils.getScreenOrientation
import yetzio.yetcalc.utils.setVibOnClick
import yetzio.yetcalc.utils.showThemeDialog
import java.math.BigInteger

class ProgramCalcActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvExp: TextView
    private lateinit var tvResult: TextView
    private lateinit var tvHex: TextView
    private lateinit var tvDec: TextView
    private lateinit var tvOct: TextView
    private lateinit var tvBin: TextView

    private var buttonlist = ArrayList<Button>()

    private lateinit var btnForHex: Button
    private lateinit var btnForDec: Button
    private lateinit var btnForOct: Button
    private lateinit var btnForBin: Button

    private lateinit var inputBtnLSH: Button
    private lateinit var inputBtnRSH: Button

    private lateinit var inputBtnDouble0: Button
    private lateinit var inputBtn0: Button
    private lateinit var inputBtn1: Button
    private lateinit var inputBtn2: Button
    private lateinit var inputBtn3: Button
    private lateinit var inputBtn4: Button
    private lateinit var inputBtn5: Button
    private lateinit var inputBtn6: Button
    private lateinit var inputBtn7: Button
    private lateinit var inputBtn8: Button
    private lateinit var inputBtn9: Button

    private lateinit var inputBtnA: Button
    private lateinit var inputBtnB: Button
    private lateinit var inputBtnC: Button
    private lateinit var inputBtnD: Button
    private lateinit var inputBtnE: Button
    private lateinit var inputBtnF: Button

    private lateinit var inputBtnAddition: Button
    private lateinit var inputBtnSubtraction: Button
    private lateinit var inputBtnMultiply: Button
    private lateinit var inputBtnDivide: Button

    private lateinit var inputBtnAnd: Button
    private lateinit var inputBtnOr: Button
    private lateinit var inputBtnNot: Button
    private lateinit var inputBtnNand: Button
    private lateinit var inputBtnNor: Button
    private lateinit var inputBtnXor: Button

    private lateinit var inputBtnCl: Button
    private lateinit var inputBtnAC: Button
    private lateinit var inputBtnEqual: Button

    private lateinit var modeselecSpin: Spinner
    lateinit var mViewModel: ProgramCalcViewModel

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var theme: String
    private var dark = false
    private var light = false

    private lateinit var toolbar: Toolbar

    private lateinit var opsList: ArrayList<String>
    private var booleanOpsList = arrayListOf("AND", "OR", "NAND", "NOR", "XOR", "Lsh", "Rsh") // NOT is unary, so not required

    override fun onCreate(savedInstanceState: Bundle?) {
        initPrefs()
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
        setContentView(R.layout.activity_program_calc)

        opsList = arrayListOf()

        for(i in Operator.values()){
            i.str?.let { opsList.add(it) }
        }

        mViewModel = ViewModelProvider(this)[ProgramCalcViewModel::class.java]

        toolbar = findViewById(R.id.prgmappbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

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
        modeselecSpin.setSelection(2)
        modeselecSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if(pos == 0){
                    startActivity(Intent(applicationContext, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                }
                else if(pos == 1){
                    startActivity(Intent(applicationContext, UnitConvActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        tvExp = findViewById(R.id.tvExp)
        tvResult = findViewById(R.id.tvResult)

        tvHex = findViewById(R.id.tvHex)
        tvDec = findViewById(R.id.tvDec)
        tvOct = findViewById(R.id.tvOct)
        tvBin = findViewById(R.id.tvBin)

        btnForHex = findViewById(R.id.btnForHex)
        btnForDec = findViewById(R.id.btnForDec)
        btnForOct = findViewById(R.id.btnForOct)
        btnForBin = findViewById(R.id.btnForBin)

        inputBtnDouble0 = findViewById(R.id.inputBtnDouble0)
        inputBtn0 = findViewById(R.id.inputBtn0)
        inputBtn1 = findViewById(R.id.inputBtn1)
        inputBtn2 = findViewById(R.id.inputBtn2)
        inputBtn3 = findViewById(R.id.inputBtn3)
        inputBtn4 = findViewById(R.id.inputBtn4)
        inputBtn5 = findViewById(R.id.inputBtn5)
        inputBtn6 = findViewById(R.id.inputBtn6)
        inputBtn7 = findViewById(R.id.inputBtn7)
        inputBtn8 = findViewById(R.id.inputBtn8)
        inputBtn9 = findViewById(R.id.inputBtn9)

        inputBtnA = findViewById(R.id.inputBtnA)
        inputBtnB = findViewById(R.id.inputBtnB)
        inputBtnC = findViewById(R.id.inputBtnC)
        inputBtnD = findViewById(R.id.inputBtnD)
        inputBtnE = findViewById(R.id.inputBtnE)
        inputBtnF = findViewById(R.id.inputBtnF)

        inputBtnAddition = findViewById(R.id.inputBtnAddition)
        inputBtnSubtraction = findViewById(R.id.inputBtnSubtraction)
        inputBtnMultiply = findViewById(R.id.inputBtnMultiply)
        inputBtnDivide = findViewById(R.id.inputBtnDivide)

        inputBtnAnd = findViewById(R.id.inputBtnAnd)
        inputBtnOr = findViewById(R.id.inputBtnOr)
        inputBtnNot = findViewById(R.id.inputBtnNot)
        inputBtnNand = findViewById(R.id.inputBtnNand)
        inputBtnNor = findViewById(R.id.inputBtnNor)
        inputBtnXor = findViewById(R.id.inputBtnXor)

        inputBtnLSH = findViewById(R.id.inputBtnLSH)
        inputBtnRSH = findViewById(R.id.inputBtnRSH)

        inputBtnCl = findViewById(R.id.inputBtnCl)
        inputBtnAC = findViewById(R.id.inputBtnAC)
        inputBtnEqual = findViewById(R.id.inputBtnEqual)

        buttonlist.add(btnForHex)
        buttonlist.add(btnForDec)
        buttonlist.add(btnForOct)
        buttonlist.add(btnForBin)

        buttonlist.add(inputBtnDouble0)
        buttonlist.add(inputBtn0)
        buttonlist.add(inputBtn1)
        buttonlist.add(inputBtn2)
        buttonlist.add(inputBtn3)
        buttonlist.add(inputBtn4)
        buttonlist.add(inputBtn5)
        buttonlist.add(inputBtn6)
        buttonlist.add(inputBtn7)
        buttonlist.add(inputBtn8)
        buttonlist.add(inputBtn9)

        buttonlist.add(inputBtnA)
        buttonlist.add(inputBtnB)
        buttonlist.add(inputBtnC)
        buttonlist.add(inputBtnD)
        buttonlist.add(inputBtnE)
        buttonlist.add(inputBtnF)

        buttonlist.add(inputBtnAddition)
        buttonlist.add(inputBtnSubtraction)
        buttonlist.add(inputBtnMultiply)
        buttonlist.add(inputBtnDivide)

        buttonlist.add(inputBtnAnd)
        buttonlist.add(inputBtnOr)
        buttonlist.add(inputBtnNot)
        buttonlist.add(inputBtnNand)
        buttonlist.add(inputBtnNor)
        buttonlist.add(inputBtnXor)

        buttonlist.add(inputBtnLSH)
        buttonlist.add(inputBtnRSH)

        buttonlist.add(inputBtnCl)
        buttonlist.add(inputBtnAC)
        buttonlist.add(inputBtnEqual)

        restoreExp()

        mViewModel.initialized = false

        if(light){
            Paris.style(findViewById<Toolbar>(R.id.prgmappbar)).apply(R.style.AppBarLight)
            Paris.style(modeselecSpin).apply(R.style.AppModeSpinnerStyleLight)

            Paris.style(tvExp).apply(R.style.yetCalcTVDarkResLight)

            if(getScreenOrientation(applicationContext) == Configuration.ORIENTATION_LANDSCAPE){
                Paris.style(tvResult).apply(R.style.programCalcTVLandLight)
            }
            else{
                Paris.style(tvResult).apply(R.style.yetCalcTVDarkLight)
            }

            Paris.style(tvHex).apply(R.style.programCalcTVLight)
            Paris.style(tvDec).apply(R.style.programCalcTVLight)
            Paris.style(tvOct).apply(R.style.programCalcTVLight)
            Paris.style(tvBin).apply(R.style.programCalcTVLight)

            Paris.style(btnForHex).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(btnForDec).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(btnForOct).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(btnForBin).apply(R.style.programCalcBorderlessButtonLight)

            Paris.style(inputBtnAC).apply(R.style.programCalcBorderlessButtonAltLight)
            Paris.style(inputBtnCl).apply(R.style.programCalcBorderlessButtonAltLight)

            Paris.style(inputBtnLSH).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnRSH).apply(R.style.programCalcBorderlessButtonLight)

            Paris.style(inputBtnDouble0).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnEqual).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtn0).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtn1).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtn2).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtn3).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtn4).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtn5).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtn6).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtn7).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtn8).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtn9).apply(R.style.programCalcBorderlessButtonLight)

            Paris.style(inputBtnA).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnB).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnC).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnD).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnE).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnF).apply(R.style.programCalcBorderlessButtonLight)

            Paris.style(inputBtnAddition).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnSubtraction).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnMultiply).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnDivide).apply(R.style.programCalcBorderlessButtonLight)

            Paris.style(inputBtnAnd).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnOr).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnNot).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnNand).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnNor).apply(R.style.programCalcBorderlessButtonLight)
            Paris.style(inputBtnXor).apply(R.style.programCalcBorderlessButtonLight)
        }

        // sets the current number system, by default it is 'Decimal'
        setCurrentNumberSystem(mViewModel.numberSys)

        mViewModel.initialized = true

        setOnClickListeners()

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

    private fun initPrefs(){
        preferences = getSharedPreferences("CalcPrefs", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    private fun restoreExp(){
        if(mViewModel.textRES.isNotEmpty()){
            tvResult.text = mViewModel.textRES
        }
        else{
            tvResult.text = "0"
        }

        if(mViewModel.textEXP.isNotEmpty()){
            tvExp.text = mViewModel.textEXP
        }
    }

    private fun setOnClickListeners(){
        for(btn in buttonlist){
            btn.setOnClickListener(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if(light){
            menuInflater.inflate(R.menu.menulight, menu)
        }
        else{
            menuInflater.inflate(R.menu.menu, menu)
        }

        val historyopt = menu.findItem(R.id.historyopt)
        historyopt.isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val item_id = item.itemId

        when(item_id){
            R.id.selecthemeopt -> { showThemeDialog(this) }
            R.id.settingsopt -> {
                val setIntent = Intent(this, SettingsActivity::class.java)
                startActivity(setIntent)
            }
            R.id.helpopt -> {
                val helpuri = Uri.parse("https://github.com/Yet-Zio/yetCalc/blob/main/HELP.md")
                val helpIntent = Intent(Intent.ACTION_VIEW, helpuri)
                startActivity(helpIntent)
            }
        }
        return true
    }

    override fun onClick(view: View?) {
        val buttonId = view?.id

        if(mViewModel.divByZero){
            tvResult.text = "0"
            tvExp.text = "0"
            mViewModel.divByZero = false
        }

        if(buttonId != null){
            when(buttonId){
                R.id.btnForHex -> {
                    setCurrentNumberSystem(NumberSystem.HEX)
                }
                R.id.btnForDec -> {
                    setCurrentNumberSystem(NumberSystem.DEC)
                }
                R.id.btnForOct -> {
                    setCurrentNumberSystem(NumberSystem.OCT)
                }
                R.id.btnForBin -> {
                    setCurrentNumberSystem(NumberSystem.BIN)
                }
                R.id.inputBtnDouble0 -> {
                    tvExp.append(inputBtnDouble0.text)
                    inputReceived(inputBtnDouble0.text)
                }
                R.id.inputBtn0 -> {
                    tvExp.append(inputBtn0.text)
                    inputReceived(inputBtn0.text)
                }
                R.id.inputBtn1 -> {
                    tvExp.append(inputBtn1.text)
                    inputReceived(inputBtn1.text)
                }
                R.id.inputBtn2 -> {
                    tvExp.append(inputBtn2.text)
                    inputReceived(inputBtn2.text)
                }
                R.id.inputBtn3 -> {
                    tvExp.append(inputBtn3.text)
                    inputReceived(inputBtn3.text)
                }
                R.id.inputBtn4 -> {
                    tvExp.append(inputBtn4.text)
                    inputReceived(inputBtn4.text)
                }
                R.id.inputBtn5 -> {
                    tvExp.append(inputBtn5.text)
                    inputReceived(inputBtn5.text)
                }
                R.id.inputBtn6 -> {
                    tvExp.append(inputBtn6.text)
                    inputReceived(inputBtn6.text)
                }
                R.id.inputBtn7 -> {
                    tvExp.append(inputBtn7.text)
                    inputReceived(inputBtn7.text)
                }
                R.id.inputBtn8 -> {
                    tvExp.append(inputBtn8.text)
                    inputReceived(inputBtn8.text)
                }
                R.id.inputBtn9 -> {
                    tvExp.append(inputBtn9.text)
                    inputReceived(inputBtn9.text)
                }
                R.id.inputBtnA -> {
                    tvExp.append(inputBtnA.text)
                    inputReceived(inputBtnA.text)
                }
                R.id.inputBtnB -> {
                    tvExp.append(inputBtnB.text)
                    inputReceived(inputBtnB.text)
                }
                R.id.inputBtnC -> {
                    tvExp.append(inputBtnC.text)
                    inputReceived(inputBtnC.text)
                }
                R.id.inputBtnD -> {
                    tvExp.append(inputBtnD.text)
                    inputReceived(inputBtnD.text)
                }
                R.id.inputBtnE -> {
                    tvExp.append(inputBtnE.text)
                    inputReceived(inputBtnE.text)
                }
                R.id.inputBtnF -> {
                    tvExp.append(inputBtnF.text)
                    inputReceived(inputBtnF.text)
                }
                R.id.inputBtnAddition -> {
                    operatorSelected(Operator.ADD)
                    tvExp.append(" " + getString(R.string.plusop) + " ")
                    mViewModel.textEXP = tvExp.text.toString()
                }
                R.id.inputBtnSubtraction -> {
                    operatorSelected(Operator.SUB)
                    tvExp.append(" " + getString(R.string.minusop) + " ")
                    mViewModel.textEXP = tvExp.text.toString()
                }
                R.id.inputBtnMultiply -> {
                    operatorSelected(Operator.MUL)
                    tvExp.append(" " + getString(R.string.mulop) + " ")
                    mViewModel.textEXP = tvExp.text.toString()
                }
                R.id.inputBtnDivide -> {
                    operatorSelected(Operator.DIV)
                    tvExp.append(" " + getString(R.string.divide_expr) + " ")
                    mViewModel.textEXP = tvExp.text.toString()
                }
                R.id.inputBtnAnd -> {
                    operatorSelected(Operator.AND)
                    tvExp.append(" " + getString(R.string.ANDstr) + " ")
                    mViewModel.textEXP = tvExp.text.toString()
                }
                R.id.inputBtnOr -> {
                    operatorSelected(Operator.OR)
                    tvExp.append(" " + getString(R.string.ORstr) + " ")
                    mViewModel.textEXP = tvExp.text.toString()
                }
                R.id.inputBtnNot -> {
                    operatorSelected(Operator.NOT)
                    tvExp.append(" " + getString(R.string.NOTstr) + " ")
                    mViewModel.textEXP = tvExp.text.toString()
                }
                R.id.inputBtnNand -> {
                    operatorSelected(Operator.NAND)
                    tvExp.append(" " + getString(R.string.NANDstr) + " ")
                    mViewModel.textEXP = tvExp.text.toString()
                }
                R.id.inputBtnNor -> {
                    operatorSelected(Operator.NOR)
                    tvExp.append(" " + getString(R.string.NORstr) + " ")
                    mViewModel.textEXP = tvExp.text.toString()
                }
                R.id.inputBtnXor -> {
                    operatorSelected(Operator.XOR)
                    tvExp.append(" " + getString(R.string.XORstr) + " ")
                    mViewModel.textEXP = tvExp.text.toString()
                }
                R.id.inputBtnLSH -> {
                    operatorSelected(Operator.LSH)
                    tvExp.append(" " + getString(R.string.LSHstr) + " ")
                    mViewModel.textEXP = tvExp.text.toString()
                }
                R.id.inputBtnRSH -> {
                    operatorSelected(Operator.RSH)
                    tvExp.append(" " + getString(R.string.RSHstr) + " ")
                    mViewModel.textEXP = tvExp.text.toString()
                }
                R.id.inputBtnCl -> {
                    tvExp.text = ""
                    tvResult.text = "0"
                }
                R.id.inputBtnAC -> {
                    tvExp.text = ""
                    tvResult.text = "0"
                    mViewModel.prevResult = BigInteger("0")
                    setResultText()
                    if (mViewModel.currentOp != null) {
                        mViewModel.currentOp = null
                        mViewModel.isCalcPending = false
                        mViewModel.clearInput = false
                    }
                }
                R.id.inputBtnEqual -> {
                    tvExp.text = ""
                    if (mViewModel.isCalcPending) {
                        calculate()
                        mViewModel.currentOp = null
                        mViewModel.isCalcPending = false
                    }
                    else{
                        mViewModel.prevResult = tvResult.text.toString().toBigInteger(mViewModel.numberSys.radix)
                        setCurrentNumberSystem(mViewModel.numberSys)
                    }
                }
            }
        }

        setVibOnClick(applicationContext)
    }

    private fun operatorSelected(operator: Operator) {

        if (mViewModel.currentOp == null) {

            if (operator == Operator.NOT) {
                var temp = tvResult.text.toString().toInt(mViewModel.numberSys.radix)
                tvResult.text = temp.inv().toString(mViewModel.numberSys.radix)
                mViewModel.prevResult = tvResult.text.toString().toBigInteger(mViewModel.numberSys.radix)
                setResultText()
                mViewModel.clearInput = true
                return
            }
            mViewModel.prevResult = tvResult.text.toString().toBigInteger(mViewModel.numberSys.radix)
        } else if (mViewModel.currentOp != null) {
            mViewModel.clearInput = true
            if (operator == Operator.NOT) {
                var temp = tvResult.text.toString().toInt(mViewModel.numberSys.radix)
                tvResult.text = temp.inv().toString(mViewModel.numberSys.radix)
                calculate()
                mViewModel.isCalcPending = false
                mViewModel.clearInput = true
                mViewModel.currentOp = null
                return
            }
            calculate()
        }
        mViewModel.currentOp = operator
        mViewModel.isCalcPending = true
        mViewModel.clearInput = true
    }

    private fun calculate() {
        when (mViewModel.currentOp) {

            Operator.ADD -> {
                mViewModel.prevResult += tvResult.text.toString().toBigInteger(mViewModel.numberSys.radix)
            }

            Operator.SUB -> {
                mViewModel.prevResult -= tvResult.text.toString().toBigInteger(mViewModel.numberSys.radix)
            }

            Operator.MUL -> {
                mViewModel.prevResult *= tvResult.text.toString().toBigInteger(mViewModel.numberSys.radix)
            }

            Operator.DIV -> {
                if(tvResult.text.toString().toInt(mViewModel.numberSys.radix) == 0){
                    mViewModel.divByZero = true
                    mViewModel.prevResult = BigInteger("0")
                }
                else{
                    mViewModel.prevResult /= tvResult.text.toString().toBigInteger(mViewModel.numberSys.radix)
                }
            }

            Operator.AND -> {
                mViewModel.prevResult = mViewModel.prevResult and tvResult.text.toString().toBigInteger(mViewModel.numberSys.radix)
            }

            Operator.OR -> {
                mViewModel.prevResult = mViewModel.prevResult or tvResult.text.toString().toBigInteger(mViewModel.numberSys.radix)
            }

            Operator.NAND -> {
                mViewModel.prevResult =
                    (mViewModel.prevResult and tvResult.text.toString().toBigInteger(mViewModel.numberSys.radix)).inv()
            }

            Operator.NOR -> {
                mViewModel.prevResult =
                    (mViewModel.prevResult or tvResult.text.toString().toBigInteger(mViewModel.numberSys.radix)).inv()
            }

            Operator.XOR -> {
                mViewModel.prevResult = mViewModel.prevResult xor tvResult.text.toString().toBigInteger(mViewModel.numberSys.radix)
            }

            Operator.LSH -> {
                mViewModel.prevResult = mViewModel.prevResult shl tvResult.text.toString().toInt(mViewModel.numberSys.radix)
            }

            Operator.RSH -> {
                mViewModel.prevResult = mViewModel.prevResult shr tvResult.text.toString().toInt(mViewModel.numberSys.radix)
            }

            else -> {
                // do nothing
            }
        }
        setResultText()

    }

    private fun inputReceived(input: CharSequence) {
        if (tvResult.text.trim() == "0" || mViewModel.clearInput) {
            tvResult.text = input
            mViewModel.clearInput = false
        } else {
            tvResult.text = tvResult.text.toString() + input
        }
        mViewModel.textEXP = tvExp.text.toString()
        mViewModel.textRES = tvResult.text.toString()
    }

    private fun setResultText() {

        tvHex.text = mViewModel.prevResult.toString(NumberSystem.HEX.radix)
        tvDec.text = mViewModel.prevResult.toString()
        tvOct.text = mViewModel.prevResult.toString(NumberSystem.OCT.radix)
        tvBin.text = mViewModel.prevResult.toString(NumberSystem.BIN.radix)

        when (mViewModel.numberSys) {
            NumberSystem.HEX -> {
                tvExp.text = tvHex.text
                tvResult.text = tvHex.text
            }
            NumberSystem.DEC -> {
                tvExp.text = tvDec.text
                tvResult.text = tvDec.text
            }
            NumberSystem.OCT -> {
                tvExp.text = tvOct.text
                tvResult.text = tvOct.text
            }
            NumberSystem.BIN -> {
                tvExp.text = tvBin.text
                tvResult.text = tvBin.text
            }
        }

        mViewModel.textEXP = tvExp.text.toString()
        mViewModel.textRES = tvResult.text.toString()

        if(mViewModel.divByZero) {
            tvResult.text = "Cannot divide by zero!"
        }
    }

    private fun setCurrentNumberSystem(numSys: NumberSystem) {

        for(i in opsList){
            if(tvExp.text.toString().contains(i) && tvExp.text.toString().trim().indexOf(i) == tvExp.text.toString().trim().lastIndex){
                mViewModel.opPresent = true
                break
            }

            if(i in booleanOpsList){
                val inc = when (i) {
                    "OR" -> {
                        1
                    }
                    "NAND" -> {
                        3
                    }
                    else -> {
                        2
                    }
                }

                if(tvExp.text.toString().contains(i) && tvExp.text.toString().trim().indexOf(i) + inc == tvExp.text.toString().trim().lastIndex){
                    mViewModel.opPresent = true
                    break
                }
            }

        }

        if(!mViewModel.opPresent){
            tvExp.text = ""
            if (mViewModel.isCalcPending) {
                calculate()
                mViewModel.currentOp = null
                mViewModel.isCalcPending = false
            }
        }

        if (mViewModel.prevResult == BigInteger("0") && tvResult.text.toString() != "0") {
            mViewModel.prevResult = tvResult.text.toString().toBigInteger(mViewModel.numberSys.radix)
        }

        mViewModel.numberSys = numSys

        btnForHex.setTextColor(ContextCompat.getColor(this, R.color.greyish))
        tvHex.setTextColor(ContextCompat.getColor(this, R.color.greyish))

        btnForDec.setTextColor(ContextCompat.getColor(this, R.color.greyish))
        tvDec.setTextColor(ContextCompat.getColor(this, R.color.greyish))

        btnForOct.setTextColor(ContextCompat.getColor(this, R.color.greyish))
        tvOct.setTextColor(ContextCompat.getColor(this, R.color.greyish))

        btnForBin.setTextColor(ContextCompat.getColor(this, R.color.greyish))
        tvBin.setTextColor(ContextCompat.getColor(this, R.color.greyish))

        enableAllInput()

        when (numSys) {
            NumberSystem.HEX -> {
                if(light){
                    btnForHex.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    tvHex.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtnA.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtnB.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtnC.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtnD.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtnE.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtnF.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))

                    inputBtn7.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn8.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn9.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))

                    inputBtn4.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn5.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn6.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))

                    inputBtn2.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn3.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                }
                else{
                    btnForHex.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    tvHex.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtnA.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtnB.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtnC.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtnD.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtnE.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtnF.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))

                    inputBtn7.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn8.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn9.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))

                    inputBtn4.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn5.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn6.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))

                    inputBtn2.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn3.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                }
            }
            NumberSystem.DEC -> {
                enableDecInputBtns()
                if(light){
                    btnForDec.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    tvDec.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtnA.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnB.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnC.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnD.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnE.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnF.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))

                    inputBtn7.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn8.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn9.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))

                    inputBtn4.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn5.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn6.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))

                    inputBtn2.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn3.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                }
                else{
                    btnForDec.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    tvDec.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtnA.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnB.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnC.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnD.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnE.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnF.setTextColor(ContextCompat.getColor(this, R.color.greyish))

                    inputBtn7.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn8.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn9.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))

                    inputBtn4.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn5.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn6.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))

                    inputBtn2.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn3.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                }
            }
            NumberSystem.OCT -> {
                enableOctInputBtns()
                if(light){
                    btnForOct.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    tvOct.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtnA.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnB.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnC.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnD.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnE.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnF.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))

                    inputBtn7.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn8.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtn9.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))

                    inputBtn4.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn5.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn6.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))

                    inputBtn2.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtn3.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                }
                else{
                    btnForOct.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    tvOct.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtnA.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnB.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnC.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnD.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnE.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnF.setTextColor(ContextCompat.getColor(this, R.color.greyish))

                    inputBtn7.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn8.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtn9.setTextColor(ContextCompat.getColor(this, R.color.greyish))

                    inputBtn4.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn5.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn6.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))

                    inputBtn2.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtn3.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                }

            }
            NumberSystem.BIN -> {
                enableBinInputBtns()
                if(light){
                    btnForBin.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    tvBin.setTextColor(ContextCompat.getColor(this, R.color.calc_textdeflight))
                    inputBtnA.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnB.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnC.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnD.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnE.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtnF.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))

                    inputBtn7.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtn8.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtn9.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))

                    inputBtn4.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtn5.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtn6.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))

                    inputBtn2.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                    inputBtn3.setTextColor(ContextCompat.getColor(this, R.color.greyishlight))
                }
                else{
                    btnForBin.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    tvBin.setTextColor(ContextCompat.getColor(this, R.color.calc_textdef))
                    inputBtnA.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnB.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnC.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnD.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnE.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtnF.setTextColor(ContextCompat.getColor(this, R.color.greyish))

                    inputBtn7.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtn8.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtn9.setTextColor(ContextCompat.getColor(this, R.color.greyish))

                    inputBtn4.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtn5.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtn6.setTextColor(ContextCompat.getColor(this, R.color.greyish))

                    inputBtn2.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                    inputBtn3.setTextColor(ContextCompat.getColor(this, R.color.greyish))
                }
            }
        }

        if(mViewModel.initialized){
            setResultText()
        }

        if(mViewModel.opPresent){
            tvExp.append(" " + mViewModel.currentOp!!.str + " ")
            mViewModel.textEXP = tvExp.text.toString()
            mViewModel.opPresent = false
        }
    }

    private fun enableAllInput() {
        inputBtnA.isEnabled = true
        inputBtnB.isEnabled = true
        inputBtnC.isEnabled = true
        inputBtnD.isEnabled = true
        inputBtnE.isEnabled = true
        inputBtnF.isEnabled = true

        inputBtn2.isEnabled = true
        inputBtn3.isEnabled = true
        inputBtn4.isEnabled = true
        inputBtn5.isEnabled = true
        inputBtn6.isEnabled = true
        inputBtn7.isEnabled = true
        inputBtn8.isEnabled = true
        inputBtn9.isEnabled = true
    }

    private fun enableBinInputBtns() {
        enableOctInputBtns()

        inputBtn2.isEnabled = false
        inputBtn3.isEnabled = false
        inputBtn4.isEnabled = false
        inputBtn5.isEnabled = false
        inputBtn6.isEnabled = false
        inputBtn7.isEnabled = false

    }

    private fun enableOctInputBtns() {

        inputBtnA.isEnabled = false
        inputBtnB.isEnabled = false
        inputBtnC.isEnabled = false
        inputBtnD.isEnabled = false
        inputBtnE.isEnabled = false
        inputBtnF.isEnabled = false

        inputBtn8.isEnabled = false
        inputBtn9.isEnabled = false
    }

    private fun enableDecInputBtns() {

        enableOctInputBtns()

        inputBtn8.isEnabled = true
        inputBtn9.isEnabled = true
    }

}