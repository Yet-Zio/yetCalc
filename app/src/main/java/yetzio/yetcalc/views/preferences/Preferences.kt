package yetzio.yetcalc.views.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.mikepenz.aboutlibraries.LibsBuilder
import yetzio.yetcalc.R
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.dialogs.showEasterEggDialog
import yetzio.yetcalc.models.EasterEggViewModel
import yetzio.yetcalc.utils.startYetLibIntent
import yetzio.yetcalc.views.SettingsActivity
import yetzio.yetcalc.views.YetLibsActivity.Companion.fixIncorrectLicenseNotes

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SharedPrefs.PREF_NAME
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference>("appearance")?.setOnPreferenceClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.settingslyt, AppearanceSettingsFragment())
                .addToBackStack(AppearanceSettingsFragment::class.java.name)
                .commit()
            true
        }

        findPreference<Preference>("general")?.setOnPreferenceClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.settingslyt, GeneralSettingsFragment())
                .addToBackStack(GeneralSettingsFragment::class.java.name)
                .commit()
            true
        }

        findPreference<Preference>("calculator")?.setOnPreferenceClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.settingslyt, CalculatorSettingsFragment())
                .addToBackStack(CalculatorSettingsFragment::class.java.name)
                .commit()
            true
        }

        findPreference<Preference>("unitconv")?.setOnPreferenceClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.settingslyt, UnitConvSettingsFragment())
                .addToBackStack(UnitConvSettingsFragment::class.java.name)
                .commit()
            true
        }

        findPreference<Preference>("programmer")?.setOnPreferenceClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.settingslyt, ProgrammerPreferenceFragment())
                .addToBackStack(ProgrammerPreferenceFragment::class.java.name)
                .commit()
            true
        }

        findPreference<Preference>("aboutkey")?.setOnPreferenceClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.settingslyt, AboutPreferenceFragment())
                .addToBackStack(AboutPreferenceFragment::class.java.name)
                .commit()
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireContext().getString(R.string.action_settings)
        (activity as? SettingsActivity)?.setTitle(title)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        // Apply icon tint for each preference
        preferenceScreen?.let { screen ->
            for (i in 0 until screen.preferenceCount) {
                (activity as? SettingsActivity)?.applyIconTint(screen.getPreference(i), requireContext())
            }
        }

        return view
    }

}

class AppearanceSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SharedPrefs.PREF_NAME
        setPreferencesFromResource(R.xml.appearance_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireContext().getString(R.string.appearancetitle)
        (activity as? SettingsActivity)?.setTitle(title)
    }
}

class GeneralSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SharedPrefs.PREF_NAME
        setPreferencesFromResource(R.xml.general_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireContext().getString(R.string.generaltitle)
        (activity as? SettingsActivity)?.setTitle(title)

        findPreference<Preference>("prefmode")?.setOnPreferenceClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.settingslyt, PrefModeSettingsFragment())
                .addToBackStack(PrefModeSettingsFragment::class.java.name)
                .commit()
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        // Apply icon tint for each preference
        preferenceScreen?.let { screen ->
            for (i in 0 until screen.preferenceCount) {
                (activity as? SettingsActivity)?.applyIconTint(screen.getPreference(i), requireContext())
            }
        }

        return view
    }
}

class PrefModeSettingsFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SharedPrefs.PREF_NAME
        setPreferencesFromResource(R.xml.prefmode, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireContext().getString(R.string.prefmodetitle)
        (activity as? SettingsActivity)?.setTitle(title)
    }
}

class UnitConvSettingsFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SharedPrefs.PREF_NAME
        setPreferencesFromResource(R.xml.unitconvpref, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireContext().getString(R.string.unitconvtitle)
        (activity as? SettingsActivity)?.setTitle(title)

        findPreference<Preference>("unitgroups")?.setOnPreferenceClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.settingslyt, UnitGroupsSettingsFragment())
                .addToBackStack(UnitGroupsSettingsFragment::class.java.name)
                .commit()
            true
        }

        findPreference<Preference>("dateFmt")?.setOnPreferenceClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.settingslyt, DateFmtSettingsFragment())
                .addToBackStack(DateFmtSettingsFragment::class.java.name)
                .commit()
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        // Apply icon tint for each preference
        preferenceScreen?.let { screen ->
            for (i in 0 until screen.preferenceCount) {
                (activity as? SettingsActivity)?.applyIconTint(screen.getPreference(i), requireContext())
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? SettingsActivity)?.expandAppBar()
    }
}

class UnitGroupsSettingsFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SharedPrefs.PREF_NAME
        setPreferencesFromResource(R.xml.unitgrouppref, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireContext().getString(R.string.unitgroupstitle)
        (activity as? SettingsActivity)?.setTitle(title)
        (activity as? SettingsActivity)?.collapseAppBar()
    }
}

class DateFmtSettingsFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SharedPrefs.PREF_NAME
        setPreferencesFromResource(R.xml.datefmtpref, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireContext().getString(R.string.dtmFormattitle)
        (activity as? SettingsActivity)?.setTitle(title)
    }
}

class CalculatorSettingsFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SharedPrefs.PREF_NAME
        setPreferencesFromResource(R.xml.calcpref, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireContext().getString(R.string.calculatortitle)
        (activity as? SettingsActivity)?.setTitle(title)

        findPreference<Preference>("precision")?.setOnPreferenceClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.settingslyt, PrecisionPreferenceFragment())
                .addToBackStack(PrecisionPreferenceFragment::class.java.name)
                .commit()
            true
        }

        findPreference<Preference>("datetimeformat")?.setOnPreferenceClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.settingslyt, DateHistoryPreferenceFragment())
                .addToBackStack(DateHistoryPreferenceFragment::class.java.name)
                .commit()
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        // Apply icon tint for each preference
        preferenceScreen?.let { screen ->
            for (i in 0 until screen.preferenceCount) {
                (activity as? SettingsActivity)?.applyIconTint(screen.getPreference(i), requireContext())
            }
        }

        return view
    }

}

class PrecisionPreferenceFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SharedPrefs.PREF_NAME
        setPreferencesFromResource(R.xml.precisionpref, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireContext().getString(R.string.precisiontitle)
        (activity as? SettingsActivity)?.setTitle(title)
    }
}

class DateHistoryPreferenceFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SharedPrefs.PREF_NAME
        setPreferencesFromResource(R.xml.dtmhistorypref, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireContext().getString(R.string.datehisttitle)
        (activity as? SettingsActivity)?.setTitle(title)
    }
}

class ProgrammerPreferenceFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SharedPrefs.PREF_NAME
        setPreferencesFromResource(R.xml.pgpref, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireContext().getString(R.string.programmertitle)
        (activity as? SettingsActivity)?.setTitle(title)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        // Apply icon tint for each preference
        preferenceScreen?.let { screen ->
            for (i in 0 until screen.preferenceCount) {
                (activity as? SettingsActivity)?.applyIconTint(screen.getPreference(i), requireContext())
            }
        }

        return view
    }
}

class AboutPreferenceFragment : PreferenceFragmentCompat() {
    private lateinit var mViewModel: EasterEggViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SharedPrefs.PREF_NAME
        setPreferencesFromResource(R.xml.aboutpref, rootKey)

        mViewModel = ViewModelProvider(((activity as? SettingsActivity)!!))[EasterEggViewModel::class.java]

        val libsWithFixes = context?.let { fixIncorrectLicenseNotes(it) }

        val licensePreference = findPreference<Preference>("osspref")
        licensePreference?.setOnPreferenceClickListener {
            context?.let { it1 ->
                LibsBuilder()
                    .withLibs(libsWithFixes!!)
                    .withActivityTitle("Open Source Licenses")
                    .withAboutIconShown(true)
                    .withAboutVersionShown(true)
                    .withAboutAppName("yetCalc")
                    .startYetLibIntent(it1)
            }
            true
        }

        val verionPref = findPreference<Preference>("versionpref")
        verionPref?.setOnPreferenceClickListener {
            mViewModel.clickCount.value = mViewModel.clickCount.value?.plus(1)
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireContext().getString(R.string.abouttitle)
        (activity as? SettingsActivity)?.setTitle(title)

        mViewModel.clickCount.observe(viewLifecycleOwner) { newValue ->
            if (newValue == 5) {
                mViewModel.clickCount.value = 0
                showEasterEggDialog(requireContext())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        // Apply icon tint for each preference
        preferenceScreen?.let { screen ->
            for (i in 0 until screen.preferenceCount) {
                (activity as? SettingsActivity)?.applyIconTint(screen.getPreference(i), requireContext())
            }
        }

        return view
    }
}