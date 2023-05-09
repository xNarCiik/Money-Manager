package com.dms.moneymanager.domain.repository

import com.dms.moneymanager.domain.model.main.Account

interface AccountRepository {
    suspend fun getAccountById(id: Int): Account?
    suspend fun getAccounts(): List<Account>
    suspend fun insertAccount(account: Account)
    suspend fun updateAccount(account: Account)
    suspend fun removeAccount(account: Account)
}