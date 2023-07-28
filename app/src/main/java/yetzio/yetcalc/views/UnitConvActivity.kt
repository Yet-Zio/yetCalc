package yetzio.yetcalc.views

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
import android.widget.AdapterView
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import yetzio.yetcalc.MainActivity
import yetzio.yetcalc.R
import yetzio.yetcalc.component.SpinnerItemAdapter
import yetzio.yetcalc.model.ConverterPref
import yetzio.yetcalc.model.UnitConvViewModel
import yetzio.yetcalc.utils.getModesList
import yetzio.yetcalc.utils.showThemeDialog
import yetzio.yetcalc.views.fragments.*
import yetzio.yetcalc.views.fragments.adapters.ViewPagerAdapter
import java.io.File

class UnitConvActivity : AppCompatActivity() {
    val tabPrefName = "tabpref.json"
    val m_Mapper = jacksonObjectMapper()

    lateinit var viewPager: ViewPager2
    private lateinit var tabs: TabLayout
    private lateinit var toolbar: Toolbar

    lateinit var mViewModel: UnitConvViewModel

    private val titles = arrayListOf("Currency", "Length", "Volume",
        "Area", "Weight/Mass", "Temperature",
        "Speed", "Power", "Energy",
        "Pressure", "Time", "Angle",
        "Data")

    private lateinit var modeselecSpin: Spinner

    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var adp: ViewPagerAdapter

    lateinit var theme: String
    var dark = false
    var light = false

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

        if(light){
            setContentView(R.layout.activity_unit_convlight)
        }
        else{
            setContentView(R.layout.activity_unit_conv)
        }

        toolbar = findViewById(R.id.unitconvappbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mViewModel = ViewModelProvider(this)[UnitConvViewModel::class.java]

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
        modeselecSpin.setSelection(1)
        modeselecSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if(pos == 0){
                    saveTabPrefs()
                    startActivity(Intent(applicationContext, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                }
                else if(pos == 2){
                    saveTabPrefs()
                    startActivity(Intent(applicationContext, ProgramCalcActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        viewPager = findViewById(R.id.unitviewpager)
        tabs = findViewById(R.id.unittabslyt)
        //tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(applicationContext, R.color.lint))

        setUpTabLayout()

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

    private fun initTabPrefs(){
        val prefMgr = PreferenceManager.getDefaultSharedPreferences(this)
        val tabPrefer = prefMgr.getBoolean("lastusedtabkey", true)

        if(tabPrefer){
            val tabPrefFile = File(filesDir, tabPrefName)

            var tabPref = ConverterPref(0)

            if(tabPrefFile.exists()){
                val tabcontent = openFileInput(tabPrefName)?.bufferedReader().use {
                    it?.readText().toString()
                }

                try{
                    tabPref = m_Mapper.readValue(tabcontent, object: TypeReference<ConverterPref>(){})
                }
                catch (e: Exception){
                    println("tab preference read error occurred")
                }
            }

            tabs.selectTab(tabs.getTabAt(tabPref.tabnum))
        }
    }

    private fun saveTabPrefs(){
        val tabPrefFile = File(filesDir, tabPrefName)

        val currentTabPref = ConverterPref(tabs.selectedTabPosition)
        if(!tabPrefFile.exists()){
            tabPrefFile.createNewFile()
        }
        else{
            tabPrefFile.delete()
            tabPrefFile.createNewFile()
        }

        openFileOutput(tabPrefName, Context.MODE_PRIVATE or Context.MODE_APPEND).use {
            it?.write(m_Mapper.writeValueAsString(currentTabPref).toByteArray())
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

    private fun setUpTabLayout(){
        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(CurrencyFragment())
        adapter.addFragment(LengthFragment())
        adapter.addFragment(VolumeFragment())

        adapter.addFragment(AreaFragment())
        adapter.addFragment(WeightFragment())
        adapter.addFragment(TemperatureFragment())

        adapter.addFragment(SpeedFragment())
        adapter.addFragment(PowerFragment())
        adapter.addFragment(EnergyFragment())

        adapter.addFragment(PressureFragment())
        adapter.addFragment(TimeFragment())
        adapter.addFragment(AngleFragment())

        adapter.addFragment(DataFragment())
        viewPager.adapter = adapter
        adp = adapter

        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = titles[position]
        }.attach()

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_currency_exchange_60)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_straighten_60)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_local_drink_60)

        tabs.getTabAt(3)!!.setIcon(R.drawable.ic_baseline_area_chart_60)
        tabs.getTabAt(4)!!.setIcon(R.drawable.ic_baseline_monitor_weight_60)
        tabs.getTabAt(5)!!.setIcon(R.drawable.ic_baseline_thermostat_60)

        tabs.getTabAt(6)!!.setIcon(R.drawable.ic_baseline_speed_60)
        tabs.getTabAt(7)!!.setIcon(R.drawable.ic_baseline_bolt_60)
        tabs.getTabAt(8)!!.setIcon(R.drawable.ic_baseline_local_fire_department_60)

        tabs.getTabAt(9)!!.setIcon(R.drawable.ic_baseline_compress_60)
        tabs.getTabAt(10)!!.setIcon(R.drawable.ic_baseline_access_time_60)
        tabs.getTabAt(11)!!.setIcon(R.drawable.ic_baseline_incomplete_circle_60)

        tabs.getTabAt(12)!!.setIcon(R.drawable.ic_baseline_text_snippet_60)

        initTabPrefs()

    }

    override fun onPause() {
        saveTabPrefs()
        super.onPause()
    }

    override fun onStop() {
        saveTabPrefs()
        super.onStop()
    }

    override fun onDestroy() {
        saveTabPrefs()
        super.onDestroy()
    }
}