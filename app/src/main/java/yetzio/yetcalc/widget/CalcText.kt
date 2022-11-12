package yetzio.yetcalc.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.EditText

interface CalcTextListener {
    fun onUpdate()
    fun onCutText()
}

@SuppressLint("AppCompatCustomView")
class CalcText : EditText {
    var listeners: ArrayList<CalcTextListener>

    constructor(context: Context?) : super(context) {
        listeners = ArrayList()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        listeners = ArrayList()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        listeners = ArrayList()
    }

    fun addListener(listener: CalcTextListener) {
        try {
            listeners.add(listener)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
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