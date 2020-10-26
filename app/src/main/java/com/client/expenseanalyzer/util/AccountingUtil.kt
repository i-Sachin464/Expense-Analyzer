package com.client.expenseanalyzer.util

import com.client.expenseanalyzer.model.SMS
import java.util.regex.Pattern

/**
 * This class is used for accounting calculations
 */
class AccountingUtil {
    /**
     * This function is used for calculating total amounts from reading message value of List<SMS>
     *
     * param : ArrayList<SMS>
     * return : double
     */
    fun calculateAmountIn(list: java.util.ArrayList<SMS>): Double {
        var amount = 0.00
        list.forEach() {
            val regexAmount =
                Pattern.compile(RegexPatterns.amount)
            val matcherAmount = regexAmount.matcher(it.msg)
            if (matcherAmount.find()) {
                val group = matcherAmount.group()
                val value: String = when {
                    Pattern.compile("[Rr][Ss][.]").matcher(group).find() -> {
                        group.substring(group.indexOf('.') + 1).trim()
                    }
                    Pattern.compile("[Ii][Nn][Rr]").matcher(group).find() -> {
                        group.substring(group.indexOf(' ') + 1).trim()
                    }
                    else -> {
                        "0.00"
                    }
                }
                amount += value.toDouble()
            }
        }
        return amount
    }
}