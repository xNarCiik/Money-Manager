package com.dms.moneymanager.domain.repository

import com.dms.moneymanager.domain.model.main.Transaction

interface TransactionRepository {
    suspend fun getTransactions(): List<Transaction>
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun removeTransaction(transaction: Transaction)
}