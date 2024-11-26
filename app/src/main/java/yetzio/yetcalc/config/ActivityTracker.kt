package yetzio.yetcalc.config

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.component.getDefSharedPrefs

class ActivityTracker(private val appContext: Context) : Application.ActivityLifecycleCallbacks {
    private val activeActivities = mutableListOf<Activity>()

    fun recreateAllActivities() {
        activeActivities.forEach { activity ->
            activity.recreate()
        }

        val sharedPrefs = appContext.getDefSharedPrefs()
        val refreshShorts = sharedPrefs.getBoolean(SharedPrefs.REFRESH_SHORTCUT, true)

        if(refreshShorts){
            val shortcuts = ShortcutManager(appContext)
            shortcuts.removeAllShortcuts()

            shortcuts.addAllShortcuts()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activeActivities.add(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        activeActivities.remove(activity)
    }
    
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
}
