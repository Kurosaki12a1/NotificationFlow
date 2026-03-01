package com.kuro.notiflow.presentation.common.ui.main

import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.notiflow.presentation.common.AppScope
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.common.MainActivity
import com.kuro.notiflow.presentation.common.navigation.MainNavGraph
import com.kuro.notiflow.presentation.common.theme.NotificationFlowTheme
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.dialog.AppDialogHost
import com.kuro.notiflow.presentation.common.ui.local.LocalNavController
import com.kuro.notiflow.presentation.common.ui.local.LocalSnackBarController
import com.kuro.notiflow.presentation.common.ui.main.components.AppTopBar
import com.kuro.notiflow.presentation.common.ui.main.components.EmptyScreen
import com.kuro.notiflow.presentation.common.ui.snackbar.DefaultSnackBar
import com.kuro.notiflow.presentation.common.ui.snackbar.ErrorSnackBar
import com.kuro.notiflow.presentation.common.ui.snackbar.InfoSnackBar
import com.kuro.notiflow.presentation.common.utils.SnackBarType
import com.kuro.notiflow.presentation.common.view.BottomNavigationBar
import com.kuro.notiflow.presentation.common.view.BottomNavigationItem

private val bottomBarStartRoutes = setOf(
    Screen.Home.toString(),
    Screen.Notifications.toString(),
    Screen.Bookmark.toString(),
    Screen.Settings.toString()
)

@Composable
fun AppScope.MainScreen(
    features: Set<FeatureNav>,
    topBarProviders: Set<TopBarProvider>,
    viewModel: MainViewModel = hiltViewModel(),
) {
    // These values are host-only dependencies used to render the root scaffold.
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val snackBarController = LocalSnackBarController.current
    val window = (LocalActivity.current as? MainActivity)?.window
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(state.settingsModel?.secureMode) {
        val secureMode = state.settingsModel?.secureMode ?: return@LaunchedEffect
        if (secureMode) {
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } else {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    val settings = state.settingsModel
    if (settings == null) {
        EmptyScreen()
        return
    }

    val shouldShowOnboarding = (state.isFirstLaunch != false)
    val startGraph = if (shouldShowOnboarding) Graph.OnboardingGraph else Graph.HomeGraph
    val currentRoute = currentBackStackEntry?.destination?.route
    val currentParentRoute = currentBackStackEntry?.destination?.parent?.route
    val isOnboardingRoute = currentParentRoute == Graph.OnboardingGraph.toString()
    val shouldShowBottomBar = currentRoute in bottomBarStartRoutes

    NotificationFlowTheme(
        languageType = settings.language,
        themeType = settings.themeType,
        colorType = settings.colorsType,
        dynamicColor = settings.isDynamicColorEnabled,
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeContent,
            topBar = {
                if (!isOnboardingRoute) {
                    AppTopBar(
                        appScope = this,
                        navController = navController,
                        providers = topBarProviders
                    )
                }
            },
            bottomBar = {
                if (shouldShowBottomBar) {
                    BottomNavigationBar(
                        modifier = Modifier,
                        selectedItem = currentParentRoute,
                        items = BottomNavigationItem.entries.toTypedArray(),
                        showLabel = true,
                        onItemSelected = { navigateGraph(it.destination) }
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
                    features = features,
                    startDestination = startGraph
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarController.hostState,
                    snackbar = { data ->
                        when (snackBarController.type) {
                            SnackBarType.INFO -> InfoSnackBar(data)
                            SnackBarType.ERROR -> ErrorSnackBar(data)
                            SnackBarType.SUCCESS -> DefaultSnackBar(data)
                        }
                    }
                )
            }
        )
        AppDialogHost()
    }
}
