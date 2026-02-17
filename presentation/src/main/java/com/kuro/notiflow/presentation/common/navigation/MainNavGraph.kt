package com.kuro.notiflow.presentation.common.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.presentation.ui.details.NotificationDetails
import com.kuro.notiflow.presentation.ui.filter.FilterScreen
import com.kuro.notiflow.presentation.ui.notifications.NotificationsScreen
import com.kuro.notiflow.presentation.ui.notifications.NotificationsViewModel
import kotlinx.serialization.Serializable

@Composable
fun MainNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        navController = navController,
        startDestination = Graph.HomeGraph,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, animationSpec =
                    tween(durationMillis = 160)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = 160)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = 160)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = 160)
            )
        }
    ) {
        homeNavGraph()
        notificationsNavGraph()
        settingsNavGraph()
    }
}