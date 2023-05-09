package com.dms.moneymanager.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dms.moneymanager.data.dao.AccountDao
import com.dms.moneymanager.data.dao.TransactionDao
import com.dms.moneymanager.data.database.converter.TypeConverter
import com.dms.moneymanager.data.entity.AccountEntity
import com.dms.moneymanager.data.entity.TransactionEntity

@androidx.room.Database(
    entities = [AccountEntity::class, TransactionEntity::class],
    version = 2, // TODO BACK TO 1
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
                ).addMigrations(MIGRATION_1_2).build()
                databaseInstance = instance
                return instance
            }
        }

        // TODO REMOVE ALL AT THE END AND PUT DB VERSION TO 1
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE TransactionEntity ADD COLUMN ACCOUNT_ID INTEGER NULL")
            }
        }
    }
}
