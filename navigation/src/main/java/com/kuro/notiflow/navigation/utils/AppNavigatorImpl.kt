package com.kuro.notiflow.navigation.utils

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.kuro.notiflow.navigation.NavigationConstants
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.model.Screen

class AppNavigatorImpl(
    private val navController: NavHostController
) : AppNavigator {

    private var lastNavigateTime = 0L

    override fun navigateGraph(
        graph: Graph
    ) {
        navController.navigate(graph) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    override fun navigateTo(
        route: Screen,
        popUpTo: Screen?,
        isInclusive: Boolean,
        isSingleTop: Boolean
    ) {
        if (!canNavigate(route)) return
        navController.navigate(route) {
            launchSingleTop = isSingleTop
            popUpTo?.let { popUpToRoute ->
                popUpTo(popUpToRoute) {
                    inclusive = isInclusive
                }
            }
        }
    }

    override fun navigateBack(route: Screen, inclusive: Boolean) {
        if (!canNavigate()) return
        navController.popBackStack(route, inclusive)
    }

    override fun popBackStack() {
        if (!canNavigate()) return
        navController.popBackStack()
    }

    private fun canNavigate(screen: Screen? = null): Boolean {
        if (screen != null && navController.currentDestination?.route == screen.toString()) {
            return false
        }
        val now = System.currentTimeMillis()
        return if (now - lastNavigateTime >= NavigationConstants.Delay.NAVIGATE) {
            lastNavigateTime = now
            true
        } else {
            false
        }
    }
}
