package yetzio.yetcalc.views

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import yetzio.yetcalc.R
import yetzio.yetcalc.config.CalcBaseActivity
import yetzio.yetcalc.utils.getThemeColor
import yetzio.yetcalc.views.preferences.SettingsFragment

class SettingsActivity : CalcBaseActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var collapseBar: CollapsingToolbarLayout
    private lateinit var appbarlyt: AppBarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settingslyt, SettingsFragment())
                .commit()
        }

        toolbar = findViewById(R.id.settingsMaterialToolbar)
        collapseBar = findViewById(R.id.settingsCollapseToolbar)
        appbarlyt = findViewById(R.id.settings_app_bar)

        // Handle the navigation button in the toolbar
        toolbar.setNavigationOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        }

    }

    fun setTitle(title: String) {
        toolbar.title = title
        collapseBar.title = title
    }

    fun collapseAppBar(){
        appbarlyt.setExpanded(false, true)
    }

    fun expandAppBar(){
        appbarlyt.post{
            appbarlyt.setExpanded(true, true)
        }
    }

    fun applyIconTint(preference: Preference, ctx: Context) {
        preference.icon?.let { icon ->
            val tintedIcon = icon.mutate()
            val tint = ctx.getThemeColor(R.attr.calcTextDefaultColor)
            tintedIcon.setTint(tint)
            preference.icon = tintedIcon
        }

        if (preference is PreferenceCategory) {
            for (i in 0 until preference.preferenceCount) {
                applyIconTint(preference.getPreference(i), ctx)
            }
        }
    }

}
