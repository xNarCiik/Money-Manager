package com.dms.moneymanager.data.di

import android.content.Context
import com.dms.moneymanager.data.dao.AccountDao
import com.dms.moneymanager.data.dao.TransactionDao
import com.dms.moneymanager.data.database.Database
import com.dms.moneymanager.data.repository.AccountRepositoryImpl
import com.dms.moneymanager.data.repository.TransactionRepositoryImpl
import com.dms.moneymanager.domain.repository.AccountRepository
import com.dms.moneymanager.domain.repository.TransactionRepository
import com.dms.moneymanager.domain.usecase.AccountUseCase
import com.dms.moneymanager.domain.usecase.TransactionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Database.getDatabase(context)

    @Singleton
    @Provides
    fun provideAccountDao(database: Database): AccountDao {
        return database.accountDao()
    }

    @Singleton
    @Provides
    fun provideTransactionDao(database: Database): TransactionDao {
        return database.transactionDao()
    }

    @Singleton
    @Provides
    fun provideAccountRepository(accountDao: AccountDao): AccountRepository {
        return AccountRepositoryImpl(accountDao)
    }

    @Singleton
    @Provides
    fun provideAccountUseCase(accountRepository: AccountRepository): AccountUseCase {
        return AccountUseCase(accountRepository)
    }

    @Singleton
    @Provides
    fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository {
        return TransactionRepositoryImpl(transactionDao)
    }

    @Singleton
    @Provides
    fun provideTransactionUseCase(transactionRepository: TransactionRepository): TransactionUseCase {
        return TransactionUseCase(transactionRepository)
    }

}