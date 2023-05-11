package com.dms.moneymanager.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dms.moneymanager.data.dao.AccountDao
import com.dms.moneymanager.data.dao.TransactionDao
import com.dms.moneymanager.data.database.converter.TypeConverter
import com.dms.moneymanager.data.entity.AccountEntity
import com.dms.moneymanager.data.entity.TransactionEntity

@androidx.room.Database(
    entities = [AccountEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(TypeConverter::class)
abstract class Database : RoomDatabase() {

    internal abstract fun accountDao(): AccountDao
    internal abstract fun transactionDao(): TransactionDao

    companion object {
        private const val DATABASE_NAME = "Database"

        @Volatile
        private var databaseInstance: Database? = null

        fun getDatabase(context: Context): Database {
            return databaseInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    Database::class.java,
                    DATABASE_NAME
                ).build()
                databaseInstance = instance
                return instance
            }
        }
    }
}
