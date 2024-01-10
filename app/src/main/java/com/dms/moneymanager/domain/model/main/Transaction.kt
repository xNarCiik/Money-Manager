package com.dms.moneymanager.domain.model.main

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.Date

data class Transaction(
    val id: Int = 0,
    val name: String,
    val amount: Float,
    var isEnable: Boolean = true,
    var isApplied: Boolean = false,
    val dueDate: Date? = null,
    val recurrenceType: RecurrenceType? = null,
    val destinationAccount: Account? = null
)

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
abstract class RecurrenceType {
    data class MonthlyRecurrence(val numberOfRepetition: Int): RecurrenceType()
    object YearRecurrence: RecurrenceType()
}
