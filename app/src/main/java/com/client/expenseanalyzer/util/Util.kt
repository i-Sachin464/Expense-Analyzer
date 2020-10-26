package com.client.expenseanalyzer.util

import android.widget.ProgressBar

class Util {
    /**
     * Param: (Double)Progress_value, (ProgressBar)progressbar_view
     *
     * This function is update progressbar value
     */
    fun updatePieChart(progress: Double, progressBar: ProgressBar) {
        val progressPercentage = (progress * 100).toInt()
        progressBar.progress = progressPercentage
    }


}