package com.smartledger.app.domain.usecase

import com.smartledger.app.domain.model.MonthlyStats
import com.smartledger.app.domain.repository.ITransactionRepository
import java.util.Calendar
import javax.inject.Inject

/**
 * 获取月度统计数据的用例。
 */
class GetMonthlyStatsUseCase @Inject constructor(
    private val repository: ITransactionRepository
) {
    /**
     * 获取指定年份和月份的统计数据。
     *
     * @param year  年份，如 2026
     * @param month 月份（1-12）
     * @return MonthlyStats 或 null（当月无数据时）
     */
    suspend operator fun invoke(year: Int, month: Int): MonthlyStats? {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfMonth = cal.timeInMillis

        cal.add(Calendar.MONTH, 1)
        val endOfMonth = cal.timeInMillis

        return repository.getMonthlyStats(startOfMonth, endOfMonth)
    }
}
