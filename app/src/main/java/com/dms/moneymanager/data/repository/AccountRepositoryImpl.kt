package com.dms.moneymanager.data.repository

import com.dms.moneymanager.data.dao.AccountDao
import com.dms.moneymanager.data.mapper.AccountMapper.toAccount
import com.dms.moneymanager.data.mapper.AccountMapper.toAccountEntity
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {
    override suspend fun getAccountById(id: Int): Account? =
        accountDao.getById(id = id)?.toAccount()

    override suspend fun getAccounts(): List<Account> =
        accountDao.getAccounts().map { it.toAccount() }

    override suspend fun insertAccount(account: Account) =
        accountDao.insert(account.toAccountEntity())

    override suspend fun updateAccount(account: Account) =
        accountDao.update(account.toAccountEntity())

    override suspend fun removeAccount(account: Account) = accountDao.deleteById(account.id)

    override suspend fun removeAccounts() = accountDao.deleteAll()
}