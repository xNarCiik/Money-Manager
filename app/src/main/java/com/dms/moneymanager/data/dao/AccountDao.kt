package com.dms.moneymanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dms.moneymanager.data.entity.ACCOUNT_TABLE
import com.dms.moneymanager.data.entity.AccountEntity

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(account: AccountEntity)

    @Update
    suspend fun update(account: AccountEntity)

    @Query("SELECT * FROM $ACCOUNT_TABLE")
    suspend fun getAccounts(): List<AccountEntity>

    @Query("DELETE FROM $ACCOUNT_TABLE WHERE ID = :id")
    suspend fun deleteById(id: Int)
}
