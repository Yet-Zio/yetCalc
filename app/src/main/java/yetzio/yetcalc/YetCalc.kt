package yetzio.yetcalc

import android.app.Application
import yetzio.yetcalc.config.ActivityTracker

class YetCalc : Application() {
    lateinit var activityTracker: ActivityTracker

    override fun onCreate() {
        super.onCreate()
        activityTracker = ActivityTracker(applicationContext)
        registerActivityLifecycleCallbacks(activityTracker)
    }
}
