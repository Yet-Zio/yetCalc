package yetzio.yetcalc.widget

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import yetzio.yetcalc.R

interface CalcTextListener {
    fun onUpdate()
    fun onCutText()
    fun onSolve(selectedText: String): String
}

class CalcText : TextInputEditText {
    var listeners: ArrayList<CalcTextListener> = ArrayList()
    private val SOLVE_ACTION_ID = 1000

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle)

    init {
        customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                menu.add(Menu.NONE, SOLVE_ACTION_ID, Menu.NONE, "Solve")
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return when (item.itemId) {
                    SOLVE_ACTION_ID -> {
                        handleSolveAction()
                        mode.finish()
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode) {}
        }
    }

    fun addListener(listener: CalcTextListener) {
        try {
            listeners.add(listener)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun handleSolveAction() {
        val selectedText = text.toString().substring(selectionStart, selectionEnd)

        val solveResult = listeners.firstNotNullOfOrNull { listener ->
            runCatching { listener.onSolve(selectedText) }.getOrNull()
        }

        solveResult?.let { result ->
            showSolveResultDialog(selectedText, result)
        }
    }

    private fun showSolveResultDialog(expression: String, result: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.expression_solve_dialog, null)
        val expressionTextView = dialogView.findViewById<MaterialTextView>(R.id.tv_expression)
        val resultTextView = dialogView.findViewById<MaterialTextView>(R.id.tv_result)
        val btnCopy = dialogView.findViewById<MaterialButton>(R.id.btn_copy)
        val btnShare = dialogView.findViewById<MaterialButton>(R.id.btn_share)
        val btnClose = dialogView.findViewById<MaterialButton>(R.id.btn_close)

        val dialog = MaterialAlertDialogBuilder(context, com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog)
            .setView(dialogView)
            .create()

        expressionTextView.apply {
            setText(expression)
        }

        resultTextView.apply {
            setText(result)
        }

        // Copy button
        btnCopy.setOnClickListener {
            (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                .setPrimaryClip(ClipData.newPlainText("Solved Result", result))
            dialog.dismiss()
        }

        // Share button
        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Expression: $expression\nResult: $result")
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Result"))
            dialog.dismiss()
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        val consumed = super.onTextContextMenuItem(id)
        when (id) {
            android.R.id.cut -> onTextCut()
            android.R.id.paste -> onTextPaste()
            android.R.id.copy -> onTextCopy()
        }
        return consumed
    }

    fun onTextCut() {
        for (listener in listeners)
            listener.onCutText()
    }

    fun onTextCopy() {}

    fun onTextPaste() {
        for (listener in listeners)
            listener.onUpdate()
    }
}