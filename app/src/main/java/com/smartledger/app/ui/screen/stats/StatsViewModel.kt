package com.smartledger.app.ui.screen.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartledger.app.domain.model.MonthlyStats
import com.smartledger.app.domain.usecase.GetMonthlyStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * 统计页面 UI 状态。
 */
sealed interface StatsUiState {
    data object Loading : StatsUiState
    data class Success(val stats: MonthlyStats) : StatsUiState
    data class Error(val message: String) : StatsUiState
    data object Empty : StatsUiState
}

/**
 * 统计页面 ViewModel。
 * 加载月度收支统计数据。
 */
@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getMonthlyStatsUseCase: GetMonthlyStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<StatsUiState>(StatsUiState.Loading)
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    /** 当前选中的年份 */
    private val _selectedYear = MutableStateFlow(
        Calendar.getInstance().get(Calendar.YEAR)
    )
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    /** 当前选中的月份（1-12） */
    private val _selectedMonth = MutableStateFlow(
        Calendar.getInstance().get(Calendar.MONTH) + 1
    )
    val selectedMonth: StateFlow<Int> = _selectedMonth.asStateFlow()

    init {
        loadStats()
    }

    /**
     * 加载当前年月的数据。
     */
    fun loadStats() {
        viewModelScope.launch {
            _uiState.value = StatsUiState.Loading
            try {
                val stats = getMonthlyStatsUseCase(
                    _selectedYear.value,
                    _selectedMonth.value
                )
                _uiState.value = if (stats != null) {
                    StatsUiState.Success(stats)
                } else {
                    StatsUiState.Empty
                }
            } catch (e: Exception) {
                _uiState.value = StatsUiState.Error(
                    e.message ?: "加载统计失败"
                )
            }
        }
    }

    /**
     * 切换到上一个月。
     */
    fun previousMonth() {
        var year = _selectedYear.value
        var month = _selectedMonth.value - 1
        if (month < 1) {
            month = 12
            year--
        }
        _selectedYear.value = year
        _selectedMonth.value = month
        loadStats()
    }

    /**
     * 切换到下一个月。
     */
    fun nextMonth() {
        var year = _selectedYear.value
        var month = _selectedMonth.value + 1
        if (month > 12) {
            month = 1
            year++
        }
        _selectedYear.value = year
        _selectedMonth.value = month
        loadStats()
    }

    /**
     * 返回当月。
     */
    fun resetToCurrentMonth() {
        val now = Calendar.getInstance()
        _selectedYear.value = now.get(Calendar.YEAR)
        _selectedMonth.value = now.get(Calendar.MONTH) + 1
        loadStats()
    }
}
