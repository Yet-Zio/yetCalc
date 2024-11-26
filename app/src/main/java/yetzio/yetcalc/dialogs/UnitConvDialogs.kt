package yetzio.yetcalc.dialogs

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import yetzio.yetcalc.R
import yetzio.yetcalc.adapters.UnitConvPinAdapter
import yetzio.yetcalc.adapters.UnitConvSearchAdapter
import yetzio.yetcalc.component.UnitConvPin
import yetzio.yetcalc.models.UnitConvViewModel
import yetzio.yetcalc.widget.CalcText

fun showUnitConvSearchDialog(ctx: Context, appctx: Context, itemsList: ArrayList<String>, viewModel: UnitConvViewModel, lifeCycleOwner: LifecycleOwner) : AlertDialog{
    val dialogView = LayoutInflater.from(ctx).inflate(R.layout.uc_dropsearchdialog, null)

    val searchField: CalcText = dialogView.findViewById(R.id.unitSearchField)
    val searchFieldContainer: TextInputLayout = dialogView.findViewById(R.id.unitSearchFieldContainer)
    val recyclerView: RecyclerView = dialogView.findViewById(R.id.unitsContainer)
    val unitCyclerView: RecyclerView = dialogView.findViewById(R.id.unitPinsContainer)

    recyclerView.layoutManager = LinearLayoutManager(ctx)
    unitCyclerView.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)

    val adapter = UnitConvSearchAdapter(itemsList, viewModel)
    recyclerView.adapter = adapter

    val m_pins = viewModel.currentUnit.value?.let { UnitConvPin.getConvPinStore(it) }
    m_pins?.ctx = appctx
    val allChips = m_pins?.getAllPins()

    val pinsList = arrayListOf<String>()
    for(value in allChips?.values!!){
        println("Chip yo: $value")
        pinsList.add(value)
    }

    // For remembering removed units
    val pinSet = pinsList.toMutableSet()
    viewModel._localPinList.value = pinSet

    val unitPinAdapter = UnitConvPinAdapter(appctx, pinsList, itemsList, viewModel)

    val localPinlistObserver = Observer<MutableSet<String>?> { newValues ->
        newValues?.let {
            if (it.isNotEmpty() && viewModel.isChipAdded.value == true) {
                val lastAdded = it.last()
                if (lastAdded.isNotEmpty()) {
                    unitPinAdapter.addChip(lastAdded)
                    viewModel.isChipAdded.value = false
                }
            }
        }
    }

    // Observe the LiveData using the lifecycle owner
    viewModel._localPinList.observe(lifeCycleOwner, localPinlistObserver)

    unitCyclerView.adapter = unitPinAdapter

    searchField.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            adapter.filter(s.toString())
            if(searchField.text!!.isNotEmpty()){
                searchFieldContainer.setStartIconDrawable(R.drawable.uc_back)
                searchFieldContainer.setStartIconOnClickListener {
                    searchField.setText("")
                    searchFieldContainer.setStartIconDrawable(R.drawable.uc_search)
                    searchFieldContainer.setStartIconOnClickListener(null)
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
            if(searchField.text!!.isEmpty()){
                searchFieldContainer.setStartIconDrawable(R.drawable.uc_search)
                searchFieldContainer.setStartIconOnClickListener(null)
            }
        }
    })

    val dialog = MaterialAlertDialogBuilder(ctx, R.style.UnitConvDialogTheme)
        .setView(dialogView)
        .setBackgroundInsetStart(24)
        .setBackgroundInsetEnd(24)
        .setBackgroundInsetTop(32)
        .setBackgroundInsetBottom(32)

    dialog.setOnDismissListener {
        viewModel._localPinList.removeObserver(localPinlistObserver)
    }

    dialog.setOnCancelListener {
        viewModel._localPinList.removeObserver(localPinlistObserver)
    }

    return dialog.show()
}