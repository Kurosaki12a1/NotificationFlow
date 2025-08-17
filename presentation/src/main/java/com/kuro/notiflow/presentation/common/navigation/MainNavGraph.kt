package com.kuro.notiflow.presentation.common.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuro.notiflow.presentation.ui.details.NotificationDetails
import com.kuro.notiflow.presentation.ui.filter.FilterScreen
import com.kuro.notiflow.presentation.ui.home.HomeScreen
import com.kuro.notiflow.presentation.ui.notifications.NotificationsScreen
import com.kuro.notiflow.presentation.ui.notifications.NotificationsViewModel
import com.kuro.notiflow.presentation.ui.settings.SettingsScreen

@Composable
fun MainNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        navController = navController,
        startDestination = Screen.Home,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, animationSpec =
                    tween(durationMillis = 280)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = 280)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = 280)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = 280)
            )
        }
    ) {
        composable<Screen.Home> {
            HomeScreen()
        }
        composable<Screen.Settings> {
            SettingsScreen()
        }
        composable<Screen.Notifications> {
            val viewModel: NotificationsViewModel = hiltViewModel<NotificationsViewModel>(it)
            NotificationsScreen(viewModel)
        }
        composable<Screen.Filter> {
            FilterScreen()
        }
        composable<Screen.NotificationDetail> {
            val args = it.toRoute<Screen.NotificationDetail>()
            NotificationDetails(args.notificationId)
        }
    }
}