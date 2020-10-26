package com.client.expenseanalyzer.util

import android.view.View

interface OnClickListener {
    fun onClick(view: View?, position: Int)
    fun onLongClick(view: View?, position: Int)
}