package com.kuro.notiflow.presentation.ui.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NotificationDetails(
    notificationId: Long,
    viewModel: NotificationDetailsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getNotification(notificationId)
    }
}