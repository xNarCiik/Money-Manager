package com.dms.moneymanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dms.moneymanager.data.entity.TRANSACTION_TABLE
import com.dms.moneymanager.data.entity.TransactionEntity

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(transaction: TransactionEntity)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Query("SELECT * FROM $TRANSACTION_TABLE")
    suspend fun getTransactions(): List<TransactionEntity>

    @Query("DELETE FROM $TRANSACTION_TABLE WHERE ID = :id")
    suspend fun deleteById(id: Int)
}
