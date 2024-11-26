package yetzio.yetcalc.views

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.android.material.color.DynamicColors
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.LibsActivity
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import yetzio.yetcalc.R
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.component.getDefSharedPrefs
import yetzio.yetcalc.utils.getThemeColor
import java.io.InputStreamReader
import kotlin.properties.Delegates

class YetLibsActivity : LibsActivity() {
    lateinit var theme: String
    lateinit var currentMatYouStyle: String
    var abyssEncounter by Delegates.notNull<Boolean>()
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = getDefSharedPrefs()
        editor = preferences.edit()

        theme = preferences.getString(SharedPrefs.THEMEKEY, getString(R.string.system_theme)).toString()
        abyssEncounter = preferences.getBoolean(SharedPrefs.ABYSSENCOUNTEREDKEY, false)
        currentMatYouStyle = preferences.getString(SharedPrefs.CURRENTMATSTYLEKEY, SharedPrefs.YOUSTYLE1).toString()

        when (theme) {
            getString(R.string.system_theme) -> {
                val nightModeFlags: Int = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                when (nightModeFlags) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        setTheme(R.style.Theme_YetCalc_Night)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        setTheme(R.style.Theme_YetCalc)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        setTheme(R.style.Theme_YetCalc_Night)
                    }
                }
            }
            getString(R.string.dark_theme) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                setTheme(R.style.Theme_YetCalc_Night)
            }
            getString(R.string.abyss_theme) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                setTheme(R.style.AbyssTheme)

                if(!abyssEncounter){
                    val abyssMessage = getString(R.string.messageFromAbyss)
                    DynamicToast.make(this, abyssMessage, ContextCompat.getDrawable(this, R.drawable.evil), getThemeColor(R.attr.calcTextDefaultColor), getThemeColor(R.attr.calcBackgroundDefault), Toast.LENGTH_LONG).show()
                    DynamicToast.make(this, abyssMessage, ContextCompat.getDrawable(this, R.drawable.evil), getThemeColor(R.attr.calcTextDefaultColor), getThemeColor(R.attr.calcBackgroundDefault), Toast.LENGTH_LONG).show()

                    with(editor) {
                        putBoolean(SharedPrefs.ABYSSENCOUNTEREDKEY, true)
                        apply()
                    }
                }
            }
            getString(R.string.light_theme) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setTheme(R.style.Theme_YetCalc)
            }
            getString(R.string.materialyou_theme) -> {
                val isDynamicColorEnabled = DynamicColors.isDynamicColorAvailable()
                if (isDynamicColorEnabled) {
                    when(currentMatYouStyle){
                        SharedPrefs.YOUSTYLE1 -> {
                            setTheme(R.style.MaterialYouTheme)
                        }
                        SharedPrefs.YOUSTYLE2 -> {
                            setTheme(R.style.YouStyle2)
                        }
                        SharedPrefs.YOUSTYLE3 -> {
                            setTheme(R.style.YouStyle3)
                        }
                        SharedPrefs.YOUSTYLE4 -> {
                            setTheme(R.style.YouStyle4)
                        }
                    }
                    DynamicColors.applyToActivitiesIfAvailable(applicationContext as Application)
                } else {
                    setTheme(R.style.Theme_YetCalc_Night)
                }
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                setTheme(R.style.Theme_YetCalc_Night)
            }
        }
        super.onCreate(savedInstanceState)
    }


    companion object{

        fun loadJsonFromRaw(context: Context, resId: Int): String {
            val inputStream = context.resources.openRawResource(resId)
            val reader = InputStreamReader(inputStream)
            val stringBuilder = StringBuilder()

            reader.forEachLine { stringBuilder.append(it) }
            reader.close()
            return stringBuilder.toString()
        }

        val rawContent = "Simplified BSD License\n" +
                "\n" +
                "Copyright 2010 - 2020 Mariusz Gromada. All rights reserved.\n" +
                "\n" +
                "You may use this software under the condition of Simplified BSD License.\n" +
                "Redistribution and use in source and binary forms, with or without\n" +
                "modification, are permitted provided that the following conditions are met:\n" +
                "\n" +
                "1. Redistributions of source code must retain the above copyright notice,\n" +
                "   this list of conditions and the following disclaimer.\n" +
                "2. Redistributions in binary form must reproduce the above copyright notice,\n" +
                "   this list of conditions and the following disclaimer in the documentation\n" +
                "   and/or other materials provided with the distribution.\n" +
                "\n" +
                "THIS SOFTWARE IS PROVIDED BY MARIUSZ GROMADA \"AS IS\" AND ANY EXPRESS\n" +
                "OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\n" +
                "OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\n" +
                "IN NO EVENT SHALL MARIUSZ GROMADA OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,\n" +
                "INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES\n" +
                "(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;\n" +
                "LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED\n" +
                "AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,\n" +
                "OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE\n" +
                "USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
                "\n" +
                "The views and conclusions contained in the software and documentation\n" +
                "are those of the authors and should not be interpreted as representing\n" +
                "official policies, either expressed or implied, of Mariusz Gromada.\n" +
                "\n" +
                "Mariusz Gromada, MathParser.org\n" +
                "e-mail: mariuszgromada.org@gmail.com\n" +
                "\n" +
                "Please visit:\n" +
                "\n" +
                "MathParser.org-mXparser:\n" +
                "- https://mathparser.org\n" +
                "- https://github.com/mariuszgromada/MathParser.org-mXparser\n" +
                "\n" +
                "Scalar - The Most Advanced Scientific Calculator:\n" +
                "- https://scalarmath.org\n" +
                "- https://play.google.com/store/apps/details?id=org.mathparser.scalar.pro\n" +
                "- https://play.google.com/store/apps/details?id=org.mathparser.scalar.lite\n" +
                "\n" +
                "Science Blog:\n" +
                "- https://mathspace.pl"

        fun fixIncorrectLicenseNotes(ctx: Context): Libs {
            val json = loadJsonFromRaw(ctx, R.raw.aboutlibraries)
            val gson = GsonBuilder().setPrettyPrinting().create()

            val rootObject: JsonObject = JsonParser.parseString(json).asJsonObject

            val librariesArray: JsonArray = rootObject.getAsJsonArray("libraries")

            librariesArray.forEach { libItem ->
                if(libItem.asJsonObject.get("uniqueId").asString == "org.mariuszgromada.math:MathParser.org-mXparser"){
                    val licenses = libItem.asJsonObject.get("licenses").asJsonArray
                    licenses.remove(0)
                    licenses.add("Simplified BSD License")
                }
            }

            val bsdlicense = JsonObject().apply {
                addProperty("hash", "Simplified BSD License")
                addProperty("internalHash", "Simplified BSD License")
                addProperty("spdxId", "BSD-2-Clause")
                addProperty("url", "https://github.com/mariuszgromada/MathParser.org-mXparser/blob/v.4.4.2/LICENSE.txt")
                addProperty("name", "Simplified BSD License")
                addProperty("content", rawContent)
            }

            val licensesObj: JsonObject = rootObject.getAsJsonObject("licenses")
            licensesObj.add("Simplified BSD License", bsdlicense)

            val updatedJsonString: String = gson.toJson(rootObject)

            return Libs.Builder().withJson(updatedJsonString).build()
        }

    }
}