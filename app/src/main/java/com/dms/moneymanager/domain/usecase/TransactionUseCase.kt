package com.dms.moneymanager.domain.usecase

import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend fun getNotAppliedTransactions() =
        transactionRepository.getTransactions().filter { !it.isApplied }

    suspend fun createTransaction(transaction: Transaction) {
        transactionRepository.insertTransaction(transaction = transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionRepository.updateTransaction(transaction = transaction)
    }

    suspend fun removeTransaction(transaction: Transaction) {
        transactionRepository.removeTransaction(transaction = transaction)
    }
}