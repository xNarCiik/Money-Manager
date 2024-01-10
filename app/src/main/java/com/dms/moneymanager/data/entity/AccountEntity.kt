package com.dms.moneymanager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val ACCOUNT_TABLE = "AccountEntity"

@Entity(tableName = ACCOUNT_TABLE)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Int,
    @ColumnInfo(name = "NAME")
    val name: String,
    @ColumnInfo(name = "CURRENT_BALANCE")
    val currentBalance: Float,
    @ColumnInfo(name = "IS_ENABLE")
    val isEnable: Boolean,
    @ColumnInfo(name = "HAS_OVERDRAFT")
    val hasOverdraft: Boolean,
    @ColumnInfo(name = "OVERDRAFT_LIMIT")
    val overdraftLimit: Float?
)
