package com.kuro.notiflow.presentation.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun NotificationDetails(
    notificationId: Long,
    viewModel: NotificationDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle(NotificationDetailsState())

    LaunchedEffect(Unit) {
        viewModel.getNotification(notificationId)
    }

    Column {
        Text(state.notification?.title ?: "")
    }
}