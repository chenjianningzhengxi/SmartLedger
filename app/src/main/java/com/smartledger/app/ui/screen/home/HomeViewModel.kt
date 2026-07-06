package com.smartledger.app.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartledger.app.domain.model.Transaction
import com.smartledger.app.domain.usecase.DeleteTransactionUseCase
import com.smartledger.app.domain.usecase.GetAllTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 首页流水列表 UI 状态。
 */
sealed interface HomeUiState {
    /** 加载中 */
    data object Loading : HomeUiState
    /** 加载成功，包含交易列表 */
    data class Success(
        val transactions: List<Transaction>,
        val totalIncome: Double,
        val totalExpense: Double,
        val balance: Double
    ) : HomeUiState
    /** 加载失败 */
    data class Error(val message: String) : HomeUiState
}

/**
 * 首页 ViewModel。
 * 管理交易流水列表的加载、摘要计算和删除操作。
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        loadTransactions()
    }

    /**
     * 加载所有交易记录 Flow。
     */
    private fun loadTransactions() {
        viewModelScope.launch {
            getAllTransactionsUseCase()
                .onStart { _uiState.value = HomeUiState.Loading }
                .catch { e ->
                    _uiState.value = HomeUiState.Error(
                        e.message ?: "加载失败"
                    )
                }
                .collect { transactions ->
                    val income = transactions
                        .filter { it.type.name == "INCOME" }
                        .sumOf { it.amount }
                    val expense = transactions
                        .filter { it.type.name == "EXPENSE" }
                        .sumOf { it.amount }
                    _uiState.value = HomeUiState.Success(
                        transactions = transactions,
                        totalIncome = income,
                        totalExpense = expense,
                        balance = income - expense
                    )
                }
        }
    }

    /**
     * 删除指定交易记录。
     *
     * @param id 记录 ID
     */
    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            val result = deleteTransactionUseCase(id)
            result.onSuccess {
                _snackbarMessage.value = "删除成功"
            }.onFailure {
                _snackbarMessage.value = "删除失败，请重试"
            }
        }
    }

    /**
     * 清除 Snackbar 消息。
     */
    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
