package com.kuro.notiflow.presentation.notifications.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuro.notiflow.domain.Constants.Details.ACTION_KEY
import com.kuro.notiflow.domain.Constants.Details.DETAIL_KEY
import com.kuro.notiflow.domain.Constants.Details.GENERAL_KEY
import com.kuro.notiflow.presentation.common.ui.local.LocalSnackBarController
import com.kuro.notiflow.presentation.notifications.ui.details.components.ActionNotifications
import com.kuro.notiflow.presentation.notifications.ui.details.components.DetailsInformationNotifications
import com.kuro.notiflow.presentation.notifications.ui.details.components.GeneralNotifications
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NotificationDetailsScreen(
    notificationId: Long,
    viewModel: NotificationDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle(NotificationDetailsState())
    val snackBarController = LocalSnackBarController.current
    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.getNotification(notificationId)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is NotificationDetailsEvent.ShowSnackBar -> {
                    snackBarController.show(
                        message = resources.getString(event.messageResId),
                        type = event.type
                    )
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = GENERAL_KEY) {
            GeneralNotifications(state.notification)
        }
        item(key = DETAIL_KEY) {
            DetailsInformationNotifications(state.notification)
        }
        item(key = ACTION_KEY) {
            ActionNotifications(
                notification = state.notification,
                onSeeMore = { packageName -> viewModel.onSeeMoreClick(packageName) },
                onSaved = {

                },
                onDelete = {

                }
            )
        }
    }
}
