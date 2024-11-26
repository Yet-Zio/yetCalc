package yetzio.yetcalc.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import yetzio.yetcalc.R
import yetzio.yetcalc.models.UnitGroup
import java.util.Collections

class UnitGroupAdapter(
    private val unitGroups: MutableList<UnitGroup>,
    private val listener: OnItemActionListener?,
    private val dragListener: OnStartDragListener
) : RecyclerView.Adapter<UnitGroupAdapter.ViewHolder>() {

    interface OnItemActionListener {
        fun onToggleClicked(group: UnitGroup, position: Int)
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: ViewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_unit_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = unitGroups[position]
        holder.nameTextView.text = group.name
        holder.toggleButton.setImageResource(
            if (group.enabled) R.drawable.round_remove_circle_24 else R.drawable.round_add_circle_24
        )

        holder.toggleButton.setOnClickListener {
            listener?.onToggleClicked(group, holder.adapterPosition)
        }

        if(!group.enabled){
            holder.dragHandle.visibility = View.GONE
        }
        else{
            holder.dragHandle.visibility = View.VISIBLE
        }

        holder.dragHandle.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    dragListener.onStartDrag(holder)
                    holder.unitGroupLytContainer.setBackgroundResource(R.drawable.unitgroupbg_dragged)
                }
            }
            false
        }
    }

    override fun getItemCount(): Int = unitGroups.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val toggleButton: ImageButton = itemView.findViewById(R.id.toggleButton)
        val dragHandle: ImageView = itemView.findViewById(R.id.dragHandle)
        val unitGroupLytContainer: LinearLayout = itemView.findViewById(R.id.unitGroupLytContainer)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(unitGroups, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun getUnitGroups(): List<UnitGroup> = unitGroups
}