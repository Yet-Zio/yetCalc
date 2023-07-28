package yetzio.yetcalc.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.airbnb.paris.Paris
import yetzio.yetcalc.R

class AboutActivity : AppCompatActivity() {

    private lateinit var theme: String
    private var dark = false
    private var light = false

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var toolbar: Toolbar
    private lateinit var abtTitle: TextView
    private lateinit var ghbTV: TextView
    private lateinit var ghbIV: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        initThemePrefs()
        theme = preferences.getString(getString(R.string.key_theme), getString(R.string.system_theme)).toString()

        if(theme == getString(R.string.system_theme)){
            val nightModeFlags: Int = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    dark = true
                    light = false
                    setTheme(R.style.AboutDark)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    dark = false
                    light = true
                    setTheme(R.style.AboutLight)
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    dark = true
                    light = false
                    setTheme(R.style.AboutDark)
                }
            }
        }
        else if(theme == getString(R.string.dark_theme)){
            dark = true
            light = false
            setTheme(R.style.AboutDark)
        }
        else{
            dark = false
            light = true
            setTheme(R.style.AboutLight)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        toolbar = findViewById(R.id.about_app_bar)
        abtTitle = toolbar.findViewById(R.id.abouttitle)

        if (dark){
            toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_arrow_back_24)
        }
        else{
            Paris.style(abtTitle).apply(R.style.GenericTextLight)
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

        ghbTV = findViewById(R.id.githubTV)
        ghbIV = findViewById(R.id.githubIV)

        if(light){
            Paris.style(ghbTV).apply(R.style.AboutTextLight)
            Paris.style(ghbIV).apply(R.style.ghbImgSrcLight)
        }

        val ghburi = Uri.parse("https://github.com/Yet-Zio/yetCalc")

        ghbTV.setOnClickListener{
            val ghbIntent = Intent(Intent.ACTION_VIEW, ghburi)
            startActivity(ghbIntent)
        }

        ghbIV.setOnClickListener{
            val ghbIntent = Intent(Intent.ACTION_VIEW, ghburi)
            startActivity(ghbIntent)
        }
    }

    fun initThemePrefs(){
        preferences = getSharedPreferences("CalcPrefs", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }
}