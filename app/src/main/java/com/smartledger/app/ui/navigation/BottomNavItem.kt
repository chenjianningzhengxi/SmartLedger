package com.smartledger.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 底部导航栏条目定义。
 *
 * @property route 导航路由
 * @property label 显示文字
 * @property selectedIcon 选中图标
 * @property unselectedIcon 未选中图标
 */
enum class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME(
        route = "home",
        label = "流水",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    ADD(
        route = "add",
        label = "记账",
        selectedIcon = Icons.Filled.Home, // 用 FAB 替代
        unselectedIcon = Icons.Outlined.Home
    ),
    STATS(
        route = "stats",
        label = "统计",
        selectedIcon = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart
    )
}
