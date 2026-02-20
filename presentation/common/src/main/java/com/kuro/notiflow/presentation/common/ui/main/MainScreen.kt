package com.kuro.notiflow.presentation.common.ui.main

import android.content.Context
import android.view.WindowManager
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.common.MainActivity
import com.kuro.notiflow.presentation.common.navigation.MainNavGraph
import com.kuro.notiflow.presentation.common.theme.NotificationFlowTheme
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.dialog.AppDialogHost
import com.kuro.notiflow.presentation.common.ui.local.LocalDialogController
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.presentation.common.ui.local.LocalSnackBarHostState
import com.kuro.notiflow.presentation.common.ui.main.components.AppTopBar
import com.kuro.notiflow.presentation.common.view.BottomNavigationBar
import com.kuro.notiflow.presentation.common.view.BottomNavigationItem

@Composable
fun MainScreen(
    navController: NavHostController,
    features: Set<FeatureNav>,
    topBarProviders: Set<TopBarProvider>,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state by viewModel.state
    val navigator = LocalNavigator.current
    val context: Context = LocalContext.current
    val snackBar = LocalSnackBarHostState.current
    val window = (context as? MainActivity)?.window
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
                AppTopBar(navController, topBarProviders)
            },
            bottomBar = {
                if (currentBackStackEntry?.destination?.parent?.route in BottomNavigationItem.entries.map { it.destination.toString() }) {
                    BottomNavigationBar(
                        modifier = Modifier,
                        selectedItem = currentBackStackEntry?.destination?.parent?.route,
                        items = BottomNavigationItem.entries.toTypedArray(),
                        showLabel = true,
                        onItemSelected = { navigator.navigateGraph(it.destination) }
                    )
                }
            },
            content = { paddingValues ->
                MainNavGraph(
                    navController = navController,
                    paddingValues = PaddingValues(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding(),
                        start = 16.dp + paddingValues.calculateLeftPadding(LayoutDirection.Ltr),
                        end = 16.dp + paddingValues.calculateRightPadding(LayoutDirection.Ltr)
                    ),
                    features = features
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBar,
                    snackbar = { data ->
                        Snackbar(
                            snackbarData = data,
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            actionColor = MaterialTheme.colorScheme.error,
                            dismissActionContentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                )
            }
        )
        AppDialogHost(dialogController = LocalDialogController.current)
    }
}
