package com.dms.moneymanager.data.mapper

import com.dms.moneymanager.data.entity.AccountEntity
import com.dms.moneymanager.domain.model.main.Account

object AccountMapper {

    fun Account.toAccountEntity() = with(this) {
        AccountEntity(
            name = name,
            currentBalance = currentBalance
        )
    }

    fun AccountEntity.toAccount() = with(this) {
        Account(
            name = name,
            currentBalance = currentBalance
        )
    }
}
