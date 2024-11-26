package yetzio.yetcalc.views

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import yetzio.yetcalc.R
import yetzio.yetcalc.adapters.HistoryAdapter
import yetzio.yetcalc.config.CalcBaseActivity
import yetzio.yetcalc.utils.getThemeColor
import yetzio.yetcalc.utils.setVibOnClick

class HistoryActivity : CalcBaseActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var histListView: RecyclerView
    private lateinit var clearBt: MaterialButton
    private lateinit var nohistorycontainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        toolbar = findViewById(R.id.app_bar)
        setSupportActionBar(toolbar)
        nohistorycontainer = findViewById(R.id.nohistorycontainer)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.navigationIcon?.setTint(getThemeColor(R.attr.calcTextDefaultColor))

        histListView = findViewById(R.id.historyContainer)
        histListView.layoutManager = LinearLayoutManager(this)

        Calc.m_history.histViewModel?.histArrList?.observe(this) { newlist ->
            histListView.adapter = HistoryAdapter(this, newlist)
            if(newlist.isNotEmpty()){
                nohistorycontainer.visibility = View.GONE
                histListView.visibility = View.VISIBLE
            }
        }

        Calc.m_history.histViewModel?.listCount?.observe(this) { newcount ->
            Calc.m_history.histViewModel?.histArrList?.value = Calc.m_history.getHistoryItems()
        }

        clearBt = findViewById(R.id.histdelbutton)
        clearBt.setOnClickListener {
            setVibOnClick(applicationContext)
            Calc.m_history.emptyDB()
            histListView.adapter = HistoryAdapter(this, ArrayList())

            histListView.visibility = View.GONE
            nohistorycontainer.visibility = View.VISIBLE
        }
    }
}