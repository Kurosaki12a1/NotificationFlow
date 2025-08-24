package com.kuro.notiflow.presentation.ui.main

import android.content.Context
import android.view.WindowManager
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kuro.notiflow.presentation.MainActivity
import com.kuro.notiflow.presentation.common.extensions.getCurrentRoute
import com.kuro.notiflow.presentation.common.navigation.MainNavGraph
import com.kuro.notiflow.presentation.common.theme.NotificationFlowTheme
import com.kuro.notiflow.presentation.common.utils.AppNavigator
import com.kuro.notiflow.presentation.common.utils.AppSnackBar
import com.kuro.notiflow.presentation.common.view.BottomNavigationBar
import com.kuro.notiflow.presentation.common.view.BottomNavigationItem
import com.kuro.notiflow.presentation.ui.main.components.AppTopBar

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val state by viewModel.state
    val window = (context as? MainActivity)?.window
    val navController = rememberNavController()
    val snackBarState = remember { SnackbarHostState() }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(state.settingsModel.secureMode) {
        if (state.settingsModel.secureMode) {
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } else {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    DisposableEffect(Unit) {
        AppNavigator.attachNavController(navController)
        AppSnackBar.attachSnackBar(snackBarState)
        onDispose {
            AppNavigator.detachNavController()
            AppSnackBar.deAttachSnackBar()
        }
    }

    NotificationFlowTheme(
        languageType = state.settingsModel.language,
        themeType = state.settingsModel.themeType,
        colorType = state.settingsModel.colorsType,
        dynamicColor = state.settingsModel.isDynamicColorEnabled,
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeContent,
            topBar = {
                AppTopBar(navBackStackEntry = currentBackStackEntry)
            },
            bottomBar = {
                if (currentBackStackEntry.getCurrentRoute() in BottomNavigationItem.entries.map { it.destination.toString() }) {
                    BottomNavigationBar(
                        modifier = Modifier,
                        selectedItem = currentBackStackEntry.getCurrentRoute(),
                        items = BottomNavigationItem.entries.toTypedArray(),
                        showLabel = true,
                        onItemSelected = { AppNavigator.navigateTo(it.destination) }
                    )
                }
            },
            content = { paddingValues ->
                MainNavGraph(
                    navController = navController,
                    paddingValues = paddingValues
                )
            },
            snackbarHost = { AppSnackBar.ErrorSnackBar() }
        )
    }
}