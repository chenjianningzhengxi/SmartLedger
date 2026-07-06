package com.smartledger.app.domain.model

import com.smartledger.app.data.local.entity.TransactionEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 交易记录领域模型。
 *
 * @property id 唯一标识
 * @property amount 金额
 * @property type 交易类型
 * @property category 分类
 * @property note 备注
 * @property date 交易日期时间戳（毫秒）
 */
data class Transaction(
    val id: Long,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val note: String,
    val date: Long
) {
    /** 格式化的日期字符串，如 "06月30日 周一"。 */
    val formattedDate: String
        get() {
            val sdf = SimpleDateFormat("MM月dd日 EE", Locale.CHINESE)
            return sdf.format(Date(date))
        }

    /** 格式化的金额，如 "+¥123.45" 或 "-¥123.45"。 */
    val formattedAmount: String
        get() {
            val prefix = if (type == TransactionType.INCOME) "+" else "-"
            return "${prefix}¥${String.format("%.2f", amount)}"
        }

    companion object {
        /**
         * 从 Room 实体转换为领域模型。
         */
        fun fromEntity(entity: TransactionEntity): Transaction {
            return Transaction(
                id = entity.id,
                amount = entity.amount,
                type = if (entity.type == "INCOME") TransactionType.INCOME else TransactionType.EXPENSE,
                category = entity.category,
                note = entity.note,
                date = entity.date
            )
        }
    }
}
