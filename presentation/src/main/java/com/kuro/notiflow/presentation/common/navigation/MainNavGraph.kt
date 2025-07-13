package com.kuro.notiflow.presentation.common.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuro.notiflow.presentation.ui.home.HomeScreen
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
            .padding(horizontal = 10.dp),
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(text = "This is Green")
            }
        }
    }
}