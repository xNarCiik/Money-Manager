package com.dms.moneymanager.domain.usecase

import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.domain.repository.AccountRepository
import javax.inject.Inject

class AccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionUseCase: TransactionUseCase
) {
    private suspend fun getAccountById(id: Int) = accountRepository.getAccountById(id = id)

    suspend fun getAccounts() = accountRepository.getAccounts()

    suspend fun createAccount(account: Account) {
        accountRepository.insertAccount(account = account)
    }

    suspend fun editAccount(id: Int, name: String, currentBalance: Float) {
        getAccountById(id = id)?.let { currentAccount ->
            accountRepository.updateAccount(
                account = currentAccount.copy(
                    name = name,
                    currentBalance = currentBalance
                )
            )
        }
    }

    suspend fun appliedTransaction(account: Account, transaction: Transaction) {
        account.currentBalance += transaction.amount
        transaction.isApplied = true
        accountRepository.updateAccount(account = account)
        transactionUseCase.editTransaction(transaction = transaction)
    }

    suspend fun transfer(transmitterAccount: Account, receiverAccount: Account, amount: Float) {
        transmitterAccount.currentBalance -= amount
        receiverAccount.currentBalance += amount
        accountRepository.updateAccount(account = transmitterAccount)
        accountRepository.updateAccount(account = receiverAccount)
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