package com.dms.moneymanager.domain.model.main

import java.util.Date

data class Transaction(
    val id: Int = 0,
    val name: String,
    val amount: Float,
    var isApplied: Boolean = false,
    val dueDate: Date? = null,
    val recurrenceType: RecurrenceType? = null
)

enum class RecurrenceType {
    MONTH,
    YEAR
}
