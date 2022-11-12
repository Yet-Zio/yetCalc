package yetzio.yetcalc.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat
import com.airbnb.paris.Paris
import yetzio.yetcalc.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var theme: String

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var toolbar: Toolbar
    private lateinit var stTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        initThemePrefs()
        theme = preferences.getString(getString(R.string.key_theme), getString(R.string.dark_theme)).toString()
        if(theme == getString(R.string.dark_theme)){
            setTheme(R.style.DarkSettings)
        }
        else{
            setTheme(R.style.LightSettings)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settingslyt, SettingsFragment())
                .commit()
        }

        toolbar = findViewById(R.id.settings_app_bar)
        stTitle = toolbar.findViewById(R.id.settingstitle)

        if (theme == getString(R.string.dark_theme)){
            toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_arrow_back_24)
        }
        else{
            Paris.style(stTitle).apply(R.style.GenericTextLight)
            toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_arrow_back_24light)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            val fiIntent = Intent()
            setResult(RESULT_OK, fiIntent)
            finish()
        }
    }

    fun initThemePrefs(){
        preferences = getSharedPreferences("CalcPrefs", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}