package com.kuro.notiflow.presentation.ui.main.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.kuro.notiflow.navigation.NavigationConstants.Destination.FILTER
import com.kuro.notiflow.navigation.NavigationConstants.Destination.HOME
import com.kuro.notiflow.navigation.NavigationConstants.Destination.NOTIFICATIONS
import com.kuro.notiflow.navigation.NavigationConstants.Destination.NOTIFICATION_DETAIL
import com.kuro.notiflow.navigation.NavigationConstants.Destination.SETTINGS
import com.kuro.notiflow.presentation.common.extensions.getCurrentRoute
import com.kuro.notiflow.presentation.ui.details.NotificationDetailsState
import com.kuro.notiflow.presentation.ui.details.NotificationDetailsViewModel
import com.kuro.notiflow.presentation.ui.details.components.DetailsTopAppBar
import com.kuro.notiflow.presentation.ui.filter.components.FilterTopAppBar
import com.kuro.notiflow.presentation.ui.home.components.HomeTopAppBar
import com.kuro.notiflow.presentation.ui.notifications.NotificationsViewModel
import com.kuro.notiflow.presentation.ui.notifications.components.NotificationsTopAppBar
import com.kuro.notiflow.presentation.ui.settings.SettingsViewModel
import com.kuro.notiflow.presentation.ui.settings.components.SettingsTopAppBar

@Composable
fun AppTopBar(navBackStackEntry: NavBackStackEntry) {
    val notificationViewModel = hiltViewModel<NotificationsViewModel>(navBackStackEntry)
    when (navBackStackEntry.getCurrentRoute()) {
        null -> {
            // Do nothing
        }

        HOME -> {
            HomeTopAppBar()
        }

        NOTIFICATIONS -> {
            val totalNotifications by notificationViewModel.overviewNotificationStats.collectAsStateWithLifecycle()
            NotificationsTopAppBar(
                totalNotifications = totalNotifications
            )
        }

        SETTINGS -> {
            val viewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>(navBackStackEntry)
            SettingsTopAppBar(
                onResetToDefaultClick = {
                    viewModel.resetToDefault()
                }
            )
        }

        FILTER -> {
            FilterTopAppBar()
        }

        NOTIFICATION_DETAIL -> {
            val viewModel: NotificationDetailsViewModel =
                hiltViewModel<NotificationDetailsViewModel>(navBackStackEntry)
            val state by viewModel.state.collectAsStateWithLifecycle(NotificationDetailsState())
            DetailsTopAppBar(
                data = state.notification,
                onBookmarkClicked = {
                    viewModel.onBookmarkClicked(it)
                },
                onShareClicked = {
                    viewModel.onShareClicked(it)
                }
            )
        }
    }
}