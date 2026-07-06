package com.smartledger.app.domain.usecase

import com.smartledger.app.domain.repository.ITransactionRepository
import javax.inject.Inject

/**
 * 删除一条交易记录的用例。
 */
class DeleteTransactionUseCase @Inject constructor(
    private val repository: ITransactionRepository
) {
    /**
     * 执行删除操作。
     *
     * @param id 要删除的记录 ID
     * @return Result 包装的 Unit
     */
    suspend operator fun invoke(id: Long): Result<Unit> {
        return repository.deleteTransaction(id)
    }
}
