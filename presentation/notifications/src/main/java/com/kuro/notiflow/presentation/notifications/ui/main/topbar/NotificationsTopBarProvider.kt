package com.kuro.notiflow.presentation.notifications.ui.main.topbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.notiflow.navigation.NavigationConstants.Destination.NOTIFICATIONS
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.local.LocalNavController
import com.kuro.notiflow.presentation.notifications.ui.main.NotificationsViewModel
import javax.inject.Inject

class NotificationsTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String
        get() = NOTIFICATIONS

    @Composable
    override fun Render() {
        val navController = LocalNavController.current
        val backStackEntry by navController.currentBackStackEntryAsState()
        backStackEntry?.let {
            val notificationViewModel = hiltViewModel<NotificationsViewModel>(it)

            val totalNotifications by notificationViewModel.overviewNotificationStats.collectAsStateWithLifecycle()
            NotificationsTopAppBar(
                totalNotifications = totalNotifications
            )
        }
    }
}
