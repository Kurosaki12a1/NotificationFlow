package com.kuro.notiflow.presentation.common.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.utils.FeatureNav

@Composable
fun MainNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    features: Set<FeatureNav>
) {
    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        navController = navController,
        startDestination = Graph.HomeGraph,

        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },

        exitTransition = {
            fadeOut(animationSpec = tween(150))
        },

        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },

        popExitTransition = {
            fadeOut(animationSpec = tween(150))
        }
    ) {
        features.forEach { it.register(this) }
    }
}