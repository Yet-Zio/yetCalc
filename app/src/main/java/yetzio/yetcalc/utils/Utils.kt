package yetzio.yetcalc.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.aboutlibraries.LibsBuilder.Companion.BUNDLE_EDGE_TO_EDGE
import com.mikepenz.aboutlibraries.LibsBuilder.Companion.BUNDLE_SEARCH_ENABLED
import com.mikepenz.aboutlibraries.LibsBuilder.Companion.BUNDLE_TITLE
import yetzio.yetcalc.R
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.component.getDefSharedPrefs
import yetzio.yetcalc.enums.UnitType
import yetzio.yetcalc.views.YetLibsActivity


fun Context.getThemeColor(@AttrRes attributeId: Int): Int {
    val typedArray = theme.obtainStyledAttributes(intArrayOf(attributeId))
    val color = typedArray.getColor(0, 0)
    typedArray.recycle()
    return color
}

fun getScreenOrientation(ctx: Context): Int{
    val orientation = ctx.resources.configuration.orientation
    return orientation
}

fun setVibOnClick(ctx: Context){
    val prefs = ctx.getDefSharedPrefs()
    val hapticPref = prefs.getBoolean(SharedPrefs.HAPTICKEY, true)

    if(hapticPref){
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            ctx.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
        }

        if(vibrator.hasVibrator()){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                vibrator.vibrate(VibrationEffect.createOneShot(40, 10))
            }
            else{
                @Suppress("DEPRECATION")
                vibrator.vibrate(10)
            }
        }
    }
}

fun isNetworkAvailable(ctx: Context?): Boolean{
    val connectivityManager = ctx?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork
        val networkTransport = connectivityManager.getNetworkCapabilities(networkCapabilities)

        networkTransport?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo
        networkInfo != null && networkInfo.isConnected
    }
}

// F indicating FrameLayout

fun showLayoutWithAnimationF(layout: FrameLayout, dropLineContainer: LinearLayout) {
    layout.visibility = View.VISIBLE

    layout.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            layout.viewTreeObserver.removeOnPreDrawListener(this)
            val startPosition = dropLineContainer.bottom.toFloat()
            layout.translationY = startPosition
            animateLayoutShowingF(layout)
            return true
        }
    })
}

fun animateLayoutShowingF(layout: FrameLayout) {
    val animator = ObjectAnimator.ofFloat(layout, "translationY", 0f)
    animator.duration = 300
    animator.interpolator = DecelerateInterpolator()
    animator.start()
}

// Duration should be higher here for giving a sensation of the view dropping but not at the same time.
fun hideLayoutWithAnimationF(layout: FrameLayout, dropLineContainer: LinearLayout, mainbuttonlyt: FrameLayout, droppedBtnLyt: FrameLayout) {
    val endPosition = dropLineContainer.bottom.toFloat()
    val animator = ObjectAnimator.ofFloat(layout, "translationY", endPosition)
    animator.duration = 400
    animator.interpolator = AccelerateInterpolator()

    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) {
            layout.visibility = View.GONE
            if (layout.id == droppedBtnLyt.id) {
                mainbuttonlyt.visibility = View.VISIBLE
            } else {
                droppedBtnLyt.visibility = View.VISIBLE
            }
        }

        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })

    animator.start()
}

fun Context.copyToClipboard(str: String){
    if(str.isNotEmpty()){
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("Copied Text", str))
        Toast.makeText(this, "Copied Text to Clipboard", Toast.LENGTH_SHORT).show()
    }
}

fun Context.getUnitsList(unitType: UnitType) : ArrayList<String>{
    return when(unitType){
        UnitType.CURRENCY -> resources.getStringArray(R.array.currencies_one).toCollection(ArrayList())
        UnitType.ANGLE -> resources.getStringArray(R.array.anglelist).toCollection(ArrayList())
        UnitType.AREA -> resources.getStringArray(R.array.arealist).toCollection(ArrayList())
        UnitType.DATA -> resources.getStringArray(R.array.datalist).toCollection(ArrayList())
        UnitType.ENERGY -> resources.getStringArray(R.array.energylist).toCollection(ArrayList())
        UnitType.LENGTH -> resources.getStringArray(R.array.lengthlist).toCollection(ArrayList())
        UnitType.POWER -> resources.getStringArray(R.array.powerlist).toCollection(ArrayList())
        UnitType.PRESSURE -> resources.getStringArray(R.array.pressurelist).toCollection(ArrayList())
        UnitType.SPEED -> resources.getStringArray(R.array.speedlist).toCollection(ArrayList())
        UnitType.TEMPERATURE -> resources.getStringArray(R.array.temperaturelist).toCollection(ArrayList())
        UnitType.TIME -> resources.getStringArray(R.array.timelist).toCollection(ArrayList())
        UnitType.VOLUME -> resources.getStringArray(R.array.volumelist).toCollection(ArrayList())
        UnitType.WEIGHT -> resources.getStringArray(R.array.weightormasslist).toCollection(ArrayList())
    }
}

fun doesChipGroupContain(chipGroup: ChipGroup, chipId: Int): Boolean {
    return chipGroup.findViewById<Chip>(chipId) != null
}

fun LibsBuilder.startYetLibIntent(ctx: Context){
    val i = yetLibIntent(ctx)
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    ctx.startActivity(i)
}

fun LibsBuilder.yetLibIntent(ctx: Context): Intent{
    val i = Intent(ctx, YetLibsActivity::class.java)
    i.putExtra("data", this)

    if (this.activityTitle != null) {
        i.putExtra(BUNDLE_TITLE, this.activityTitle)
    }
    i.putExtra(BUNDLE_EDGE_TO_EDGE, this.edgeToEdge)
    i.putExtra(BUNDLE_SEARCH_ENABLED, this.searchEnabled)

    return i
}