package com.client.expenseanalyzer.model

/**
 * Model class for SMS' details
 */
class SMS(
    var address: String?,
    var msg: String?,
    var readState: String?,
    var time: Long?,
    var type: String?
)