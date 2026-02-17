package com.kuro.notiflow.presentation.common.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.presentation.ui.details.NotificationDetails
import com.kuro.notiflow.presentation.ui.filter.FilterScreen
import com.kuro.notiflow.presentation.ui.notifications.NotificationsScreen
import com.kuro.notiflow.presentation.ui.notifications.NotificationsViewModel

fun NavGraphBuilder.notificationsNavGraph() {
    navigation<Graph.NotificationsGraph>(
        startDestination = Screen.Notifications
    ) {
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