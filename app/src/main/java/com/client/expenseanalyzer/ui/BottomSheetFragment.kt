package com.client.expenseanalyzer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.client.expenseanalyzer.R
import com.client.expenseanalyzer.model.SMS
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_bottom_sheet.*
import java.util.*

internal class BottomSheetFragment : BottomSheetDialogFragment() {
//    lateinit var message: SMS
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.layout_bottom_sheet, container,
            false
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        header.text = message.address.toString()
        info.text = message.msg
        time.text = Date(message.time!!).toString()
    }

    companion object {
        private lateinit var message: SMS
        fun newInstance(sms: SMS): BottomSheetFragment {
            message = sms
            return BottomSheetFragment()
        }
    }
}
