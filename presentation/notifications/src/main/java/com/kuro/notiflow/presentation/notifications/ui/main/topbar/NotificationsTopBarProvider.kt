package com.kuro.notiflow.presentation.notifications.ui.main.topbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.notiflow.navigation.NavigationConstants.Destination.NOTIFICATIONS
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.local.LocalNavController
import com.kuro.notiflow.presentation.notifications.ui.main.NotificationsViewModel
import javax.inject.Inject
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.navigation.model.Screen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import com.kuro.notiflow.presentation.common.ui.local.LocalTopBarScrollBehavior

class NotificationsTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String
        get() = NOTIFICATIONS

    @Composable
    override fun Render() {
        val navController = LocalNavController.current
        val navigator = LocalNavigator.current
        val backStackEntry by navController.currentBackStackEntryAsState()
        backStackEntry?.let {
            val notificationViewModel = hiltViewModel<NotificationsViewModel>(it)

            val totalNotifications by notificationViewModel.overviewNotificationStats.collectAsStateWithLifecycle()
            NotificationTopBarWithScroll(
                totalNotifications = totalNotifications,
                onFilterClick = { navigator.navigateTo(Screen.Filter) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationTopBarWithScroll(
    totalNotifications: Int,
    onFilterClick: () -> Unit
) {
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

    NotificationsTopAppBar(
        totalNotifications = totalNotifications,
        onFilterClick = onFilterClick,
        scrollBehavior = scrollBehavior
    )
}
