package yetzio.yetcalc.widget

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.slider.Slider
import com.google.android.material.textview.MaterialTextView
import yetzio.yetcalc.R
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.component.getDefSharedPrefs

class PrecisionPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    private lateinit var precisionExampleText: MaterialTextView
    private lateinit var precisionSlider: Slider
    private lateinit var selectedPrecisionText: MaterialTextView

    private val sharedPrefs = context.getDefSharedPrefs()

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        precisionExampleText = holder.findViewById(R.id.precisonExampleText) as MaterialTextView
        precisionSlider = holder.findViewById(R.id.precisionSlider) as Slider
        selectedPrecisionText = holder.findViewById(R.id.selectedPrecisionText) as MaterialTextView

        val precisionChoice = sharedPrefs.getString(SharedPrefs.PRECKEY, "Default precision")

        val precisionValues = context.resources.getStringArray(R.array.precision_array).toCollection(ArrayList())
        selectedPrecisionText.text = precisionChoice
        when(precisionChoice){
            "Default precision" -> {
                precisionSlider.value = 0F
            }
            "1e-60" -> {
                precisionSlider.value = 1F
            }
            "1e-99" -> {
                precisionSlider.value = 2F
            }
            "1e-323" -> {
                precisionSlider.value = 3F
            }
        }

        setPrecisionExampleText(precisionChoice!!)

        precisionSlider.addOnChangeListener { slider, value, _ ->
            val index = value.toInt()
            selectedPrecisionText.text = precisionValues[index]
            (selectedPrecisionText.text as String?)?.let { setPrecisionExampleText(it) }
            sharedPrefs.edit().apply{
                putString(SharedPrefs.PRECKEY, precisionValues[index])
                apply()
            }
        }
    }

    private fun setPrecisionExampleText(precision: String){
        when(precision){
            "Default precision" -> {
                precisionExampleText.text = context.getString(R.string.defaultprectext)
            }
            "1e-60" -> {
                precisionExampleText.text = context.getString(R.string.secondprectext)
            }
            "1e-99" -> {
                precisionExampleText.text = context.getString(R.string.thirdprectext)
            }
            "1e-323" -> {
                precisionExampleText.text = context.getString(R.string.fourthprectext)
            }
        }
    }
}