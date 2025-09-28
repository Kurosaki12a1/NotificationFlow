package com.kuro.notiflow.presentation.ui.main.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.kuro.notiflow.domain.Constants
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
fun AppTopBar(navBackStackEntry: NavBackStackEntry?) {
    when (navBackStackEntry.getCurrentRoute()) {
        null -> {
            // Do nothing
        }

        Constants.Destination.HOME -> {
            HomeTopAppBar()
        }

        Constants.Destination.NOTIFICATIONS -> {
            val viewModel: NotificationsViewModel =
                hiltViewModel<NotificationsViewModel>(navBackStackEntry!!)
            val totalNotifications by viewModel.overviewNotificationStats.collectAsStateWithLifecycle()
            println("Data...$totalNotifications")
            NotificationsTopAppBar(
                totalNotifications = totalNotifications
            )
        }

        Constants.Destination.SETTINGS -> {
            val viewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>(navBackStackEntry!!)
            SettingsTopAppBar(
                onResetToDefaultClick = {
                    viewModel.resetToDefault()
                }
            )
        }

        Constants.Destination.FILTER -> {
            FilterTopAppBar()
        }

        Constants.Destination.NOTIFICATION_DETAIL -> {
            val viewModel: NotificationDetailsViewModel =
                hiltViewModel<NotificationDetailsViewModel>(navBackStackEntry!!)
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