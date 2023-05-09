package com.dms.moneymanager.domain.usecase

import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    private suspend fun getTransactionById(id: Int) = transactionRepository.getTransactionById(id = id)

    suspend fun getAllTransactions() = transactionRepository.getTransactions()

    suspend fun getNotAppliedTransactions() = getAllTransactions().filter { !it.isApplied }

    suspend fun createTransaction(transaction: Transaction) {
        transactionRepository.insertTransaction(transaction = transaction)
    }

    suspend fun editTransaction(id: Int, name: String, amount: Float) {
       getTransactionById(id = id)?.let { currentTransaction ->
           transactionRepository.updateTransaction(
               transaction = currentTransaction.copy(
                   name = name,
                   amount = amount
               )
           )
       }
    }

    suspend fun editTransaction(transaction: Transaction) {
        transactionRepository.updateTransaction(transaction = transaction)
    }

    suspend fun removeTransaction(transaction: Transaction) {
        transactionRepository.removeTransaction(transaction = transaction)
    }
}