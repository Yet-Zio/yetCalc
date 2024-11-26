package yetzio.yetcalc.adapters

import yetzio.yetcalc.models.UnitItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import yetzio.yetcalc.R
import yetzio.yetcalc.models.UnitConvViewModel

class UnitConvSearchAdapter(private val originalList: ArrayList<String>, private val unitViewModel: UnitConvViewModel) :
    RecyclerView.Adapter<UnitConvSearchAdapter.ViewHolder>() {

    private var filteredList: List<UnitItem> = originalList.mapIndexed { index, value -> UnitItem(value, index) }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val unitTextContainer: LinearLayout = itemView.findViewById(R.id.unitTextContainer)
        val textView: MaterialTextView = itemView.findViewById(R.id.unitTextView)
        val actionButton: MaterialButton = itemView.findViewById(R.id.pinItemButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.uc_unit_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredList[position]
        holder.textView.text = item.value

        holder.unitTextContainer.setOnClickListener {
            val originalIndex = item.originalIndex
            unitViewModel.first.value?.let { it1 ->
                unitViewModel.setUnitPosition(
                    it1,
                    unitViewModel.currentUnit.value,
                    originalIndex,
                    holder.textView.text.toString())
            }
        }

        holder.actionButton.setOnClickListener {
            val newPin = holder.textView.text.toString()
            val updatedList = unitViewModel._localPinList.value ?: mutableSetOf()
            updatedList.add(newPin)
            unitViewModel.isChipAdded.value = true
            unitViewModel._localPinList.value = updatedList
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            originalList.mapIndexed { index, value -> UnitItem(value, index) }
        } else {
            originalList.mapIndexed { index, value -> UnitItem(value, index) }
                .filter { it.value.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }
}
