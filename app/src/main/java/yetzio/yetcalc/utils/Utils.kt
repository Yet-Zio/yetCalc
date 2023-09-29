package yetzio.yetcalc.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.afollestad.materialdialogs.MaterialDialog
import io.github.muddz.styleabletoast.StyleableToast
import yetzio.yetcalc.R
import yetzio.yetcalc.model.SpinnerItem
import java.util.*


fun showThemeDialog(ctx: Activity){
    val pref = ctx.getSharedPreferences("CalcPrefs", Context.MODE_PRIVATE)
    val ed = pref.edit()

    val theme = pref.getString(ctx.getString(R.string.key_theme), ctx.getString(R.string.system_theme)).toString()
    val dialog = MaterialDialog(ctx).noAutoDismiss()

    if(theme == ctx.getString(R.string.system_theme)){
        val nightModeFlags: Int = ctx.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                dialog.setContentView(R.layout.choose_theme)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                dialog.setContentView(R.layout.choose_themelight)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                dialog.setContentView(R.layout.choose_theme)
            }
        }
    } else if(theme == ctx.getString(R.string.light_theme)){
        dialog.setContentView(R.layout.choose_themelight)
    }
    else{
        dialog.setContentView(R.layout.choose_theme)
    }

    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    var prevTheme = false

    when (theme) {
        ctx.getString(R.string.system_theme) -> {
            dialog.findViewById<RadioGroup>(R.id.theme_group).check(R.id.systemRadioBt)
        }
        ctx.getString(R.string.dark_theme) -> {
            dialog.findViewById<RadioGroup>(R.id.theme_group).check(R.id.darkRadioBt)
        }
        else -> {
            dialog.findViewById<RadioGroup>(R.id.theme_group).check(R.id.lightRadioBt)
        }
    }

    dialog.findViewById<Button>(R.id.positive_button).setOnClickListener{
        val selectedTheme = dialog.findViewById<RadioButton>(dialog.findViewById<RadioGroup>(R.id.theme_group).checkedRadioButtonId)

        if(theme == selectedTheme.text.toString()){
            prevTheme = true
        }

        ed.putString(ctx.getString(R.string.key_theme), selectedTheme.text.toString())
        ed.apply()

        dialog.dismiss()

        if(!prevTheme){
            ctx.recreate()
        }

    }

    dialog.findViewById<Button>(R.id.negative_button).setOnClickListener{
        dialog.dismiss()
    }
    dialog.show()

}

fun getModesList(theme: String): ArrayList<SpinnerItem> {
    val resList = ArrayList<SpinnerItem>()
    if(theme == "light"){
        resList.add(SpinnerItem("Calculator", R.drawable.ic_baseline_calculate_24light))
        resList.add(SpinnerItem("Converter", R.drawable.ic_baseline_cyclone_24light))
        resList.add(SpinnerItem("Programmer", R.drawable.ic_baseline_code_24light))
    }
    else{
        resList.add(SpinnerItem("Calculator", R.drawable.ic_baseline_calculate_24))
        resList.add(SpinnerItem("Converter", R.drawable.ic_baseline_cyclone_24))
        resList.add(SpinnerItem("Programmer", R.drawable.ic_baseline_code_24))
    }

    return resList
}

fun getScreenOrientation(ctx: Context): Int{
    val orientation = ctx.resources.configuration.orientation
    return orientation
}

fun setVibOnClick(ctx: Context){
    val prefMgr = PreferenceManager.getDefaultSharedPreferences(ctx)
    val hapticPref = prefMgr.getBoolean("hapticfdkey", true)

    if(hapticPref){
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
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

fun gigaChad_EasterEgg(ctx: Context?){
    ctx?.let {
        StyleableToast.makeText(it, "GIGACHAD!", R.style.gigatoast).show()
    }
}