package yetzio.yetcalc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import yetzio.yetcalc.R
import yetzio.yetcalc.component.UnitConvPin
import yetzio.yetcalc.enums.UnitType
import yetzio.yetcalc.models.UnitConvViewModel

class UnitConvPinAdapter(private val ctx: Context, private val chipList: ArrayList<String>, private val originalList: ArrayList<String>, private val viewModel: UnitConvViewModel) : RecyclerView.Adapter<UnitConvPinAdapter.ChipViewHolder>() {

    class ChipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chip: Chip = itemView.findViewById(R.id.unitchip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chip, parent, false)
        return ChipViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val chipText = chipList[position]
        if (chipText.isNotEmpty()) {
            holder.chip.text = chipText
            holder.chip.isCloseIconVisible = true

            holder.chip.setOnClickListener {
                val originalIndex = originalList.indexOf(holder.chip.text.toString())

                viewModel.first.value?.let { it1 ->
                    viewModel.setUnitPosition(
                        it1,
                        viewModel.currentUnit.value,
                        originalIndex,
                        holder.chip.text.toString())
                }
            }

            holder.chip.setOnCloseIconClickListener {
                val m_pins = viewModel.currentUnit.value?.let { UnitConvPin.getConvPinStore(it) }
                m_pins?.ctx = ctx
                m_pins?.removeFromPinStore(holder.chip.text.toString())
                removeChip(position)
            }
        } else {
            holder.chip.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return chipList.size
    }

    fun addChip(chipText: String) {
        if (!chipList.contains(chipText)) {
            val m_pins = viewModel.currentUnit.value?.let { UnitConvPin.getConvPinStore(it) }
            m_pins?.ctx = ctx
            m_pins?.addToPinStore(chipText)
            chipList.add(chipText)
            notifyItemInserted(chipList.size - 1)
        }
    }

    fun removeChip(position: Int){
        if (position >= 0 && position < chipList.size) {
            chipList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, chipList.size)
        }
    }
}
