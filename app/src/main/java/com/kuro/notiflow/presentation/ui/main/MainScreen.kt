package com.kuro.notiflow.presentation.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kuro.notiflow.presentation.common.navigation.Screen
import com.kuro.notiflow.presentation.common.utils.AppNavigator
import com.kuro.notiflow.presentation.common.view.AppToolbar
import com.kuro.notiflow.presentation.common.view.BottomNavigationBar
import com.kuro.notiflow.presentation.common.view.BottomNavigationItem
import com.kuro.notiflow.presentation.ui.home.HomeScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value

    DisposableEffect(Unit) {
        AppNavigator.attachNavController(navController)
        onDispose {
            AppNavigator.detachNavController()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeContent,
        topBar = { AppToolbar(getCurrentRoute(currentBackStackEntry)) },
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier,
                selectedItem = getCurrentRoute(currentBackStackEntry),
                items = BottomNavigationItem.entries.toTypedArray(),
                showLabel = true,
                onItemSelected = { AppNavigator.navigateTo(it.destination) }
            )
        },
        content = { paddingValues ->
            MainScreenNavigationConfiguration(
                navController = navController,
                paddingValues = paddingValues
            )
        }
    )
}

private fun getCurrentRoute(currentBackStackEntry: NavBackStackEntry?): String? {
    return currentBackStackEntry?.destination?.route.toString()
}

@Composable
private fun MainScreenNavigationConfiguration(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen()
        }
        composable<Screen.Settings> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(text = "This is Blue")
            }
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