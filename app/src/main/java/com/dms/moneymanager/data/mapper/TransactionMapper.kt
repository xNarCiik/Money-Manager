package com.dms.moneymanager.data.mapper

import com.dms.moneymanager.data.entity.TransactionEntity
import com.dms.moneymanager.domain.model.main.Transaction

object TransactionMapper {

    fun Transaction.toTransactionEntity() = with(this) {
        TransactionEntity(
            name = name,
            amount = amount,
            dueDate = dueDate,
            recurrenceType = recurrenceType
        )
    }

    fun TransactionEntity.toTransaction() = with(this) {
        Transaction(
            name = name,
            amount = amount,
            dueDate = dueDate,
            recurrenceType = recurrenceType
        )
    }
}
