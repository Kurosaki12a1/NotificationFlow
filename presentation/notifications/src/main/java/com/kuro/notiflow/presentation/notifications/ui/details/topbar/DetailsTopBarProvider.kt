package com.kuro.notiflow.presentation.notifications.ui.details.topbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.navigation.NavigationConstants.Destination.NOTIFICATION_DETAIL
import com.kuro.notiflow.presentation.common.AppScope
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.local.LocalNavController
import com.kuro.notiflow.presentation.notifications.ui.details.NotificationDetailsState
import com.kuro.notiflow.presentation.notifications.ui.details.NotificationDetailsViewModel
import javax.inject.Inject

class DetailsTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String = NOTIFICATION_DETAIL

    @Composable
    override fun AppScope.Render() {
        val navController = LocalNavController.current
        val backStackEntry by navController.currentBackStackEntryAsState()
        backStackEntry?.let {
            val viewModel: NotificationDetailsViewModel =
                hiltViewModel<NotificationDetailsViewModel>(it)
            val state by viewModel.state.collectAsStateWithLifecycle(NotificationDetailsState())
            DetailsTopAppBar(
                data = state.notification,
                onBackClick = {
                    AppLog.d(TAG, "back")
                    popBackStack()
                },
                onBookmarkClicked = {
                    val action = if (it) "bookmarkAdd" else "bookmarkRemove"
                    state.notification?.let { notification ->
                        AppLog.i(
                            TAG,
                            "$action id=${notification.id} pkg=${notification.packageName}"
                        )
                    }
                    viewModel.onBookmarkClicked(it)
                },
                onShareClicked = {
                    state.notification?.let { notification ->
                        AppLog.i(
                            TAG,
                            "share id=${notification.id} pkg=${notification.packageName}"
                        )
                    }
                    viewModel.onShareClicked()
                }
            )
        }
    }
}

private const val TAG = "DetailsTopBarProvider"
