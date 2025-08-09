package com.kuro.notiflow.presentation.common.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuro.notiflow.presentation.ui.home.HomeScreen
import com.kuro.notiflow.presentation.ui.notifications.NotificationsScreen
import com.kuro.notiflow.presentation.ui.notifications.NotificationsViewModel
import com.kuro.notiflow.presentation.ui.settings.SettingsScreen

@Composable
fun MainNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen()
        }
        composable<Screen.Settings> {
            SettingsScreen()
        }
        composable<Screen.Notifications> {
            val viewModel: NotificationsViewModel = hiltViewModel<NotificationsViewModel>(it)
            NotificationsScreen(viewModel)
        }
    }
}