package yetzio.yetcalc.component

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import yetzio.yetcalc.R
import yetzio.yetcalc.model.SpinnerItem

class SpinnerItemAdapter(private val context: Activity, private val spinList: ArrayList<SpinnerItem>, private val theme: String)
    : ArrayAdapter<SpinnerItem>(context, 0, spinList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = when(theme){
            "default" -> {
                inflater.inflate(R.layout.custom_spinner_layout, parent, false)
            }
            "light" -> {
                inflater.inflate(R.layout.custom_spinner_layoutlight, parent, false)
            }
            else -> {
                inflater.inflate(R.layout.custom_spinner_layout, parent, false)
            }
        }

        val item = getItem(position) as SpinnerItem
        val spinnerIV = view.findViewById<ImageView>(R.id.imSpinLyt)
        val spinnerTV = view.findViewById<TextView>(R.id.tvSpinLyt)

        spinnerIV.setImageResource(item.spinnerItemImage)
        spinnerTV.text = item.spinnerItemName

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = when (theme) {
            "default" -> {
                inflater.inflate(R.layout.custom_dropdown_layout, parent, false)
            }
            "light" -> {
                inflater.inflate(R.layout.custom_dropdown_layoutlight, parent, false)
            }
            else -> {
                inflater.inflate(R.layout.custom_dropdown_layout, parent, false)
            }
        }

        val item = getItem(position) as SpinnerItem
        val spinnerIV = view.findViewById<ImageView>(R.id.imspinDropLyt)
        val spinnerTV = view.findViewById<TextView>(R.id.tvspinDropLyt)

        spinnerIV.setImageResource(item.spinnerItemImage)
        spinnerTV.text = item.spinnerItemName

        return view
    }
}