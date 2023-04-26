package com.dms.moneymanager.domain.usecase

import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.domain.repository.AccountRepository
import javax.inject.Inject

class AccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend fun getAccounts() = accountRepository.getAccounts()

    suspend fun createAccount(account: Account) {
        accountRepository.insertAccount(account = account)
    }

    suspend fun removeAccount(account: Account) {
        accountRepository.removeAccount(account = account)
    }

    fun getCurrentBalance(accounts: List<Account>) = accounts.map { it.currentBalance }.sum()

    fun getFutureBalance(accounts: List<Account>, transactions: List<Transaction>): Float {
        val currentBalance = getCurrentBalance(accounts = accounts)
        val sumAllTransactions = transactions.map { it.amount }.sum()
        return currentBalance + sumAllTransactions
    }
}