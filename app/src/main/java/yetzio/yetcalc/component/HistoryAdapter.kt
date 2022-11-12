package yetzio.yetcalc.component

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import yetzio.yetcalc.R
import yetzio.yetcalc.model.HistoryItem
import yetzio.yetcalc.utils.setVibOnClick
import yetzio.yetcalc.views.HistoryActivity

class HistoryAdapter(private val context: Activity, private val histList: ArrayList<HistoryItem>):
    ArrayAdapter<HistoryItem>(context, R.layout.history_list_item, histList){

    private var pTheme: String = (context as? HistoryActivity)?.theme.toString()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = if(pTheme == context.getString(R.string.light_theme)){
            inflater.inflate(R.layout.history_list_itemlight, null)
        } else{
            inflater.inflate(R.layout.history_list_item, null)
        }

        val timestampView = view.findViewById<TextView>(R.id.timestamptext)
        val expView = view.findViewById<TextView>(R.id.histexp)
        val resView = view.findViewById<TextView>(R.id.histres)

        timestampView.text = histList[position].timestamp
        expView.text = histList[position].expression
        resView.text = histList[position].result

        expView.setOnClickListener {
            setVibOnClick(context.applicationContext)
            val intent = Intent()
            intent.putExtra("expression", expView.text.toString())
            context.setResult(Activity.RESULT_OK, intent)
            context.finish()
        }

        resView.setOnClickListener{
            setVibOnClick(context.applicationContext)
            val intent = Intent()
            intent.putExtra("result", resView.text.toString())
            context.setResult(Activity.RESULT_OK, intent)
            context.finish()
        }

        return view
    }
}