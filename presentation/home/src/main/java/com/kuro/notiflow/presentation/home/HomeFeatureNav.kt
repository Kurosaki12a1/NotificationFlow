package com.kuro.notiflow.presentation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.home.ui.HomeScreen
import jakarta.inject.Inject

class HomeFeatureNav @Inject constructor() : FeatureNav {
    override fun register(
        builder: NavGraphBuilder,
        navController: NavHostController
    ) {
        builder.navigation<Graph.HomeGraph>(
            startDestination = Screen.Home
        ) {
            composable<Screen.Home> {
                HomeScreen()
            }
        }
    }

}