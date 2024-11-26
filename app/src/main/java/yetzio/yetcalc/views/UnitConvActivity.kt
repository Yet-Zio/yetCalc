package yetzio.yetcalc.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.HorizontalScrollView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import yetzio.yetcalc.R
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.config.CalcBaseActivity
import yetzio.yetcalc.config.CalcView
import yetzio.yetcalc.models.UnitConvRecentTab
import yetzio.yetcalc.models.UnitConvViewModel
import yetzio.yetcalc.models.UnitGroup
import yetzio.yetcalc.utils.doesChipGroupContain
import yetzio.yetcalc.views.fragments.AngleFragment
import yetzio.yetcalc.views.fragments.AreaFragment
import yetzio.yetcalc.views.fragments.CurrencyFragment
import yetzio.yetcalc.views.fragments.DataFragment
import yetzio.yetcalc.views.fragments.EnergyFragment
import yetzio.yetcalc.views.fragments.LengthFragment
import yetzio.yetcalc.views.fragments.PowerFragment
import yetzio.yetcalc.views.fragments.PressureFragment
import yetzio.yetcalc.views.fragments.SpeedFragment
import yetzio.yetcalc.views.fragments.TemperatureFragment
import yetzio.yetcalc.views.fragments.TimeFragment
import yetzio.yetcalc.views.fragments.VolumeFragment
import yetzio.yetcalc.views.fragments.WeightFragment
import kotlin.properties.Delegates

class UnitConvActivity : CalcBaseActivity() {
    var recentTab by Delegates.notNull<Boolean>()
    lateinit var lastTab: UnitConvRecentTab

    lateinit var mViewModel: UnitConvViewModel
    lateinit var unitChipsContainer: ChipGroup
    lateinit var horizontalScrollView: HorizontalScrollView
    private lateinit var settingsLauncher: ActivityResultLauncher<Intent>
    private lateinit var settingsBt: MaterialButton
    private var orderChanged: Boolean = false

    private val enabledGroups = mutableListOf<UnitGroup>()

    private val deflist = "Currency,Length,Volume,Area,Weight/Mass,Temperature,Speed,Power,Energy,Pressure,Time,Angle,Data"

    override fun onCreate(savedInstanceState: Bundle?) {
        currentView = CalcView.CONVERTER
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit_conv)

        modeSelector = findViewById(R.id.modeselector)
        setupModeSelector()

        mViewModel = ViewModelProvider(this)[UnitConvViewModel::class.java]
        settingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            orderChanged = preferences.getBoolean(SharedPrefs.UNITGROUPPREF, false)

