package com.kuro.notiflow.presentation.notifications.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.kuro.notiflow.domain.logger.AppLog
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.presentation.common.view.CustomLargeTextField
import com.kuro.notiflow.presentation.common.ui.local.LocalSnackBarController
import com.kuro.notiflow.presentation.common.utils.SnackBarType
import com.kuro.notiflow.presentation.notifications.R
import com.kuro.notiflow.presentation.notifications.ui.main.components.NotificationSwipeToDelete
import kotlinx.coroutines.launch
import com.kuro.notiflow.presentation.common.R as CommonR

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val data = viewModel.listNotifications.collectAsLazyPagingItems()
    val navigator = LocalNavigator.current
    val snackBarController = LocalSnackBarController.current
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomLargeTextField(
            text = "",
            onTextChange = {

            },
            label = { },
            placeholder = { Text(text = stringResource(R.string.search_hint)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            maxLines = 1,
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        AppLog.d(TAG, "openFilter")
                        navigator.navigateTo(Screen.Filter)
                    },
                    painter = painterResource(CommonR.drawable.ic_filter),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            background = MaterialTheme.colorScheme.background
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(data.itemCount, key = { data[it]?.id ?: 0L }) { index ->
                val item = data[index]
                if (item != null) {
                    NotificationSwipeToDelete(
                        notification = item,
                        isEven = index % 2 == 0,
                        onClick = { navigator.navigateTo(Screen.NotificationDetail(item.id)) },
                        onDelete = {
                            viewModel.deleteNotification(item.id)
                            scope.launch {
                                val result = snackBarController.showAction(
                                    message = resources.getString(R.string.delete_notification_success),
                                    actionLabel = resources.getString(CommonR.string.undoTitle),
                                    type = SnackBarType.INFO
                                )
                                if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                                    viewModel.restoreNotification(item)
                                }
                            }
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
}

private const val TAG = "NotificationsScreen"
