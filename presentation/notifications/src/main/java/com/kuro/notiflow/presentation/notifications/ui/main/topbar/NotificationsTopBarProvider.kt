package com.kuro.notiflow.presentation.notifications.ui.main.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.notiflow.navigation.NavigationConstants.Destination.NOTIFICATIONS
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.presentation.common.AppScope
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.local.LocalNavController
import com.kuro.notiflow.presentation.common.ui.local.LocalTopBarScrollBehavior
import com.kuro.notiflow.presentation.notifications.ui.main.NotificationsViewModel
import javax.inject.Inject

class NotificationsTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String = NOTIFICATIONS

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun AppScope.Render() {
        val navController = LocalNavController.current
        val backStackEntry by navController.currentBackStackEntryAsState()

        val scrollHolder = LocalTopBarScrollBehavior.current
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        DisposableEffect(scrollHolder) {
            scrollHolder.behavior = scrollBehavior
            onDispose {
                if (scrollHolder.behavior == scrollBehavior) {
                    scrollHolder.behavior = null
                }
            }
        }
        backStackEntry?.let {
            val notificationViewModel = hiltViewModel<NotificationsViewModel>(it)

            val totalNotifications by notificationViewModel.overviewNotificationStats.collectAsStateWithLifecycle()
            NotificationsTopAppBar(
                totalNotifications = totalNotifications,
                onFilterClick = { navigateTo(Screen.Filter) },
                scrollBehavior = scrollBehavior
            )
        }
    }
}
