package com.smartledger.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smartledger.app.ui.screen.add.AddTransactionScreen
import com.smartledger.app.ui.screen.home.HomeScreen
import com.smartledger.app.ui.screen.settings.SettingsScreen
import com.smartledger.app.ui.screen.stats.StatsScreen

/**
 * 应用主导航图。
 * 包含底部导航栏 + 三个主页面，以及 FAB 和设置入口。
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // 底部导航项定义
    val bottomNavItems = listOf(
        Triple("home", "流水", Icons.Filled.Home),
        Triple("stats", "统计", Icons.Filled.BarChart),
        Triple("settings", "设置", Icons.Filled.Settings)
    )

    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.first }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { (route, label, icon) ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == route
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label
                                )
                            },
                            label = { Text(label) }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentDestination?.route in listOf("home", "stats", "settings")) {
                FloatingActionButton(
                    onClick = { navController.navigate("add") },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "记账",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(onNavigateToAdd = { navController.navigate("add") })
            }
            composable("add") {
                AddTransactionScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable("stats") {
                StatsScreen()
            }
            composable("settings") {
                SettingsScreen()
            }
        }
    }
}
