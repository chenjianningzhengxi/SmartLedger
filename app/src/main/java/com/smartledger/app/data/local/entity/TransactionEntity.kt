package com.smartledger.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 交易记录 Room 实体表。
 *
 * @property id 主键，自增
 * @property amount 金额（正数）
 * @property type 交易类型：INCOME（收入） / EXPENSE（支出）
 * @property category 分类标签
 * @property note 备注文字
 * @property date 交易时间戳（毫秒）
 * @property createdAt 创建时间戳（毫秒）
 */
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val amount: Double,
    val type: String,        // "INCOME" | "EXPENSE"
    val category: String,
    val note: String = "",
    val date: Long,          // 用户指定的日期
    val createdAt: Long = System.currentTimeMillis()
)
