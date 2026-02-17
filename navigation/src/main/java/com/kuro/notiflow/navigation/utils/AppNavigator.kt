package com.kuro.notiflow.navigation.utils

import android.annotation.SuppressLint
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.model.Screen

object AppNavigator {
    @SuppressLint("StaticFieldLeak")
    private var navController: NavHostController? = null

    private const val DELAY_NAVIGATE = 150L
    private var lastNavigateTime = 0L

    fun attachNavController(controller: NavHostController) {
        this.navController = controller
    }

    fun detachNavController() {
        this.navController = null
    }

    fun navigateGraphTo(
        graph : Graph
    ) {
        navController?.let { navController ->
            navController.navigate(graph) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    fun navigateTo(
        route: Screen,
        popUpTo: Screen? = null,
        isInclusive: Boolean = false,
        isSingleTop: Boolean = true
    ) {
        if (!canNavigate(route)) return
        navController?.navigate(route) {
            launchSingleTop = isSingleTop
            popUpTo?.let { popUpToRoute ->
                popUpTo(popUpToRoute) { inclusive = isInclusive }
            }
        }
    }

    fun popBackStack() {
        if (!canNavigate()) return
        navController?.popBackStack()
    }

    fun navigateBack(
        route: Screen,
        inclusive: Boolean = false
    ) {
        if (!canNavigate()) return
        navController?.popBackStack(route, inclusive)
    }

    private fun canNavigate(screen: Screen? = null): Boolean {
        if (screen != null && navController?.currentDestination?.route?.toString() == screen.toString()) {
            return false
        }
        val now = System.currentTimeMillis()
        return if (now - lastNavigateTime >= DELAY_NAVIGATE) {
            lastNavigateTime = now
            true
        } else {
            false
        }
    }
}