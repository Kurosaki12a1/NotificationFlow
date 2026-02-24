package com.kuro.notiflow.presentation.common

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
import com.kuro.notiflow.presentation.common.ui.local.LocalSnackBarController
import com.kuro.notiflow.presentation.common.ui.local.LocalTopBarScrollBehavior
import com.kuro.notiflow.presentation.common.ui.local.TopBarScrollBehaviorHolder
import com.kuro.notiflow.presentation.common.ui.snackbar.SnackBarControllerImpl
import com.kuro.notiflow.presentation.common.ui.main.MainScreen


/**
 * The root Composable function for the application.
 *
 * This function sets up the global infrastructure for the UI, including:
 * 1.  **Navigation:** Initializes the [navController] and the [navigator] abstraction.
 * 2.  **State Management:** Creates and remembers controllers for Snackbars, Dialogs,
 *     and TopBar scroll behaviors.
 * 3.  **Dependency Injection via CompositionLocal:** Distributes global controllers
 *     down the UI tree using [CompositionLocalProvider], allowing any child
 *     composable to access them without explicit parameter passing.
 *
 * @param features A set of feature-specific navigation handlers.
 * @param topBarProviders A set of providers that define the TopBar content for different routes.
 * @param dialogController The global controller for managing application-level dialogs.
 */
@Composable
fun App(
    features: Set<FeatureNav>,
    topBarProviders: Set<TopBarProvider>,
    dialogController: DialogController
) {
    val navController = rememberNavController()
    // Wraps the NavController into a custom Navigator to abstract
    // navigation logic away from the UI.
    val navigator = remember(navController) {
        AppNavigatorImpl(navController)
    }
    // Initialize UI controllers that persist for the lifetime of the application composition.
    val snackBarController = remember { SnackBarControllerImpl() }
    val topBarScrollHolder = remember { TopBarScrollBehaviorHolder() }

    // Provide dependencies down the tree. This pattern is used for cross-cutting
    // concerns like navigation and global UI states
    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalNavController provides navController,
        LocalSnackBarController provides snackBarController,
        LocalDialogController provides dialogController,
        LocalTopBarScrollBehavior provides topBarScrollHolder
    ) {
        // The main container that handles the Scaffold, BottomBar, and Navigation Host.
        MainScreen(
            navController = navController,
            features = features,
            topBarProviders = topBarProviders
        )
    }
}
