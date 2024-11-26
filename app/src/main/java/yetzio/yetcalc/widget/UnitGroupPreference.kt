package yetzio.yetcalc.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import yetzio.yetcalc.R
import yetzio.yetcalc.adapters.UnitGroupAdapter
import yetzio.yetcalc.component.SharedPrefs
import yetzio.yetcalc.component.getDefSharedPrefs
import yetzio.yetcalc.models.UnitGroup

class UnitGroupPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs)  {

    private lateinit var touchHelper: ItemTouchHelper
    private lateinit var enabledAdapter: UnitGroupAdapter
    private lateinit var disabledAdapter: UnitGroupAdapter
    private val sharedPrefs = context.getDefSharedPrefs()

    private val deflist = "Currency,Length,Volume,Area,Weight/Mass,Temperature,Speed,Power,Energy,Pressure,Time,Angle,Data"

    private val enabledGroups = mutableListOf<UnitGroup>()
    private val disabledGroups = mutableListOf<UnitGroup>()

    private val dragListener = object : UnitGroupAdapter.OnStartDragListener {
        override fun onStartDrag(viewHolder: UnitGroupAdapter.ViewHolder) {
            touchHelper.startDrag(viewHolder)
        }
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        loadUnitGroups()
        setupRecyclerViews(holder)
    }

    private fun saveUnitGroups() {
        var enabledGroupNames = ""
        var disabledGroupNames = ""

        enabledGroups.forEachIndexed{ index, group ->
            val name = group.name
            println("saving: ${name}")
            enabledGroupNames += if(index > 0){
                ",$name"
            } else{
                name
            }

        }
        println("enabled groups: ${enabledGroupNames}")

        disabledGroups.forEachIndexed{ index, group ->
            val name = group.name
            disabledGroupNames += if(index > 0){
                ",$name"
            } else{
                name
            }
        }

        sharedPrefs.edit().apply {
            putString(SharedPrefs.KEY_ENABLED_GROUPS, enabledGroupNames)
            putString(SharedPrefs.KEY_DISABLED_GROUPS, disabledGroupNames)
            putBoolean(SharedPrefs.UNITGROUPPREF, true)
            apply()
        }
    }

    private fun loadUnitGroups(){
        val enabledGroupNames = sharedPrefs.getString(SharedPrefs.KEY_ENABLED_GROUPS, deflist)
        val disabledGroupNames = sharedPrefs.getString(SharedPrefs.KEY_DISABLED_GROUPS, "")

        val enabledGroupList = enabledGroupNames?.split(",") ?: listOf()
        val disabledGroupList = disabledGroupNames?.split(",") ?: listOf()

        if (enabledGroupList.isNotEmpty()) {
            enabledGroupList.forEach { enabledGroupName ->
                if (enabledGroupName.isNotEmpty()) {
                    enabledGroups.add(UnitGroup(enabledGroupName, true))
                }
            }
        }

        if (disabledGroupList.isNotEmpty()) {
            disabledGroupList.forEach { disabledGroupName ->
                if (disabledGroupName.isNotEmpty()) {
                    disabledGroups.add(UnitGroup(disabledGroupName, false))
                }
            }
        }
    }

    private fun setupRecyclerViews(holder: PreferenceViewHolder) {
        val enabledRecyclerView = holder.findViewById(R.id.enabledUnitRecyclerView) as RecyclerView
        val disabledRecyclerView = holder.findViewById(R.id.disabledUnitRecyclerView) as RecyclerView

        enabledAdapter = UnitGroupAdapter(
            unitGroups = enabledGroups,
            listener = object : UnitGroupAdapter.OnItemActionListener {
                override fun onToggleClicked(group: UnitGroup, position: Int) {
                    moveToDisabled(group)
                }
            },
            dragListener = dragListener
        )

        disabledAdapter = UnitGroupAdapter(
            unitGroups = disabledGroups,
            listener = object : UnitGroupAdapter.OnItemActionListener {
                override fun onToggleClicked(group: UnitGroup, position: Int) {
                    moveToEnabled(group)
                }
            },
            dragListener = dragListener
        )

        enabledRecyclerView.layoutManager = LinearLayoutManager(context)
        disabledRecyclerView.layoutManager = LinearLayoutManager(context)

        enabledRecyclerView.adapter = enabledAdapter
        disabledRecyclerView.adapter = disabledAdapter

        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition

                (recyclerView.adapter as? UnitGroupAdapter)?.onItemMove(fromPosition, toPosition)
                saveUnitGroups()
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // bruh
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)

                val backgroundContainer = viewHolder.itemView.findViewById<LinearLayout>(R.id.unitGroupLytContainer)
                backgroundContainer?.setBackgroundResource(R.drawable.unitgroupbg)
                saveUnitGroups()
            }
        }

        touchHelper = ItemTouchHelper(callback).apply {
            attachToRecyclerView(enabledRecyclerView)
        }
    }

    private fun moveToDisabled(group: UnitGroup) {
        enabledGroups.remove(group)
        group.enabled = false
        disabledGroups.add(group)
        enabledAdapter.notifyDataSetChanged()
        disabledAdapter.notifyDataSetChanged()
        saveUnitGroups()
    }

    private fun moveToEnabled(group: UnitGroup) {
        disabledGroups.remove(group)
        group.enabled = true
        enabledGroups.add(group)
        enabledAdapter.notifyDataSetChanged()
        disabledAdapter.notifyDataSetChanged()
        saveUnitGroups()
    }

}