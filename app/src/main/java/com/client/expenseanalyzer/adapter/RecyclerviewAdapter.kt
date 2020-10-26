package com.client.expenseanalyzer.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.client.expenseanalyzer.R
import com.client.expenseanalyzer.model.SMS
import kotlinx.android.synthetic.main.layout_messages.view.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

/**
 * Adapter class for the recycler view to show the details of a SMS
 */
class RecyclerviewAdapter(
    private val mContext: Context,
    private val mFilteredList: ArrayList<SMS>
) :
    RecyclerView.Adapter<RecyclerviewAdapter.SMSViewHolder>() {
    private var filterLogic: (SMS, Pattern) -> Boolean = { _: SMS, _: Pattern -> true }
    private var regex: Pattern = Pattern.compile("")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerviewAdapter.SMSViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_messages, parent, false)
        return SMSViewHolder(view)
    }

    override fun onBindViewHolder(holderSMS: RecyclerviewAdapter.SMSViewHolder, position: Int) {
        val sms: SMS = mFilteredList[position]
        holderSMS.bind(sms, filterLogic(sms, regex))
    }

    override fun getItemCount(): Int {
        return mFilteredList.size
    }

    fun setFilter(filter: (SMS, Pattern) -> Boolean) {
        filterLogic = filter
        notifyDataSetChanged()
    }

    fun setRegex(regex: Pattern) {
        this.regex = regex
        notifyDataSetChanged()
    }

    fun getList(): List<SMS> {
        return this.mFilteredList
    }

    inner class SMSViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        var senderContact: TextView = itemView.findViewById(R.id.smsSender)
        private var message: TextView = itemView.findViewById(R.id.smsContent)

        fun bind(sms: SMS, visible: Boolean) {
            if (visible) {
                val savedContactName: String = sms.address.toString()
                senderContact.text = savedContactName
                message.text = sms.msg
            } else {
            }
        }
    }
}