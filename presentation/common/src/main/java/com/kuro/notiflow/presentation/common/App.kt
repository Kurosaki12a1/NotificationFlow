package com.kuro.notiflow.presentation.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.kuro.notiflow.navigation.utils.AppNavigatorImpl
import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.common.navigation.LocalNavController
import com.kuro.notiflow.presentation.common.navigation.LocalNavigator
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.main.MainScreen
import com.kuro.notiflow.presentation.common.utils.AppSnackBar

@Composable
fun App(
    features: Set<FeatureNav>,
    topBarProviders: Set<TopBarProvider>
) {
    val navController = rememberNavController()
    val navigator = remember(navController) {
        AppNavigatorImpl(navController)
    }
    val snackBarState = remember { SnackbarHostState() }

    DisposableEffect(Unit) {
        AppSnackBar.attachSnackBar(snackBarState)
        onDispose {
            AppSnackBar.deAttachSnackBar()
        }
    }

    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalNavController provides navController
    ) {
        MainScreen(
            navController = navController,
            features = features,
            topBarProviders = topBarProviders
        )
    }
}