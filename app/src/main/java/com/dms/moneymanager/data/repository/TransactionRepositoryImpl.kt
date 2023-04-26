package com.dms.moneymanager.data.repository

import com.dms.moneymanager.data.dao.TransactionDao
import com.dms.moneymanager.data.mapper.TransactionMapper.toTransaction
import com.dms.moneymanager.data.mapper.TransactionMapper.toTransactionEntity
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override suspend fun getTransactions(): List<Transaction> {
        return transactionDao.getTransactions().map { it.toTransaction() }
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insert(transaction.toTransactionEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.update(transaction.toTransactionEntity())
    }

    override suspend fun removeTransaction(transaction: Transaction) {
        transactionDao.deleteByName(transaction.name)
    }
}