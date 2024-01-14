package com.dms.moneymanager.data.repository

import com.dms.moneymanager.data.dao.AccountDao
import com.dms.moneymanager.data.dao.TransactionDao
import com.dms.moneymanager.data.mapper.AccountMapper.toAccount
import com.dms.moneymanager.data.mapper.TransactionMapper.toTransaction
import com.dms.moneymanager.data.mapper.TransactionMapper.toTransactionEntity
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao
) : TransactionRepository {
    override suspend fun getTransactionById(id: Int): Transaction? {
        val transactionEntity = transactionDao.getTransactionById(id = id)
        return transactionEntity?.toTransaction(
            destinationAccount = if (transactionEntity.accountId != null) accountDao.getById(id = transactionEntity.accountId)
                ?.toAccount() else null
        )
    }

    override suspend fun getTransactions(): List<Transaction> = transactionDao.getTransactions()
        .map {
            it.toTransaction(
                destinationAccount = if (it.accountId != null) accountDao.getById(id = it.accountId)
                    ?.toAccount() else null
            )
        }

    override suspend fun insertTransaction(transaction: Transaction) =
        transactionDao.insert(transaction.toTransactionEntity())

    override suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.update(transaction.toTransactionEntity())

    override suspend fun removeTransaction(transaction: Transaction) =
        transactionDao.deleteById(transaction.id)

    override suspend fun removeTransactions() = transactionDao.deleteAll()
}