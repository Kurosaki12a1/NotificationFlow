package com.kuro.notiflow.presentation.notifications

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.notifications.ui.details.NotificationDetailsScreen
import com.kuro.notiflow.presentation.notifications.ui.filter.FilterScreen
import com.kuro.notiflow.presentation.notifications.ui.main.NotificationsScreen
import com.kuro.notiflow.presentation.notifications.ui.main.NotificationsViewModel
import jakarta.inject.Inject

class NotificationsFeatureNav @Inject constructor() : FeatureNav {

    override fun register(builder: NavGraphBuilder) {
        builder.navigation<Graph.NotificationsGraph>(
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
                NotificationDetailsScreen(args.notificationId)
            }
        }
    }

}
