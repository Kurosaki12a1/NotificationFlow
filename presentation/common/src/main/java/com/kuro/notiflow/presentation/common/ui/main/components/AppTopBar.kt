package com.kuro.notiflow.presentation.common.ui.main.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider

@Composable
fun AppTopBar(
    navController: NavHostController,
    providers: Set<TopBarProvider>
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route
    providers.firstOrNull { it.route == route }?.Render()
}

