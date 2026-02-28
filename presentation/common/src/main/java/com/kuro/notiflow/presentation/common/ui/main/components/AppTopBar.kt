package com.kuro.notiflow.presentation.common.ui.main.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.notiflow.presentation.common.AppScope
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider

@Composable
fun AppTopBar(
    appScope: AppScope,
    navController: NavHostController,
    providers: Set<TopBarProvider>
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route
    providers
        // Prefer the most specific route match. Nested destinations such as
        // "BookmarkRules" also contain "Bookmark", so a plain first match can
        // resolve to the wrong provider.
        .filter { route?.contains(it.route) == true }
        .maxByOrNull { it.route.length }
        ?.let { provider ->
        // TopBarProvider.Render() is a member extension function:
        // - the provider instance is the dispatch receiver
        // - AppScope is the extension receiver
        // Both receivers must be in scope for Kotlin to resolve the call.
        with(provider) {
            with(appScope) {
                Render()
            }
        }
    }
}

