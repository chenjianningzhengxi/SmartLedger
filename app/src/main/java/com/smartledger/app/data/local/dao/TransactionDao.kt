package com.smartledger.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smartledger.app.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

/**
 * 交易记录数据访问对象。
 */
@Dao
interface TransactionDao {

    /**
     * 获取所有交易记录，按日期降序排列（最新在前）。
     */
    @Query("SELECT * FROM transactions ORDER BY date DESC, id DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    /**
     * 根据 ID 获取单条记录。
     */
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    /**
     * 获取指定月份的交易记录。
     *
     * @param startOfMonth 当月起始时间戳（毫秒）
     * @param endOfMonth   当月结束时间戳（毫秒）
     */
    @Query("SELECT * FROM transactions WHERE date >= :startOfMonth AND date < :endOfMonth ORDER BY date DESC")
    fun getTransactionsByMonth(startOfMonth: Long, endOfMonth: Long): Flow<List<TransactionEntity>>

    /**
     * 获取指定月份总收入。
     */
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'INCOME' AND date >= :startOfMonth AND date < :endOfMonth")
    suspend fun getMonthlyIncome(startOfMonth: Long, endOfMonth: Long): Double

    /**
     * 获取指定月份总支出。
     */
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE' AND date >= :startOfMonth AND date < :endOfMonth")
    suspend fun getMonthlyExpense(startOfMonth: Long, endOfMonth: Long): Double

    /**
     * 插入一条交易记录。
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    /**
     * 删除一条交易记录。
     */
    @Delete
    suspend fun delete(transaction: TransactionEntity)

    /**
     * 根据 ID 删除记录。
     */
    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Long)
}
