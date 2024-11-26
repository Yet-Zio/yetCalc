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

class DateFormatPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {
    private lateinit var firstRadioBt: MaterialRadioButton
    private lateinit var secondRadioBt: MaterialRadioButton
    private lateinit var thirdRadioBt: MaterialRadioButton
    private lateinit var fourthRadioBt: MaterialRadioButton
    private lateinit var fifthRadioBt: MaterialRadioButton
    private lateinit var sixthRadioBt: MaterialRadioButton

    private val sharedPrefs = context.getDefSharedPrefs()
    private lateinit var prefFormat: String
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var dateFmtExampleView: MaterialTextView
    private val mYear = "2024"
    private val mMonth = "04"
    private val mDay = "15"

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        firstRadioBt = holder.findViewById(R.id.dtFirstFormat) as MaterialRadioButton
        secondRadioBt = holder.findViewById(R.id.dtSecondFormat) as MaterialRadioButton
        thirdRadioBt = holder.findViewById(R.id.dtThirdFormat) as MaterialRadioButton
        fourthRadioBt = holder.findViewById(R.id.dtFourthFormat) as MaterialRadioButton
        fifthRadioBt = holder.findViewById(R.id.dtFifthFormat) as MaterialRadioButton
        sixthRadioBt = holder.findViewById(R.id.dtSixthFormat) as MaterialRadioButton
        editor = sharedPrefs.edit()
        prefFormat = sharedPrefs.getString(SharedPrefs.DATEFMTKEY, "YYYY-MM-DD").toString()

        dateFmtExampleView = holder.findViewById(R.id.dateFmtExampleView) as MaterialTextView

        updateExampleView(prefFormat)
        setupPrefRadios()
    }

    private fun formatDate(dayormonth: String): String {
        return if (dayormonth.length > 1) dayormonth else "0$dayormonth"
    }

    private fun updateExampleView(prefFormat: String){
        dateFmtExampleView.text = when (prefFormat) {
            "DD-MM-YYYY" -> "${formatDate(mDay.toString())}-${formatDate(mMonth.toString())}-$mYear"
            "DD-YYYY-MM" -> "${formatDate(mDay.toString())}-$mYear-${formatDate(mMonth.toString())}"
            "YYYY-DD-MM" -> "$mYear-${formatDate(mDay.toString())}-${formatDate(mMonth.toString())}"
            "MM-DD-YYYY" -> "${formatDate(mMonth.toString())}-${formatDate(mDay.toString())}-$mYear"
            "MM-YYYY-DD" -> "${formatDate(mMonth.toString())}-$mYear-${formatDate(mDay.toString())}"
            else -> "$mYear-${formatDate(mMonth.toString())}-${formatDate(mDay.toString())}"
        }
    }

    private fun setupPrefRadios(){
        when(prefFormat){
            "YYYY-MM-DD" -> {
                firstRadioBt.isChecked = true
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = false
                fourthRadioBt.isChecked = false
                fifthRadioBt.isChecked = false
                sixthRadioBt.isChecked = false
            }
            "DD-MM-YYYY" -> {
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = true
                thirdRadioBt.isChecked = false
                fourthRadioBt.isChecked = false
                fifthRadioBt.isChecked = false
                sixthRadioBt.isChecked = false
            }
            "DD-YYYY-MM" -> {
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = true
                fourthRadioBt.isChecked = false
                fifthRadioBt.isChecked = false
                sixthRadioBt.isChecked = false
            }
            "YYYY-DD-MM" -> {
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = false
                fourthRadioBt.isChecked = true
                fifthRadioBt.isChecked = false
                sixthRadioBt.isChecked = false
            }
            "MM-DD-YYYY" -> {
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = false
                fourthRadioBt.isChecked = false
                fifthRadioBt.isChecked = true
                sixthRadioBt.isChecked = false
            }
            "MM-YYYY-DD" -> {
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = false
                fourthRadioBt.isChecked = false
                fifthRadioBt.isChecked = false
                sixthRadioBt.isChecked = true
            }
        }

        firstRadioBt.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                firstRadioBt.isChecked = true
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = false
                fourthRadioBt.isChecked = false
                fifthRadioBt.isChecked = false
                sixthRadioBt.isChecked = false
            }

            editor.putString(SharedPrefs.DATEFMTKEY, "YYYY-MM-DD")
            editor.apply()
            updateExampleView("YYYY-MM-DD")
        }

        secondRadioBt.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = true
                thirdRadioBt.isChecked = false
                fourthRadioBt.isChecked = false
                fifthRadioBt.isChecked = false
                sixthRadioBt.isChecked = false
            }

            editor.putString(SharedPrefs.DATEFMTKEY, "DD-MM-YYYY")
            editor.apply()
            updateExampleView("DD-MM-YYYY")
        }

        thirdRadioBt.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = true
                fourthRadioBt.isChecked = false
                fifthRadioBt.isChecked = false
                sixthRadioBt.isChecked = false
            }

            editor.putString(SharedPrefs.DATEFMTKEY, "DD-YYYY-MM")
            editor.apply()
            updateExampleView("DD-YYYY-MM")
        }

        fourthRadioBt.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = false
                fourthRadioBt.isChecked = true
                fifthRadioBt.isChecked = false
                sixthRadioBt.isChecked = false
            }

            editor.putString(SharedPrefs.DATEFMTKEY, "YYYY-DD-MM")
            editor.apply()
            updateExampleView("YYYY-DD-MM")
        }

        fifthRadioBt.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = false
                fourthRadioBt.isChecked = false
                fifthRadioBt.isChecked = true
                sixthRadioBt.isChecked = false
            }

            editor.putString(SharedPrefs.DATEFMTKEY, "MM-DD-YYYY")
            editor.apply()
            updateExampleView("MM-DD-YYYY")
        }

        sixthRadioBt.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                firstRadioBt.isChecked = false
                secondRadioBt.isChecked = false
                thirdRadioBt.isChecked = false
                fourthRadioBt.isChecked = false
                fifthRadioBt.isChecked = false
                sixthRadioBt.isChecked = true
            }

            editor.putString(SharedPrefs.DATEFMTKEY, "MM-YYYY-DD")
            editor.apply()
            updateExampleView("MM-YYYY-DD")
        }
    }
}