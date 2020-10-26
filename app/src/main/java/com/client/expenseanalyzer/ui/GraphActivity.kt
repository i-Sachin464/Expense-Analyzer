package com.client.expenseanalyzer.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.client.expenseanalyzer.R
import com.client.expenseanalyzer.util.Util
import kotlinx.android.synthetic.main.activity_graph.*
import kotlinx.android.synthetic.main.layout_graph.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*


class GraphActivity : AppCompatActivity() {
    private var totalIncome = 0.00
    private var totalExpense = 0.00
    private lateinit var bundle: Bundle
    var df = DecimalFormat("#.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        getBundleData()
        df.roundingMode = RoundingMode.CEILING
        totalIncome = bundle.getDouble("total_income")
        total_income.text = df.format(totalIncome)
        totalExpense = bundle.getDouble("total_expense");
        total_expense.text = df.format(totalExpense)
        var progress = bundle.getDouble("progress")
        var util = Util()
        util.updatePieChart(progress, stats_progressbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getBundleData() {
        bundle = intent.extras!!
    }
}