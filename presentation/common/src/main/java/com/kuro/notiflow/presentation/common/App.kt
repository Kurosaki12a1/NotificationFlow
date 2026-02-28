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
import androidx.compose.ui.platform.LocalResources


/**
 * Hosts the root Compose UI tree for the app.
 *
 * This composable creates the shared runtime objects used by the main screen:
 * navigation, snackbar handling, dialog handling, and top bar scroll state. It also
 * builds a stable [AppScope] so child composables can use common UI actions through
 * regular Kotlin calls instead of repeatedly resolving the same dependencies.
 *
 * @param features The set of feature navigation registrations contributed by modules.
 * @param topBarProviders The set of top bar providers contributed by feature modules.
 * @param dialogController The shared controller used to show and hide app dialogs.
 */
@Composable
fun App(
    features: Set<FeatureNav>,
    topBarProviders: Set<TopBarProvider>,
    dialogController: DialogController
) {
    val resources = LocalResources.current
    val navController = rememberNavController()
    // Keep navigation behind an app-specific abstraction so screens do not depend
    // directly on NavController APIs.
    val navigator = remember(navController) {
        AppNavigatorImpl(navController)
    }
    // These controllers live for the lifetime of the current app composition.
    val snackBarController = remember { SnackBarControllerImpl() }
    val topBarScrollHolder = remember { TopBarScrollBehaviorHolder() }
    // Resolve the shared UI services once, then expose them through a plain Kotlin scope.
    val appScope = remember(
        resources,
        navigator,
        dialogController,
        snackBarController
    ) {
        AppScopeInstance(
            resources = resources,
            navigator = navigator,
            dialogController = dialogController,
            snackBarController = snackBarController
        )
    }

    // Expose shared runtime services to the rest of the UI tree.
    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalNavController provides navController,
        LocalSnackBarController provides snackBarController,
        LocalDialogController provides dialogController,
        LocalTopBarScrollBehavior provides topBarScrollHolder
    ) {
        // Enter the scope receiver once at the root instead of threading it through
        // every child composable manually.
        with(appScope) {
            MainScreen(
                features = features,
                topBarProviders = topBarProviders
            )
        }
    }
}
