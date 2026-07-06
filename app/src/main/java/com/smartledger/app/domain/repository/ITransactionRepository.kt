package com.smartledger.app.domain.repository

import com.smartledger.app.domain.model.MonthlyStats
import com.smartledger.app.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * 交易记录仓库接口。
 * 定义数据层的契约，供 UseCase 调用。
 */
interface ITransactionRepository {

    /**
     * 获取所有交易记录的 Flow（实时更新）。
     */
    fun getAllTransactions(): Flow<List<Transaction>>

    /**
     * 获取指定月份的交易记录 Flow。
     */
    fun getTransactionsByMonth(startOfMonth: Long, endOfMonth: Long): Flow<List<Transaction>>

    /**
     * 获取指定月份的统计数据。
     */
    suspend fun getMonthlyStats(startOfMonth: Long, endOfMonth: Long): MonthlyStats?

    /**
     * 添加一条交易记录，返回生成的 ID。
     */
    suspend fun addTransaction(transaction: Transaction): Result<Long>

    /**
     * 删除一条交易记录。
     */
    suspend fun deleteTransaction(id: Long): Result<Unit>
}
