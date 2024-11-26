package yetzio.yetcalc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import yetzio.yetcalc.R
import yetzio.yetcalc.utils.getThemeColor

class ModeSelectAdapter(context: Context, private val items: List<Pair<String, Int>>) :
    ArrayAdapter<Pair<String, Int>>(context, R.layout.list_item, items){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val item = getItem(position)

        val textview = view.findViewById<TextView>(R.id.modeselectorTV)
        val imageview = view.findViewById<ImageView>(R.id.modeselectorIV)

        textview.text = item?.first
        item?.second?.let { imageview.setImageResource(it) }
        imageview.setColorFilter(context.getThemeColor(R.attr.calcTextDefaultColor), android.graphics.PorterDuff.Mode.SRC_IN);

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getDropDownView(position, convertView, parent)
    }
}