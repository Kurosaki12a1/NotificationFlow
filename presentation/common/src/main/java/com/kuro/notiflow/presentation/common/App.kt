package com.kuro.notiflow.presentation.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.kuro.notiflow.navigation.utils.AppNavigatorImpl
import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.dialog.DialogController
import com.kuro.notiflow.presentation.common.ui.local.LocalDialogController
import com.kuro.notiflow.presentation.common.ui.local.LocalNavController
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.presentation.common.ui.local.LocalSnackBarHostState
import com.kuro.notiflow.presentation.common.ui.main.MainScreen

@Composable
fun App(
    features: Set<FeatureNav>,
    topBarProviders: Set<TopBarProvider>,
    dialogController: DialogController
) {
    val navController = rememberNavController()
    val navigator = remember(navController) {
        AppNavigatorImpl(navController)
    }
    val snackBarState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalNavController provides navController,
        LocalSnackBarHostState provides snackBarState,
        LocalDialogController provides dialogController
    ) {
        MainScreen(
            navController = navController,
            features = features,
            topBarProviders = topBarProviders
        )
    }
}
