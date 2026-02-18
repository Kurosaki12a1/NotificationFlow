package com.kuro.notiflow.navigation.utils

import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.model.Screen

interface AppNavigator {
    fun navigateGraph(
        graph : Graph
    )

    fun navigateTo(
        route: Screen,
        popUpTo: Screen? = null,
        isInclusive: Boolean = false,
        isSingleTop: Boolean = true
    )

    fun navigateBack(
        route: Screen,
        inclusive: Boolean = false
    )

    fun popBackStack()
}