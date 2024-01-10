package com.dms.moneymanager.data.mapper

import com.dms.moneymanager.data.entity.TransactionEntity
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction

object TransactionMapper {

    fun Transaction.toTransactionEntity() = with(this) {
        TransactionEntity(
            id = id,
            name = name,
            amount = amount,
            isEnable = isEnable,
            isApplied = isApplied,
            dueDate = dueDate,
            recurrenceType = recurrenceType,
            accountId = destinationAccount?.id
        )
    }

    fun TransactionEntity.toTransaction(
        destinationAccount: Account?
    ) = with(this) {
        Transaction(
            id = id,
            name = name,
            amount = amount,
            isEnable = isEnable,
            isApplied = isApplied,
            dueDate = dueDate,
            recurrenceType = recurrenceType,
            destinationAccount = destinationAccount
        )
    }
}
