package yetzio.yetcalc.views

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.airbnb.paris.Paris
import yetzio.yetcalc.R
import yetzio.yetcalc.component.History
import yetzio.yetcalc.component.HistoryAdapter
import yetzio.yetcalc.databinding.ActivityHistoryBinding
import yetzio.yetcalc.model.HistoryItem
import yetzio.yetcalc.utils.setVibOnClick

class HistoryActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var histArrList: ArrayList<HistoryItem>

    private lateinit var delBt: Button
    private val histHandler = History()

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    lateinit var theme: String
    private var dark = false
    private var light = false

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
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.history_app_bar)
        if(light){
            toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_arrow_back_24light)
        }
        else{
            toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_arrow_back_24)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener { finish() }

        histHandler.ctx = applicationContext
        histArrList = histHandler.getHistoryItems()

        if(light){
            Paris.style(findViewById<TextView>(R.id.histbartitle)).apply(R.style.GenericTextLight)
            Paris.style(findViewById<Button>(R.id.histdelbutton)).apply(R.style.historyDelImgSrcStyleLight)
        }

        binding.histListview.adapter = HistoryAdapter(this, histArrList)
        val emptyAdapter = HistoryAdapter(this, ArrayList<HistoryItem>())

        delBt = findViewById(R.id.histdelbutton)
        delBt.setOnClickListener {
            setVibOnClick(applicationContext)
            histHandler.emptyDb()
            binding.histListview.adapter = emptyAdapter
        }

    }

    private fun initPrefs(){
        preferences = getSharedPreferences("CalcPrefs", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }
}