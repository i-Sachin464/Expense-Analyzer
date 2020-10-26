package com.client.expenseanalyzer.util

/**
 * This is an object class contains All regex patterns that are used fo finding transactional messages
 */
object RegexPatterns {
    const val debit = "(?=.*[Dd]ebited.*)(?=.*[Xx*][Xx*][0-9].*)(?=.*[Ii][Nn][Rr].*|.*[Rr][Ss].*)"
    const val credit = "(?=.*[Cc]redited.*)(?=.*[Xx*][Xx*][0-9].*)(?=.*[Ii][Nn][Rr].*|.*[Rr][Ss].*)"

    const val amount = "[rR][sS]\\.[\\s\\S][,\\d]+\\.?\\d{0,2}|[iI][nN][rR]\\.?\\s*[,\\d]+\\.?\\d{0,2}"
    const val account = ".*[Aa]ccount.*|.*[Aa]/[Cc].*"
    const val sender = "[a-zA-Z0-9]{2}-[a-zA-Z0-9]{6}|[a-zA-Z0-9]{8}"
    const val empty = ""
}