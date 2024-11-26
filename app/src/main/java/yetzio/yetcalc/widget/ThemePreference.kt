package yetzio.yetcalc.widget

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.ChipGroup
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textview.MaterialTextView
import yetzio.yetcalc.R
import yetzio.yetcalc.YetCalc
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.component.getDefSharedPrefs
import yetzio.yetcalc.utils.getThemeColor

class ThemePreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    val sharedPrefs = context.getDefSharedPrefs()
    private lateinit var themeToggleGroup: MaterialButtonToggleGroup
    private lateinit var youColorsContainer: LinearLayout
    private lateinit var youColorsChipGroup: ChipGroup

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        themeToggleGroup = holder.findViewById(R.id.themetogglegroup) as MaterialButtonToggleGroup
        val abyssSwitch: MaterialSwitch = holder.findViewById(R.id.abyssSwitch) as MaterialSwitch
        val matYouSwitch: MaterialSwitch = holder.findViewById(R.id.materialYouSwitch) as MaterialSwitch

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S){
            matYouSwitch.isEnabled = false
            matYouSwitch.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.disabled_switch_track_tint))

            val matYouImg: ImageView = holder.findViewById(R.id.matYouImg) as ImageView
            matYouImg.imageTintList = ColorStateList.valueOf(context.getThemeColor(R.attr.textResultColor))

            val matYouText: MaterialTextView = holder.findViewById(R.id.matYouText) as MaterialTextView
            matYouText.setTextColor(context.getThemeColor(R.attr.textResultColor))

            val matYouSubText: MaterialTextView = holder.findViewById(R.id.matYouSubText) as MaterialTextView
            matYouSubText.setTextColor(context.getThemeColor(R.attr.textResultColor))
        }
        else{
            youColorsContainer = holder.findViewById(R.id.youColorsContainer) as LinearLayout
            youColorsChipGroup = holder.findViewById(R.id.matYouChipGroup) as ChipGroup
        }

        val selectedTheme = sharedPrefs.getString(SharedPrefs.THEMEKEY, context.getString(R.string.system_theme))

        val currentMatStyle = sharedPrefs.getString(SharedPrefs.CURRENTMATSTYLEKEY, SharedPrefs.YOUSTYLE1)

        when (selectedTheme) {
            context.getString(R.string.system_theme) -> {
                themeToggleGroup.check(R.id.systemDefaultBt)
                val checkedButton: MaterialButton = themeToggleGroup.findViewById(themeToggleGroup.checkedButtonId)
                val lightBt: MaterialButton = themeToggleGroup.findViewById(R.id.lightThBt)
                val darkBt: MaterialButton = themeToggleGroup.findViewById(R.id.darkThBt)

                checkedButton.backgroundTintList = ColorStateList.valueOf(context.getThemeColor(R.attr.darkViewColor))
                lightBt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))
                darkBt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))

                abyssSwitch.isChecked = false
                matYouSwitch.isChecked = false
            }
            context.getString(R.string.light_theme) -> {
                themeToggleGroup.check(R.id.lightThBt)
                val checkedButton: MaterialButton = themeToggleGroup.findViewById(themeToggleGroup.checkedButtonId)
                val systemBt: MaterialButton = themeToggleGroup.findViewById(R.id.systemDefaultBt)
                val darkBt: MaterialButton = themeToggleGroup.findViewById(R.id.darkThBt)

                checkedButton.backgroundTintList = ColorStateList.valueOf(context.getThemeColor(R.attr.darkViewColor))
                systemBt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))
                darkBt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))

                abyssSwitch.isChecked = false
                matYouSwitch.isChecked = false

            }
            context.getString(R.string.dark_theme) -> {
                themeToggleGroup.check(R.id.darkThBt)
                val checkedButton: MaterialButton = themeToggleGroup.findViewById(themeToggleGroup.checkedButtonId)
                val lightBt: MaterialButton = themeToggleGroup.findViewById(R.id.lightThBt)
                val systemBt: MaterialButton = themeToggleGroup.findViewById(R.id.systemDefaultBt)

                checkedButton.backgroundTintList = ColorStateList.valueOf(context.getThemeColor(R.attr.darkViewColor))
                lightBt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))
                systemBt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))

                abyssSwitch.isChecked = false
                matYouSwitch.isChecked = false
            }
            context.getString(R.string.abyss_theme) -> {
                val sysBt: MaterialButton = themeToggleGroup.findViewById(R.id.systemDefaultBt)
                val lightBt: MaterialButton = themeToggleGroup.findViewById(R.id.lightThBt)
                val systemBt: MaterialButton = themeToggleGroup.findViewById(R.id.systemDefaultBt)

                sysBt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))
                lightBt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))
                systemBt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))

                abyssSwitch.isChecked = true
                matYouSwitch.isChecked = false
            }
            context.getString(R.string.materialyou_theme) -> {
                val sysBt: MaterialButton = themeToggleGroup.findViewById(R.id.systemDefaultBt)
                val lightBt: MaterialButton = themeToggleGroup.findViewById(R.id.lightThBt)
                val systemBt: MaterialButton = themeToggleGroup.findViewById(R.id.systemDefaultBt)

                sysBt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))
                lightBt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))
                systemBt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))

                abyssSwitch.isChecked = false
                matYouSwitch.isChecked = true

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                    when(currentMatStyle){
                        SharedPrefs.YOUSTYLE1 -> {
                            youColorsChipGroup.check(R.id.matYouChipOne)
                        }
                        SharedPrefs.YOUSTYLE2 -> {
                            youColorsChipGroup.check(R.id.matYouChipTwo)
                        }
                        SharedPrefs.YOUSTYLE3 -> {
                            youColorsChipGroup.check(R.id.matYouChipThree)
                        }
                        SharedPrefs.YOUSTYLE4 -> {
                            youColorsChipGroup.check(R.id.matYouChipFour)
                        }
                    }
                }
            }
        }

        if(!matYouSwitch.isChecked){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                youColorsContainer.visibility = View.GONE
            }
        }
        else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                youColorsContainer.visibility = View.VISIBLE
            }
        }

        themeToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val theme = when (checkedId) {
                    R.id.systemDefaultBt -> context.getString(R.string.system_theme)
                    R.id.lightThBt -> context.getString(R.string.light_theme)
                    R.id.darkThBt -> context.getString(R.string.dark_theme)
                    else -> context.getString(R.string.system_theme)
                }

                with(sharedPrefs.edit()) {
                    putString(SharedPrefs.THEMEKEY, theme)
                    apply()
                }

                applyTheme(theme)
            }
        }

        abyssSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                val theme = context.getString(R.string.abyss_theme)

                with(sharedPrefs.edit()) {
                    putString(SharedPrefs.THEMEKEY, theme)
                    apply()
                }

                applyTheme(theme)
            }
            else{
                switchToDefaultTheme()
            }
        }

        matYouSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                val theme = context.getString(R.string.materialyou_theme)

                with(sharedPrefs.edit()) {
                    putString(SharedPrefs.THEMEKEY, theme)
                    apply()
                }

                applyTheme(theme)
            }
            else{
                switchToDefaultTheme()
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            youColorsChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                when(checkedIds[0]){
                    R.id.matYouChipOne -> applyMaterialYouTheme(SharedPrefs.YOUSTYLE1)
                    R.id.matYouChipTwo -> applyMaterialYouTheme(SharedPrefs.YOUSTYLE2)
                    R.id.matYouChipThree -> applyMaterialYouTheme(SharedPrefs.YOUSTYLE3)
                    R.id.matYouChipFour -> applyMaterialYouTheme(SharedPrefs.YOUSTYLE4)
                }
            }
        }
    }

    private fun switchToDefaultTheme(){
        val checkedButtonId = themeToggleGroup.checkedButtonId
        val theme = when (checkedButtonId) {
            R.id.systemDefaultBt -> context.getString(R.string.system_theme)
            R.id.lightThBt -> context.getString(R.string.light_theme)
            R.id.darkThBt -> context.getString(R.string.dark_theme)
            else -> context.getString(R.string.system_theme)
        }

        with(sharedPrefs.edit()) {
            putString(SharedPrefs.THEMEKEY, theme)
            apply()
        }

        applyTheme(theme)
    }

    private fun applyMaterialYouTheme(matyoustyle: String){
        val theme = context.getString(R.string.materialyou_theme)

        with(sharedPrefs.edit()) {
            putString(SharedPrefs.THEMEKEY, theme)
            putString(SharedPrefs.CURRENTMATSTYLEKEY, matyoustyle)
            apply()
        }

        applyTheme(theme)
    }

    fun applyThemeAndRecreateActivities(context: Context) {
        (context as? Activity)?.runOnUiThread {
            val tracker = (context.applicationContext as YetCalc).activityTracker
            tracker.recreateAllActivities()
        }
    }

    private fun applyTheme(theme: String) {
        // Implement the logic to apply the theme based on the selected value
        when (theme) {
            context.getString(R.string.system_theme) -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            context.getString(R.string.light_theme) -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            context.getString(R.string.dark_theme) -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            context.getString(R.string.abyss_theme) -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            context.getString(R.string.materialyou_theme) -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        applyThemeAndRecreateActivities(context)
    }


}
