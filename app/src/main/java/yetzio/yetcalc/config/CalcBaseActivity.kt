package yetzio.yetcalc.config

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.color.DynamicColors
import com.google.android.material.textfield.TextInputLayout
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import yetzio.yetcalc.views.CalculatorActivity
import yetzio.yetcalc.R
import yetzio.yetcalc.adapters.ModeSelectAdapter
import yetzio.yetcalc.component.Calculator
import yetzio.yetcalc.component.ProgrammerCalculator
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.component.getDefSharedPrefs
import yetzio.yetcalc.models.HistoryViewModel
import yetzio.yetcalc.utils.getThemeColor
import yetzio.yetcalc.views.ProgramCalcActivity
import yetzio.yetcalc.views.UnitConvActivity
import kotlin.properties.Delegates


open class CalcBaseActivity : AppCompatActivity() {
    lateinit var theme: String
    lateinit var currentMatYouStyle: String
    var abyssEncounter by Delegates.notNull<Boolean>()
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    var dark = false
    var light = false

    val Calc = Calculator()
    val PGCalc = ProgrammerCalculator()
    lateinit var historyViewModel: HistoryViewModel

    lateinit var modeSelector: TextInputLayout
    var currentView: CalcView = CalcView.CALCULATOR

    override fun onCreate(savedInstanceState: Bundle?) {
        Calc.m_history.ctx = applicationContext
        historyViewModel = HistoryViewModel.getInstance()
        Calc.m_history.histViewModel = historyViewModel

        preferences = getDefSharedPrefs()
        editor = preferences.edit()

        theme = preferences.getString(SharedPrefs.THEMEKEY, getString(R.string.system_theme)).toString()
        abyssEncounter = preferences.getBoolean(SharedPrefs.ABYSSENCOUNTEREDKEY, false)
        currentMatYouStyle = preferences.getString(SharedPrefs.CURRENTMATSTYLEKEY, SharedPrefs.YOUSTYLE1).toString()

        when (theme) {
            getString(R.string.system_theme) -> {
                val nightModeFlags: Int = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                when (nightModeFlags) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        dark = true
                        light = false
                        setTheme(R.style.Theme_YetCalc_Night)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        dark = false
                        light = true
                        setTheme(R.style.Theme_YetCalc)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        dark = true
                        light = false
                        setTheme(R.style.Theme_YetCalc_Night)
                    }
                }
            }
            getString(R.string.dark_theme) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                dark = true
                light = false
                setTheme(R.style.Theme_YetCalc_Night)
            }
            getString(R.string.abyss_theme) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                dark = true
                light = false
                setTheme(R.style.AbyssTheme)

                if(!abyssEncounter){
                    val abyssMessage = getString(R.string.messageFromAbyss)
                    DynamicToast.make(this, abyssMessage, ContextCompat.getDrawable(this, R.drawable.evil), getThemeColor(R.attr.calcTextDefaultColor), getThemeColor(R.attr.calcBackgroundDefault), Toast.LENGTH_LONG).show()
                    DynamicToast.make(this, abyssMessage, ContextCompat.getDrawable(this, R.drawable.evil), getThemeColor(R.attr.calcTextDefaultColor), getThemeColor(R.attr.calcBackgroundDefault), Toast.LENGTH_LONG).show()

                    with(editor) {
                        putBoolean(SharedPrefs.ABYSSENCOUNTEREDKEY, true)
                        apply()
                    }
                }
            }
            getString(R.string.light_theme) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                dark = false
                light = true
                setTheme(R.style.Theme_YetCalc)
            }
            getString(R.string.materialyou_theme) -> {
                val isDynamicColorEnabled = DynamicColors.isDynamicColorAvailable()
                if (isDynamicColorEnabled) {
                    when(currentMatYouStyle){
                        SharedPrefs.YOUSTYLE1 -> {
                            setTheme(R.style.MaterialYouTheme)
                        }
                        SharedPrefs.YOUSTYLE2 -> {
                            setTheme(R.style.YouStyle2)
                        }
                        SharedPrefs.YOUSTYLE3 -> {
                            setTheme(R.style.YouStyle3)
                        }
                        SharedPrefs.YOUSTYLE4 -> {
                            setTheme(R.style.YouStyle4)
                        }
                    }
                    DynamicColors.applyToActivitiesIfAvailable(applicationContext as Application)
                } else {
                    setTheme(R.style.Theme_YetCalc_Night)
                }
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                dark = true
                light = false
                setTheme(R.style.Theme_YetCalc_Night)
            }
        }

        super.onCreate(savedInstanceState)

        // Let content extend into system windows (status and navigation bars)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Automatically apply insets to the root view of the activity
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBarsInsets.top, 0, systemBarsInsets.bottom) // Adjust padding
            insets
        }
    }

    fun setupModeSelector(){

        val items = when (currentView) {
            CalcView.CALCULATOR -> {
                listOf(
                    Pair("Converter", R.drawable.ic_baseline_cyclone_24),
                    Pair("Programmer", R.drawable.ic_baseline_code_24)
                )
            }
            CalcView.CONVERTER -> {
                listOf(
                    Pair("Calculator", R.drawable.ic_baseline_calculate_24),
                    Pair("Programmer", R.drawable.ic_baseline_code_24)
                )
            }
            else -> {
                listOf(
                    Pair("Calculator", R.drawable.ic_baseline_calculate_24),
                    Pair("Converter", R.drawable.ic_baseline_cyclone_24)
                )
            }
        }

        val adapter = ModeSelectAdapter(this, items)
        val modeTV = (modeSelector.editText as? AutoCompleteTextView)

        modeTV?.setAdapter(adapter)

        when(currentView){
            CalcView.CALCULATOR -> {
                modeTV?.setText("Calculator", false)
                modeSelector.setStartIconDrawable(R.drawable.ic_baseline_calculate_24)
            }
            CalcView.CONVERTER -> {
                modeTV?.setText("Converter", false)
                modeSelector.setStartIconDrawable(R.drawable.ic_baseline_cyclone_24)
            }
            else -> {
                modeTV?.setText("Programmer", false)
                modeSelector.setStartIconDrawable(R.drawable.ic_baseline_code_24)
            }
        }

        val colorStateList = ColorStateList.valueOf(getThemeColor(R.attr.calcTextDefaultColor))
        modeSelector.setStartIconTintList(colorStateList)

        modeTV?.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = adapter.getItem(position)

            modeTV.setText(selectedItem?.first, false)
            selectedItem?.second?.let { modeSelector.setStartIconDrawable(it) }
            when(currentView){
                CalcView.CALCULATOR -> {
                    when(position){
                        0 -> {
                            startActivity(Intent(applicationContext, UnitConvActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                        }
                        1 -> {
                            startActivity(Intent(applicationContext, ProgramCalcActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                        }
                        else -> {
                            // do nothing
                        }
                    }
                }
                CalcView.CONVERTER -> {
                    when(position){
                        0 -> {
                            startActivity(Intent(applicationContext, CalculatorActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                        }
                        1 -> {
                            startActivity(Intent(applicationContext, ProgramCalcActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                        }
                        else -> {
                            // do nothing
                        }
                    }
                }
                else -> {
                    when(position){
                        0 -> {
                            startActivity(Intent(applicationContext, CalculatorActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                        }
                        1 -> {
                            startActivity(Intent(applicationContext, UnitConvActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                        }
                        else -> {
                            // do nothing
                        }
                    }
                }
            }
        }
    }
}
