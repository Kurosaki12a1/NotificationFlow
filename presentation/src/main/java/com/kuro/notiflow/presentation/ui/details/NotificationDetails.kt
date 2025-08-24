package com.kuro.notiflow.presentation.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuro.notiflow.domain.Constants.Details.GENERAL_KEY
import com.kuro.notiflow.presentation.ui.details.components.GeneralNotifications

@Composable
fun NotificationDetails(
    notificationId: Long,
    viewModel: NotificationDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle(NotificationDetailsState())

    LaunchedEffect(Unit) {
        viewModel.getNotification(notificationId)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = GENERAL_KEY) {
            GeneralNotifications(state.notification)
        }
    }
}