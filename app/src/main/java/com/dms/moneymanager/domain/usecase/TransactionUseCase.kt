package com.dms.moneymanager.domain.usecase

import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    private suspend fun getTransactionById(id: Int) =
        transactionRepository.getTransactionById(id = id)

    suspend fun getAllTransactions() = transactionRepository.getTransactions()

    suspend fun getNotAppliedTransactions() = getAllTransactions().filter { !it.isApplied }

    suspend fun createTransaction(transaction: Transaction) {
        transactionRepository.insertTransaction(transaction = transaction)
    }

    suspend fun updateTransaction(
        id: Int,
        name: String,
        amount: Float,
        destinationAccount: Account?
    ) {
        getTransactionById(id = id)?.let { currentTransaction ->
            transactionRepository.updateTransaction(
                transaction = currentTransaction.copy(
                    name = name,
                    amount = amount,
                    destinationAccount = destinationAccount
                )
            )
        }
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionRepository.updateTransaction(transaction = transaction)
    }

    suspend fun enableOrDisableTransaction(transaction: Transaction,) {
        transaction.isEnable = !transaction.isEnable
        transactionRepository.updateTransaction(transaction = transaction)
    }

    suspend fun removeTransaction(transaction: Transaction) {
        transactionRepository.removeTransaction(transaction = transaction)
    }

    suspend fun removeAccountOnTransactions(account: Account) {
        getAllTransactions().filter { it.destinationAccount?.id == account.id }.forEach {
            updateTransaction(it.copy(destinationAccount = null))
        }
    }
}