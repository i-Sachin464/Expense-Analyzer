package com.client.expenseanalyzer.ui

import android.database.Cursor
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.client.expenseanalyzer.model.SMS
import com.client.expenseanalyzer.transaction.TransactionManager
import com.client.expenseanalyzer.util.AccountingUtil
import com.client.expenseanalyzer.util.RegexPatterns
import java.util.regex.Pattern


/**
 * ViewModel class for the [MainActivity]
 */
class TransactionViewModel : ViewModel() {
    private val mTransactionList = ArrayList<SMS>()
    private val expenseList = ArrayList<SMS>()
    private val incomeList = ArrayList<SMS>()
    val mFilteredList = ArrayList<SMS>()

    private val transactionManager = TransactionManager()

    private var totalIncome = 0.00
    private var totalExpense = 0.00

    /**
     * method to fetch mTransactionList
     */
    fun getmTransactionList(): ArrayList<SMS> {
        return mTransactionList
    }

    /**
     * Filter ArrayList<SMS> using regex and store it in another ArrayList<SMS>.
     *
     * @param toList : it is filtered out list
     * @param fromList : it is main list from which data is going to filtered out
     * @param regex : in which regex basis list is going to filtered out
     *
     */
    fun filterArrayList(
        toList: ArrayList<SMS> = mFilteredList,
        fromList: ArrayList<SMS> = mTransactionList,
        regex: String = ""
    ) {
        toList.apply {
            clear()
            addAll(fromList.filter {
                filter(
                    it,
                    Pattern.compile(regex)
                )
            })
        }
    }

    /**
     * method to fetch Transactional SMS list from a List of All SMS and
     * store it in mTransactionList
     *
     * @param data : it is a List<SMS> containing every messages
     *
     */
    fun getTransactionalSMSList(data: List<SMS>) {
        try {
            for (i in data.indices) {
                val sms: SMS = data[i]
                val regexAccount =
                    Pattern.compile(RegexPatterns.account)
                val regexDebit = Pattern.compile(RegexPatterns.debit)
                val regexCredit = Pattern.compile(RegexPatterns.credit)
                val regexAddress = Pattern.compile(RegexPatterns.sender)
                val m = regexAddress.matcher(sms.address)
                if (m.find()) {
                    try {
                        val message: String? = sms.msg
                        val matcherMessage = regexAccount.matcher(message)
                        if (matcherMessage.find()) {
                            val matcherDebit = regexDebit.matcher(message)
                            val matcherCredit = regexCredit.matcher(message)
                            if (matcherCredit.find() || matcherDebit.find()) {
                                mTransactionList.add(sms)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SmsReceiver", "Exception smsReceiver$e")
        }
    }

    /**
     * This method is used to find out total income and expense value and
     * set it in a variable
     */
    fun calculateTransaction() {
        val accountUtil = AccountingUtil()
        filterArrayList(incomeList, mTransactionList, RegexPatterns.credit)
        setTotalIncome(accountUtil.calculateAmountIn(incomeList))
        filterArrayList(expenseList, mTransactionList, RegexPatterns.debit)
        setTotalExpense(accountUtil.calculateAmountIn(expenseList))
    }

    /**
     * This method calculate percentage of graph that is going to show in ProgressBar
     *
     * @return calculated value in double
     */
    fun getProgressPercentage(): Double {
        return totalExpense / (totalIncome + totalExpense)
    }

    /**
     * Method to set total income
     */
    private fun setTotalIncome(income: Double) {
        totalIncome = income
    }

    /**
     * method to fetch total income
     */
    fun getTotalIncome(): Double {
        return totalIncome
    }

    /**
     * Method to set total expense
     */
    private fun setTotalExpense(expense: Double) {
        totalExpense = expense
    }

    /**
     * method to fetch total expense
     */
    fun getTotalExpense(): Double {
        return totalExpense
    }

    /**
     * This method filters SMS with regex pattern
     *
     * @param sms : A SMS data
     * @param regexTransactionType : Regex pattern to filter
     *
     * @return true, if regex found in SMS else false
     */
    fun filter(sms: SMS, regexTransactionType: Pattern): Boolean {
        val regexAccount = Pattern.compile(RegexPatterns.account)
        val regexAddress = Pattern.compile(RegexPatterns.sender)
        val m = regexAddress.matcher(sms.address)
        val message: String = sms.msg!!
        when {
            m.find() -> {
                try {
                    val matcherMessage = regexAccount.matcher(message)
                    if (matcherMessage.find()) {
                        val matcherDebit = regexTransactionType.matcher(message)
                        if (matcherDebit.find())
                            return true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else -> {
                return false
            }
        }
        return false
    }

    /**
     * In this method TransactionManager read message
     *
     * @param cursor : cursor object that contails all messages from inbox
     * @param data : reference Livedata object that allows to post value to observer
     */
    fun readSMS(cursor: Cursor, data: MutableLiveData<List<SMS>>) {
        transactionManager.readSMS(cursor, data)
    }


}