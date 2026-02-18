package com.kuro.notiflow.presentation.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.settings.ui.settings.SettingsScreen
import jakarta.inject.Inject

class SettingsFeatureNav @Inject constructor() : FeatureNav {
    override fun register(
        builder: NavGraphBuilder,
        navController: NavHostController
    ) {
        builder.navigation<Graph.SettingsGraph>(
            startDestination = Screen.Settings
        ) {
            composable<Screen.Settings> {
                SettingsScreen()
            }
        }
    }

}