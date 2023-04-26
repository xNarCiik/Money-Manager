package com.dms.moneymanager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dms.moneymanager.domain.model.main.RecurrenceType
import java.util.Date

const val TRANSACTION_TABLE = "TransactionEntity"

@Entity(tableName = TRANSACTION_TABLE)
data class TransactionEntity(
    @PrimaryKey
    @ColumnInfo(name = "NAME")
    val name: String,
    @ColumnInfo(name = "AMOUNT")
    val amount: Float,
    @ColumnInfo(name = "DATE")
    val dueDate: Date?,
    @ColumnInfo(name = "RECURRENCE_TYPE")
    val recurrenceType: RecurrenceType?
)