package com.dms.moneymanager.data.mapper

import com.dms.moneymanager.data.entity.TransactionEntity
import com.dms.moneymanager.domain.model.main.Transaction

object TransactionMapper {

    fun Transaction.toTransactionEntity() = with(this) {
        TransactionEntity(
            id = id,
            name = name,
            amount = amount,
            isApplied = isApplied,
            dueDate = dueDate,
            recurrenceType = recurrenceType
        )
    }

    fun TransactionEntity.toTransaction() = with(this) {
        Transaction(
            id = id,
            name = name,
            amount = amount,
            isApplied = isApplied,
            dueDate = dueDate,
            recurrenceType = recurrenceType
        )
    }
}
