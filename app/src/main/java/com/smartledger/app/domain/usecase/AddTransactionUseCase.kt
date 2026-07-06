package com.smartledger.app.domain.usecase

import com.smartledger.app.domain.model.Transaction
import com.smartledger.app.domain.repository.ITransactionRepository
import javax.inject.Inject

/**
 * 添加一条交易记录的用例。
 */
class AddTransactionUseCase @Inject constructor(
    private val repository: ITransactionRepository
) {
    /**
     * 执行添加操作。
     *
     * @param transaction 待添加的交易记录（id 应为 0）
     * @return Result 包装的 Long（新纪录 ID）
     */
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        return repository.addTransaction(transaction)
    }
}
