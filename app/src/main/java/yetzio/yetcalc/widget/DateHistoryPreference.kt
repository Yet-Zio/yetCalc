package yetzio.yetcalc.widget

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textview.MaterialTextView
import yetzio.yetcalc.R
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.component.getDefSharedPrefs

class DateHistoryPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs)  {

    private lateinit var firstRadioBt: MaterialRadioButton
    private lateinit var secondRadioBt: MaterialRadioButton
    private lateinit var thirdRadioBt: MaterialRadioButton

    private val sharedPrefs = context.getDefSharedPrefs()
    private lateinit var prefFormat: String
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var dateHistExampleView: MaterialTextView
    private val demoDate = "Nov 23, 2024, 1:26:07 AM"

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        firstRadioBt = holder.findViewById(R.id.firstFormat) as MaterialRadioButton
        secondRadioBt = holder.findViewById(R.id.secondFormat) as MaterialRadioButton
        thirdRadioBt = holder.findViewById(R.id.thirdFormat) as MaterialRadioButton
        editor = sharedPrefs.edit()
        prefFormat = sharedPrefs.getString(SharedPrefs.DATEHISTKEY, "Default format").toString()

        dateHistExampleView = holder.findViewById(R.id.dateHistExampleView) as MaterialTextView

        updateExampleView(prefFormat)
        setupPrefRadios()
    }

    fun updateExampleView(format: String) {
        try {
            val formatter = when (format) {
                "Default format" -> {
                    java.time.format.DateTimeFormatter.ofLocalizedDateTime(java.time.format.FormatStyle.MEDIUM)
                }
                "dd, MMMM yyyy HH:mm:ss" -> {
                    java.time.format.DateTimeFormatter.ofPattern("dd, MMMM yyyy HH:mm:ss")
                }
                "yyyy, MMMM dd HH:mm:ss" -> {
                    java.time.format.DateTimeFormatter.ofPattern("yyyy, MMMM dd HH:mm:ss")
                }
                else -> throw IllegalArgumentException("Unknown format: $format")
            }

            // Updated pattern for parsing the demo date
            val parsedDate = java.time.LocalDateTime.parse(
                demoDate,
                java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy, h:mm:ss a")
            )
            dateHistExampleView.text = parsedDate.format(formatter)
        } catch (e: Exception) {
            dateHistExampleView.text = "Error formatting date"
            e.printStackTrace() // Optional: Log the error to understand the issue
        }
    }

    private fun setupPrefRadios(){
        when(prefFormat){
            "Default format" -> {
                firstRadioBt.isChecked = true
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = false
            }
            "dd, MMMM yyyy HH:mm:ss" -> {
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = true
                thirdRadioBt.isChecked = false
            }
            "yyyy, MMMM dd HH:mm:ss" -> {
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = true
            }
        }

        firstRadioBt.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                firstRadioBt.isChecked = true
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = false

                editor.putString(SharedPrefs.DATEHISTKEY, "Default format")
                editor.apply()
                updateExampleView("Default format")
            }
        }

        secondRadioBt.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = true
                thirdRadioBt.isChecked = false

                editor.putString(SharedPrefs.DATEHISTKEY, "dd, MMMM yyyy HH:mm:ss")
                editor.apply()
                updateExampleView("dd, MMMM yyyy HH:mm:ss")
            }
        }

        thirdRadioBt.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = true

                editor.putString(SharedPrefs.DATEHISTKEY, "yyyy, MMMM dd HH:mm:ss")
                editor.apply()
                updateExampleView("yyyy, MMMM dd HH:mm:ss")
            }
        }
    }
}