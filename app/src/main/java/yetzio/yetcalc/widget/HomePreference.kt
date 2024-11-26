package yetzio.yetcalc.widget

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.radiobutton.MaterialRadioButton
import yetzio.yetcalc.R
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.component.getDefSharedPrefs
import yetzio.yetcalc.config.CalcView
import yetzio.yetcalc.config.ShortcutManager

class HomePreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    private lateinit var calcRadioBt: MaterialRadioButton
    private lateinit var unitRadioBt: MaterialRadioButton
    private lateinit var pgRadioBt: MaterialRadioButton
    private val sharedPrefs = context.getDefSharedPrefs()
    private val shortcutManager = ShortcutManager(context)

    private lateinit var prefMode: String
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var addCalcHome: MaterialButton
    private lateinit var addUnitHome: MaterialButton
    private lateinit var addPGHome: MaterialButton

    private lateinit var refreshShortcutSwitch: MaterialSwitch

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        calcRadioBt = holder.findViewById(R.id.calculatorRadio) as MaterialRadioButton
        unitRadioBt = holder.findViewById(R.id.converterRadio) as MaterialRadioButton
        pgRadioBt = holder.findViewById(R.id.programmerRadio) as MaterialRadioButton

        addCalcHome = holder.findViewById(R.id.addCalcHome) as MaterialButton
        addUnitHome = holder.findViewById(R.id.addUnitHome) as MaterialButton
        addPGHome = holder.findViewById(R.id.addPGHome) as MaterialButton

        refreshShortcutSwitch = holder.findViewById(R.id.refreshShortcutSwitch) as MaterialSwitch

        prefMode = sharedPrefs.getString(SharedPrefs.PREFMODEKEY, context.getString(R.string.calculator)).toString()
        editor = sharedPrefs.edit()

        setupPrefRadios()
        setupAddToHomeButtons()
        setupRefreshShortcut()
    }

    private fun setupPrefRadios(){
        when(prefMode){
            context.getString(R.string.calculator) -> {
                calcRadioBt.isChecked = true
                unitRadioBt.isChecked = false
                pgRadioBt.isChecked = false
            }
            context.getString(R.string.converter) -> {
                calcRadioBt.isChecked = false
                unitRadioBt.isChecked = true
                pgRadioBt.isChecked = false
            }
            context.getString(R.string.programmer) -> {
                calcRadioBt.isChecked = false
                unitRadioBt.isChecked = false
                pgRadioBt.isChecked = true
            }
        }

        calcRadioBt.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                calcRadioBt.isChecked = true
                unitRadioBt.isChecked = false
                pgRadioBt.isChecked = false

                editor.putString(SharedPrefs.PREFMODEKEY, context.getString(R.string.calculator))
                editor.apply()
            }
        }

        unitRadioBt.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                calcRadioBt.isChecked = false
                unitRadioBt.isChecked = true
                pgRadioBt.isChecked = false

                editor.putString(SharedPrefs.PREFMODEKEY, context.getString(R.string.converter))
                editor.apply()
            }
        }

        pgRadioBt.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                calcRadioBt.isChecked = false
                unitRadioBt.isChecked = false
                pgRadioBt.isChecked = true

                editor.putString(SharedPrefs.PREFMODEKEY, context.getString(R.string.programmer))
                editor.apply()
            }
        }
    }

    private fun setupAddToHomeButtons(){
        addCalcHome.setOnClickListener {
            shortcutManager.createShortcut(CalcView.CALCULATOR)
        }

        addUnitHome.setOnClickListener {
            shortcutManager.createShortcut(CalcView.CONVERTER)
        }

        addPGHome.setOnClickListener {
            shortcutManager.createShortcut(CalcView.PROGRAMMER)
        }
    }

    private fun setupRefreshShortcut(){
        val refreshShortcut = sharedPrefs.getBoolean(SharedPrefs.REFRESH_SHORTCUT, true)

        if(refreshShortcut){
            refreshShortcutSwitch.isChecked = true
        }

        refreshShortcutSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                editor.putBoolean(SharedPrefs.REFRESH_SHORTCUT, true)
                editor.apply()
            }
            else{
                editor.putBoolean(SharedPrefs.REFRESH_SHORTCUT, false)
                editor.apply()
            }
        }
    }
}