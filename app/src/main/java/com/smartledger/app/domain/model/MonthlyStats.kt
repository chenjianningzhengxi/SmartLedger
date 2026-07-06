package com.smartledger.app.domain.model

/**
 * 月度统计数据模型。
 *
 * @property year 年份
 * @property month 月份 (1-12)
 * @property totalIncome 总收入
 * @property totalExpense 总支出
 * @property netBalance 净结余
 */
data class MonthlyStats(
    val year: Int,
    val month: Int,
    val totalIncome: Double,
    val totalExpense: Double,
    val netBalance: Double
)
