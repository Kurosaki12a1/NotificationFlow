package com.kuro.notiflow.presentation.ui.main.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.presentation.common.extensions.getCurrentRoute
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
            val totalNotificationsCount by viewModel.count.collectAsStateWithLifecycle(0)
            NotificationsTopAppBar(
                totalNotifications = totalNotificationsCount
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
    }
}