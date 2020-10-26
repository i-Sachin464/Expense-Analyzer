package com.client.expenseanalyzer.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.client.expenseanalyzer.R
import com.client.expenseanalyzer.adapter.RecyclerviewAdapter
import com.client.expenseanalyzer.databinding.ActivityTransactionBinding
import com.client.expenseanalyzer.model.SMS
import com.client.expenseanalyzer.util.RegexPatterns
import com.client.expenseanalyzer.util.Util
import com.client.expenseanalyzer.util.OnClickListener
import com.client.expenseanalyzer.util.RecyclerViewClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.android.synthetic.main.layout_grant_permission.*
import kotlinx.android.synthetic.main.layout_graph.*

class TransactionActivity : AppCompatActivity() {
    private var activity: Activity = this@TransactionActivity
    private var REQUEST_CODE_SMS = 999

    private val loadedMsgs = MutableLiveData<List<SMS>>()

    private lateinit var viewBinding: ActivityTransactionBinding
    private lateinit var mViewModel: TransactionViewModel
    private lateinit var rAdapter: RecyclerviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_transaction)
        mViewModel = ViewModelProviders.of(this).get(TransactionViewModel::class.java)
        rAdapter = RecyclerviewAdapter(this, mViewModel.mFilteredList)

        if (checkPermissions()) {
            layout_grant_permission.visibility = View.GONE
            if (mViewModel.mFilteredList.isEmpty()) {
                ly_progress.visibility = View.VISIBLE
                mViewModel.readSMS(getSMSCursor()!!, loadedMsgs)
            }
        }
        loadedMsgs.observe(this, Observer {
            ly_progress.visibility = View.GONE
            mViewModel.getTransactionalSMSList(it)
            mViewModel.filterArrayList()
            rAdapter.notifyDataSetChanged()
            mViewModel.calculateTransaction()
            updateGraph()
        })
        initRecyclerView()
        initListeners()
    }

    /**
     * this method is to get Messages from inbox URI
     *
     * @return cursor? fetch all SMS by querying URI from inbox
     */
    fun getSMSCursor(): Cursor? {
        val uri: Uri = Uri.parse("content://sms/inbox")
        return contentResolver.query(uri, null, null, null, null)
    }

    /**
     * method used for permission check
     *
     * @return true, if permission granted else false
     */
    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.READ_SMS
                )
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_SMS),
                    REQUEST_CODE_SMS
                )
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_SMS),
                    REQUEST_CODE_SMS
                )
            }
            return false
        } else {
            return true
        }
    }

    /**
     * This method used for graph update
     */
    private fun updateGraph() {
        val util = Util()
        util.updatePieChart(mViewModel.getProgressPercentage(), stats_progressbar)
    }

    /**
     * In this method it initialize all onClickListeners
     */
    private fun initListeners() {
        with(viewBinding) {
            btnExpense.setOnClickListener {
                mViewModel.filterArrayList(
                    mViewModel.mFilteredList,
                    mViewModel.getmTransactionList(),
                    RegexPatterns.debit
                )
                rAdapter.notifyDataSetChanged()
            }
            btnIncome.setOnClickListener {
                mViewModel.filterArrayList(
                    mViewModel.mFilteredList,
                    mViewModel.getmTransactionList(),
                    RegexPatterns.credit
                )
                rAdapter.notifyDataSetChanged()
            }
            btnTransaction.setOnClickListener {
                mViewModel.filterArrayList(
                    mViewModel.mFilteredList,
                    mViewModel.getmTransactionList(),
                    RegexPatterns.empty
                )
                rAdapter.notifyDataSetChanged()
            }
            show_graph.setOnClickListener {
                var intentGraph = Intent(this@TransactionActivity, GraphActivity::class.java)
                intentGraph.putExtra("total_income", mViewModel.getTotalIncome())
                intentGraph.putExtra("total_expense", mViewModel.getTotalExpense())
                intentGraph.putExtra("progress", mViewModel.getProgressPercentage())
                startActivity(intentGraph)
            }
        }
    }

    /**
     * This method initialize recyclerview
     */
    private fun initRecyclerView() {
        recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            addOnItemTouchListener(
                RecyclerViewClickListener(context,
                    recyclerview, object : OnClickListener {
                        override fun onClick(view: View?, position: Int) {
                            var sms: SMS = rAdapter.getList()[position];

                            val bottomSheetFragment: BottomSheetDialogFragment =
                                BottomSheetFragment.newInstance(sms)
                            bottomSheetFragment.show(supportFragmentManager, "")
                        }

                        override fun onLongClick(view: View?, position: Int) {
                        }
                    })
            )
            adapter = rAdapter
        }
        rAdapter.setFilter(mViewModel::filter)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_SMS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_SMS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        layout_grant_permission.visibility = View.GONE
                        if (mViewModel.mFilteredList.isEmpty()) {
                            if (mViewModel.mFilteredList.isEmpty()) {
                                ly_progress.visibility = View.VISIBLE
                                mViewModel.readSMS(getSMSCursor()!!, loadedMsgs)
                            }
                        }
                    }
                } else {
                    layout_grant_permission.visibility = View.VISIBLE
                    btn_grant.setOnClickListener {
                        if (checkPermissions()) {
                            layout_grant_permission.visibility = View.GONE
                            if (mViewModel.mFilteredList.isEmpty()) {
                                if (mViewModel.mFilteredList.isEmpty()) {
                                    ly_progress.visibility = View.VISIBLE
                                    mViewModel.readSMS(getSMSCursor()!!, loadedMsgs)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}