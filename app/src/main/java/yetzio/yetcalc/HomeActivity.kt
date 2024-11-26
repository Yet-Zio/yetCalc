package yetzio.yetcalc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.component.getDefSharedPrefs
import yetzio.yetcalc.config.ShortcutManager
import yetzio.yetcalc.views.CalculatorActivity
import yetzio.yetcalc.views.ProgramCalcActivity
import yetzio.yetcalc.views.UnitConvActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPrefs = getDefSharedPrefs()
        val shortcutAdded = sharedPrefs.getBoolean(SharedPrefs.SHORTCUTSADDED, false)

        if(!shortcutAdded){
            val shortcutManager = ShortcutManager(this)
            shortcutManager.addAllShortcuts()
        }

        val launcherActivity = when (sharedPrefs.getString(SharedPrefs.PREFMODEKEY, getString(R.string.calculator))) {
            getString(R.string.calculator) -> CalculatorActivity::class.java
            getString(R.string.converter) -> UnitConvActivity::class.java
            getString(R.string.programmer) -> ProgramCalcActivity::class.java
            else -> CalculatorActivity::class.java
        }

        startActivity(Intent(this, launcherActivity))
        finish()
    }
}