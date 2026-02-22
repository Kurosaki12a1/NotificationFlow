package com.kuro.notiflow.presentation.onboarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.onboarding.ui.OnboardingScreen
import jakarta.inject.Inject

class OnboardingFeatureNav @Inject constructor() : FeatureNav {
    override fun register(builder: NavGraphBuilder) {
        builder.navigation<Graph.OnboardingGraph>(
            startDestination = Screen.Onboarding
        ) {
            composable<Screen.Onboarding> {
                OnboardingScreen()
            }
        }
    }
}
