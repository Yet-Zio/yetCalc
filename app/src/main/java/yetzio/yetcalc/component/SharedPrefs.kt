package yetzio.yetcalc.component

import android.content.Context
import android.content.SharedPreferences

object SharedPrefs {
    // CONFIG
    const val PREF_NAME = "CalcPrefs"
    const val MODE = Context.MODE_PRIVATE

    // KEYS
    const val THEMEKEY = "chosenTheme"
    const val ABYSSENCOUNTEREDKEY = "abyssEncounter"

    const val DATEHISTKEY = "datehistkey"
    const val ALMINTKEY = "almostintkey"
    const val CANINTKEY = "canonintkey"
    const val PRECKEY = "precisionkey"
    const val HAPTICKEY = "hapticfdkey"
    const val LEFTJUSTRES = "leftjustifyres"
    const val LEFTJUSTPGRES = "leftjustifypgres"
    const val DATEFMTKEY = "dateFormatKey"

    // CONVERTER KEYS
    const val SAVE_RECENT_TABKEY = "saverecenttabkey"
    const val RECENT_TAB_VALUEKEY = "recentTabValueKey"
    const val SAVE_RECENT_UNITKEY = "saverecentunitkey"

    // CONVERTER - Currency
    const val CURRENCY_FIRST = "currencyft"
    const val CURRENCY_SECOND = "currencysd"

    // CONVERTER - Angle
    const val ANGLE_FIRST = "angleft"
    const val ANGLE_SECOND = "anglesd"

    // CONVERTER - Area
    const val AREA_FIRST = "areaft"
    const val AREA_SECOND = "areasd"

    // CONVERTER - Data
    const val DATA_FIRST = "dataft"
    const val DATA_SECOND = "datasd"

    // CONVERTER - Energy
    const val ENERGY_FIRST = "energyft"
    const val ENERGY_SECOND = "energysd"

    // CONVERTER - Length
    const val LENGTH_FIRST = "lengthft"
    const val LENGTH_SECOND = "lengthsd"

    // CONVERTER - Power
    const val POWER_FIRST = "powerft"
    const val POWER_SECOND = "powersd"

    // CONVERTER - Pressure
    const val PRESSURE_FIRST = "pressureft"
    const val PRESSURE_SECOND = "pressuresd"

    // CONVERTER - Speed
    const val SPEED_FIRST = "speedft"
    const val SPEED_SECOND = "speedsd"

    // CONVERTER - Temperature
    const val TEMPERATURE_FIRST = "temperatureft"
    const val TEMPERATURE_SECOND = "temperaturesd"

    // CONVERTER - Time
    const val TIME_FIRST = "timeft"
    const val TIME_SECOND = "timesd"

    // CONVERTER - Volume
    const val VOLUME_FIRST = "volumeft"
    const val VOLUME_SECOND = "volumesd"

    // CONVERTER - Weight
    const val WEIGHT_FIRST = "weightft"
    const val WEIGHT_SECOND = "weightsd"

    // Material You Styles
    const val CURRENTMATSTYLEKEY = "MATSTYLEKEY"

    const val YOUSTYLE1 = "youstyle1"

    const val YOUSTYLE2 = "youstyle2"

    const val YOUSTYLE3 = "youstyle3"

    const val YOUSTYLE4 = "youstyle4"

    // PREFERRED MODE
    const val PREFMODEKEY = "prefmodekey"

    const val SHORTCUTSADDED = "SHORTADDKEY"
    const val REFRESH_SHORTCUT = "REFRESHSHORTCUTKEY"

    const val UNITGROUPPREF = "UNITGROUPPREF"
    const val KEY_ENABLED_GROUPS = "enabled_groups"
    const val KEY_DISABLED_GROUPS = "disabled_groups"
}

fun Context.getDefSharedPrefs() : SharedPreferences{
    return getSharedPreferences(SharedPrefs.PREF_NAME, SharedPrefs.MODE)
}