            if(orderChanged){
                editor.putBoolean(SharedPrefs.UNITGROUPPREF, false)
                recreate()
            }
        }
        settingsBt = findViewById(R.id.settingsButton)
        settingsBt.setOnClickListener {
            launchSettings()
        }

        recentTab = preferences.getBoolean(SharedPrefs.SAVE_RECENT_TABKEY, true)
        lastTab = UnitConvRecentTab(preferences.getInt(SharedPrefs.RECENT_TAB_VALUEKEY, 0))
        horizontalScrollView = findViewById(R.id.horizontalChipContainer)

        loadUnitGroups()
        setupChipTabLayout()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val setIntent = Intent(Intent.ACTION_MAIN)
                setIntent.addCategory(Intent.CATEGORY_HOME)
                setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(setIntent)
            }
        })
    }

    private fun launchSettings(){
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        settingsLauncher.launch(settingsIntent)
    }

    private fun loadUnitGroups() {
        val enabledGroupNames = preferences.getString(SharedPrefs.KEY_ENABLED_GROUPS, deflist)
        val enabledGroupList = enabledGroupNames?.split(",") ?: listOf()

        if (enabledGroupList.isNotEmpty()) {
            enabledGroups.clear()
            enabledGroupList.forEach { enabledGroupName ->
                if(enabledGroupName.isNotEmpty()){
                    enabledGroups.add(UnitGroup(enabledGroupName, true))
                }
            }
        }
    }

    fun refreshCurrentFragment() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.unitFragmentContainer)
        currentFragment?.let {
            supportFragmentManager.beginTransaction()
                .detach(it)
                .commitNow()

            supportFragmentManager.beginTransaction()
                .attach(it)
                .commit()
        }
    }

    private fun initTabPrefs() {
        if (recentTab) {
            if(doesChipGroupContain(unitChipsContainer, lastTab.tabnum)){
                unitChipsContainer.check(lastTab.tabnum)
                mViewModel.currentCheckedChip = unitChipsContainer.checkedChipId
                loadCheckedChip(lastTab.tabnum)
            }
        }
    }

    private fun saveTabPrefs() {
        editor.putInt(SharedPrefs.RECENT_TAB_VALUEKEY, unitChipsContainer.checkedChipId)
        editor.apply()
    }

    private fun loadCheckedChip(checkedId: Int) {
        when (checkedId) {
            R.id.currencyChip -> loadFragment(CurrencyFragment())
            R.id.lengthChip -> loadFragment(LengthFragment())
            R.id.volChip -> loadFragment(VolumeFragment())
            R.id.areaChip -> loadFragment(AreaFragment())
            R.id.wmChip -> loadFragment(WeightFragment())
            R.id.tempChip -> loadFragment(TemperatureFragment())
            R.id.speedChip -> loadFragment(SpeedFragment())
            R.id.powerChip -> loadFragment(PowerFragment())
            R.id.energyChip -> loadFragment(EnergyFragment())
            R.id.pressureChip -> loadFragment(PressureFragment())
            R.id.timeChip -> loadFragment(TimeFragment())
            R.id.angleChip -> loadFragment(AngleFragment())
            R.id.dataChip -> loadFragment(DataFragment())
            else -> loadFragment(CurrencyFragment())
        }
    }

    private fun setupChipTabLayout() {
        unitChipsContainer = findViewById(R.id.unitChipGroup)
        unitChipsContainer.removeAllViews()

        enabledGroups.forEachIndexed { index, group ->
            val chipId = getChipIdForGroup(group.name)
            if (chipId != null) {
                val chip = layoutInflater.inflate(R.layout.unit_chip_item, unitChipsContainer, false) as Chip
                chip.id = chipId
                chip.text = group.name
                chip.isChecked = false
                chip.setChipIconResource(getIconForGroup(group.name))
                if (index == enabledGroups.lastIndex) {
                    val layoutParams = chip.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.marginEnd = resources.getDimensionPixelSize(R.dimen.last_chip_margin_end)
                    chip.layoutParams = layoutParams
                }
                unitChipsContainer.addView(chip)
            }
        }

        if (unitChipsContainer.checkedChipId == View.NO_ID) {
            if(unitChipsContainer.isNotEmpty()){
                val firstEnabledChip = unitChipsContainer.findViewById<Chip>(
                    unitChipsContainer.getChildAt(0).id
                )
                unitChipsContainer.check(firstEnabledChip.id)
                mViewModel.currentCheckedChip = unitChipsContainer.checkedChipId
                loadCheckedChip(firstEnabledChip.id)
            }
        }

        initTabPrefs()

        unitChipsContainer.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty() && checkedIds[0] != mViewModel.currentCheckedChip) {
                mViewModel.currentCheckedChip = checkedIds[0]
                loadCheckedChip(mViewModel.currentCheckedChip)
            } else {
                unitChipsContainer.check(mViewModel.currentCheckedChip)
            }
        }

        horizontalScrollView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                horizontalScrollView.viewTreeObserver.removeOnPreDrawListener(this)

                val selectedChipId = unitChipsContainer.checkedChipId

                if (selectedChipId != View.NO_ID) {
                    val selectedChip: Chip = findViewById(selectedChipId)

                    horizontalScrollView.post {
                        val chipLeftPosition = selectedChip.left
                        horizontalScrollView.smoothScrollTo(chipLeftPosition, 0)
                    }
                }

                return true
            }
        })
    }

    private fun getChipIdForGroup(groupName: String): Int? {
        return when (groupName) {
            "Currency" -> R.id.currencyChip
            "Length" -> R.id.lengthChip
            "Volume" -> R.id.volChip
            "Area" -> R.id.areaChip
            "Weight/Mass" -> R.id.wmChip
            "Temperature" -> R.id.tempChip
            "Speed" -> R.id.speedChip
            "Power" -> R.id.powerChip
            "Energy" -> R.id.energyChip
            "Pressure" -> R.id.pressureChip
            "Time" -> R.id.timeChip
            "Angle" -> R.id.angleChip
            "Data" -> R.id.dataChip
            else -> null
        }
    }

    private fun getIconForGroup(groupName: String): Int {
        return when (groupName) {
            "Currency" -> R.drawable.ic_currency_exchange_24
            "Length" -> R.drawable.ic_straighten_24
            "Volume" -> R.drawable.ic_local_drink_24
            "Area" -> R.drawable.ic_area_chart_24
            "Weight/Mass" -> R.drawable.ic_monitor_weight_24
            "Temperature" -> R.drawable.ic_thermostat_24
            "Speed" -> R.drawable.ic_speed_24
            "Power" -> R.drawable.ic_bolt_24
            "Energy" -> R.drawable.ic_energy_24
            "Pressure" -> R.drawable.ic_pressure_24
            "Time" -> R.drawable.ic_time_24
            "Angle" -> R.drawable.ic_incomplete_circle_24
            "Data" -> R.drawable.ic_text_snippet_24
            else -> R.drawable.ic_currency_exchange_24
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.unitFragmentContainer, fragment).commit()
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
