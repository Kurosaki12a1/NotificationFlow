package com.kuro.notiflow.presentation.common.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.presentation.ui.home.HomeScreen

fun NavGraphBuilder.homeNavGraph() {
    navigation<Graph.HomeGraph>(
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen()
        }
    }
}