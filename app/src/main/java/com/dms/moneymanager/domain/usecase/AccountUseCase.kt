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

    suspend fun updateAccount(id: Int, name: String, currentBalance: Float) {
        getAccountById(id = id)?.let { currentAccount ->
            accountRepository.updateAccount(
                account = currentAccount.copy(
                    name = name,
                    currentBalance = currentBalance
                )
            )
        }
    }

    suspend fun enableOrDisableAccount(account: Account) {
        account.isEnable = !account.isEnable
        accountRepository.updateAccount(account = account)
    }

    suspend fun appliedTransaction(account: Account, transaction: Transaction) {
        account.currentBalance += transaction.amount
        transaction.isApplied = true
        accountRepository.updateAccount(account = account)
        transactionUseCase.updateTransaction(transaction = transaction)
    }

    suspend fun transfer(transmitterAccount: Account, receiverAccount: Account, amount: Float) {
        transmitterAccount.currentBalance -= amount
        receiverAccount.currentBalance += amount
        accountRepository.updateAccount(account = transmitterAccount)
        accountRepository.updateAccount(account = receiverAccount)
    }

    suspend fun removeAccount(account: Account) {
        accountRepository.removeAccount(account = account)
        transactionUseCase.removeAccountOnTransactions(account = account)
    }

    fun getCurrentBalance(accounts: List<Account>) =
        accounts.filter { it.isEnable }.map { it.currentBalance }.sum()

    fun getFutureBalance(accounts: List<Account>, transactions: List<Transaction>): Float {
        val currentBalance = getCurrentBalance(accounts = accounts)
        val sumAllTransactionsEnable = transactions.filter { it.isEnable }.map { it.amount }.sum()
        return currentBalance + sumAllTransactionsEnable
    }
}