package com.kuro.notiflow.presentation.notifications.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.activity.compose.BackHandler
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.presentation.common.ui.local.LocalSnackBarController
import com.kuro.notiflow.presentation.common.ui.local.LocalBottomBarScrollVisibility
import com.kuro.notiflow.presentation.common.ui.local.LocalTopBarScrollBehavior
import com.kuro.notiflow.presentation.common.utils.SnackBarType
import com.kuro.notiflow.presentation.notifications.R
import com.kuro.notiflow.presentation.notifications.ui.main.components.NotificationSwipeToDelete
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.kuro.notiflow.presentation.common.R as CommonR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationsScreen(
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val data = viewModel.listNotifications.collectAsLazyPagingItems()
    val isEmpty = data.loadState.refresh is LoadState.NotLoading && data.itemCount == 0

    BackHandler(enabled = state.isSelectionMode) {
        viewModel.clearSelection()
    }

    if (isEmpty) {
        EmptyScreen()
        return
    }

    val navigator = LocalNavigator.current
    val snackBarController = LocalSnackBarController.current
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current
    val topBarScrollBehavior = LocalTopBarScrollBehavior.current.behavior
    val bottomBarScrollVisibility = LocalBottomBarScrollVisibility.current

    LaunchedEffect(viewModel, resources) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is NotificationsEvent.ShowSnackBar -> {
                    snackBarController.show(
                        message = resources.getQuantityString(
                            event.messageResId,
                            event.quantity,
                            event.quantity
                        ),
                        type = event.type
                    )
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .nestedScroll(bottomBarScrollVisibility.nestedScrollConnection)
            .let { modifier ->
                if (topBarScrollBehavior != null) {
                    modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection)
                } else {
                    modifier
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(data.itemCount, key = { data[it]?.id ?: 0L }) { index ->
            val item = data[index]
            if (item != null) {
                NotificationSwipeToDelete(
                    notification = item,
                    isSelected = item.id in state.selectedNotificationIds &&
                        item.id !in state.swipingNotificationIds,
                    isSelectionMode = state.isSelectionMode,
                    onClick = {
                        if (state.isSelectionMode) {
                            viewModel.toggleNotificationSelection(item.id)
                        } else {
                            AppLog.d(
                                TAG,
                                "openDetail id=${item.id} pkg=${item.packageName}"
                            )
                            navigator.navigateTo(Screen.NotificationDetail(item.id))
                        }
                    },
                    onLongClick = {
                        viewModel.toggleNotificationSelection(item.id)
                    },
                    onSwipeStateChange = { isSwiping ->
                        viewModel.onItemSwipeStateChanged(item.id, isSwiping)
                    },
                    onDelete = {
                        viewModel.onNotificationRemoved(item.id)
                        viewModel.deleteNotification(item.id)
                        scope.launch {
                            val result = snackBarController.showAction(
                                message = resources.getString(R.string.delete_notification_success),
                                actionLabel = resources.getString(CommonR.string.undoTitle),
                                type = SnackBarType.INFO
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.restoreNotification(item)
                            }
                        }
                    }, onBookmarkClick = { shouldBookmark ->
                        viewModel.setNotificationBookmark(item.id, shouldBookmark)
                    }
                )
            } else {
                Text(
                    text = stringResource(CommonR.string.loading),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private const val TAG = "NotificationsScreen"


@Composable
private fun EmptyScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_notifications),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
