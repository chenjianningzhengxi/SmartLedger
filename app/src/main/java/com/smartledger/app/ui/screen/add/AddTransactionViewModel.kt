package com.smartledger.app.ui.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartledger.app.domain.model.Transaction
import com.smartledger.app.domain.model.TransactionType
import com.smartledger.app.domain.usecase.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 记账页面 UI 状态。
 */
data class AddTransactionUiState(
    val amount: String = "",
    val selectedType: TransactionType = TransactionType.EXPENSE,
    val selectedCategory: String = "餐饮",
    val note: String = "",
    val selectedDate: Long = System.currentTimeMillis(),
    val isSaving: Boolean = false,
    val amountError: String? = null
)

/**
 * 记账页面一次性事件。
 */
sealed interface AddTransactionEvent {
    data object SaveSuccess : AddTransactionEvent
    data class SaveError(val message: String) : AddTransactionEvent
}

/**
 * 记账页面 ViewModel。
 * 处理金额输入校验、分类选择和保存逻辑。
 */
@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AddTransactionEvent>()
    val events = _events.asSharedFlow()

    /** 预定义的支出分类列表 */
    val expenseCategories = listOf(
        "餐饮", "交通", "购物", "娱乐", "医疗", "教育", "其他"
    )

    /** 预定义的收入分类列表 */
    val incomeCategories = listOf(
        "工资", "兼职", "红包", "理财", "其他"
    )

    /** 当前可用分类（根据所选类型切换） */
    val currentCategories: List<String>
        get() = if (_uiState.value.selectedType == TransactionType.INCOME)
            incomeCategories else expenseCategories

    /**
     * 更新金额输入。
     * 校验格式：只允许数字和小数点。
     */
    fun updateAmount(amount: String) {
        if (amount.isEmpty() || amount.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
            _uiState.value = _uiState.value.copy(
                amount = amount,
                amountError = null
            )
        }
    }

    /**
     * 更新交易类型（收入/支出）。
     * 切换时重置分类为对应列表的第一项。
     */
    fun updateType(type: TransactionType) {
        _uiState.value = _uiState.value.copy(
            selectedType = type,
            selectedCategory = if (type == TransactionType.INCOME) "工资" else "餐饮"
        )
    }

    /**
     * 更新分类。
     */
    fun updateCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    /**
     * 更新备注。
     */
    fun updateNote(note: String) {
        _uiState.value = _uiState.value.copy(note = note)
    }

    /**
     * 更新日期。
     */
    fun updateDate(dateMillis: Long) {
        _uiState.value = _uiState.value.copy(selectedDate = dateMillis)
    }

    /**
     * 保存交易记录。
     * 先校验金额不为空且有效。
     */
    fun save() {
        val state = _uiState.value

        // 校验金额
        if (state.amount.isBlank()) {
            _uiState.value = state.copy(amountError = "请输入金额")
            return
        }
        val amount = state.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _uiState.value = state.copy(amountError = "金额格式不正确")
            return
        }
        if (amount > 99999999.99) {
            _uiState.value = state.copy(amountError = "金额超出范围")
            return
        }

        _uiState.value = state.copy(isSaving = true, amountError = null)

        viewModelScope.launch {
            val transaction = Transaction(
                id = 0L,
                amount = amount,
                type = state.selectedType,
                category = state.selectedCategory,
                note = state.note.trim(),
                date = state.selectedDate
            )

            val result = addTransactionUseCase(transaction)
            result.onSuccess {
                _events.emit(AddTransactionEvent.SaveSuccess)
                // 重置表单
                _uiState.value = AddTransactionUiState()
            }.onFailure { e ->
                _events.emit(
                    AddTransactionEvent.SaveError(e.message ?: "保存失败，请重试")
                )
                _uiState.value = _uiState.value.copy(isSaving = false)
            }
        }
    }
}
