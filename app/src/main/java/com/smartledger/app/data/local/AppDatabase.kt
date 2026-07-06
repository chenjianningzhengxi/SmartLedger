package com.smartledger.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smartledger.app.data.local.dao.TransactionDao
import com.smartledger.app.data.local.entity.TransactionEntity

/**
 * 智能极简记账本 Room 数据库。
 * 版本号：1
 */
@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * 交易记录 DAO。
     */
    abstract fun transactionDao(): TransactionDao
}
