package com.smartledger.app.data.repository

import com.smartledger.app.data.local.dao.TransactionDao
import com.smartledger.app.data.local.entity.TransactionEntity
import com.smartledger.app.domain.model.MonthlyStats
import com.smartledger.app.domain.model.Transaction
import com.smartledger.app.domain.model.TransactionType
import com.smartledger.app.domain.repository.ITransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 交易记录仓库实现。
 * 将领域模型与 Room 实体相互转换，处理异常边界。
 */
@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : ITransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions().map { entities ->
            entities.map { Transaction.fromEntity(it) }
        }
    }

    override fun getTransactionsByMonth(
        startOfMonth: Long,
        endOfMonth: Long
    ): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByMonth(startOfMonth, endOfMonth).map { entities ->
            entities.map { Transaction.fromEntity(it) }
        }
    }

    override suspend fun getMonthlyStats(
        startOfMonth: Long,
        endOfMonth: Long
    ): MonthlyStats? {
        return try {
            val income = transactionDao.getMonthlyIncome(startOfMonth, endOfMonth)
            val expense = transactionDao.getMonthlyExpense(startOfMonth, endOfMonth)

            val cal = java.util.Calendar.getInstance().apply {
                timeInMillis = startOfMonth
            }
            MonthlyStats(
                year = cal.get(java.util.Calendar.YEAR),
                month = cal.get(java.util.Calendar.MONTH) + 1,
                totalIncome = income,
                totalExpense = expense,
                netBalance = income - expense
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addTransaction(transaction: Transaction): Result<Long> {
        return try {
            val entity = TransactionEntity(
                amount = transaction.amount,
                type = if (transaction.type == TransactionType.INCOME) "INCOME" else "EXPENSE",
                category = transaction.category,
                note = transaction.note,
                date = transaction.date
            )
            val id = transactionDao.insert(entity)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTransaction(id: Long): Result<Unit> {
        return try {
            transactionDao.deleteById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
