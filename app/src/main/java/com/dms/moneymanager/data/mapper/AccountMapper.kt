package com.dms.moneymanager.data.mapper

import com.dms.moneymanager.data.entity.AccountEntity
import com.dms.moneymanager.domain.model.main.Account

object AccountMapper {

    fun Account.toAccountEntity() = with(this) {
        AccountEntity(
            id = id,
            name = name,
            currentBalance = currentBalance,
            hasOverdraft = hasOverdraft,
            overdraftLimit = overdraftLimit
        )
    }

    fun AccountEntity.toAccount() = with(this) {
        Account(
            id = id,
            name = name,
            currentBalance = currentBalance,
            hasOverdraft = hasOverdraft,
            overdraftLimit = overdraftLimit
        )
    }
}
