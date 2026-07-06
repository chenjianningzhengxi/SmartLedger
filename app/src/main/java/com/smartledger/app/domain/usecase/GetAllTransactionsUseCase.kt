package com.smartledger.app.domain.usecase

import com.smartledger.app.domain.model.Transaction
import com.smartledger.app.domain.repository.ITransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取全部交易记录的用例。
 * 返回 Flow，当数据库变化时自动推送新数据。
 */
class GetAllTransactionsUseCase @Inject constructor(
    private val repository: ITransactionRepository
) {
    /**
     * 执行获取操作。
     */
    operator fun invoke(): Flow<List<Transaction>> {
        return repository.getAllTransactions()
    }
}
