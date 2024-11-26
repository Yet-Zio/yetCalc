package yetzio.yetcalc.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import yetzio.yetcalc.R
import yetzio.yetcalc.models.HistoryItem
import yetzio.yetcalc.utils.setVibOnClick

class HistoryAdapter(
    private val context: Activity,
    private val histList: ArrayList<HistoryItem>
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.history_list_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = histList[position]
        holder.bind(historyItem)
    }

    override fun getItemCount(): Int = histList.size

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timestampView: MaterialTextView = itemView.findViewById(R.id.timestamptext)
        private val expView: MaterialTextView = itemView.findViewById(R.id.histexp)
        private val resView: MaterialTextView = itemView.findViewById(R.id.histres)

        fun bind(historyItem: HistoryItem) {
            timestampView.text = historyItem.timestamp
            expView.text = historyItem.expression
            resView.text = historyItem.result

            expView.setOnClickListener {
                setVibOnClick(context.applicationContext)
                val intent = Intent().apply {
                    putExtra("expression", expView.text.toString())
                }
                context.setResult(Activity.RESULT_OK, intent)
                context.finish()
            }

            resView.setOnClickListener {
                setVibOnClick(context.applicationContext)
                val intent = Intent().apply {
                    putExtra("result", resView.text.toString())
                }
                context.setResult(Activity.RESULT_OK, intent)
                context.finish()
            }
        }
    }
}
