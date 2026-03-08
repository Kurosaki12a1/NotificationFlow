package com.kuro.notiflow.presentation.settings.ui.notification_filters.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.notifications.NotificationFilterMode
import com.kuro.notiflow.presentation.common.view.PackageIconImage
import com.kuro.notiflow.presentation.settings.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NotificationFilterAppItem(
    app: AppSelectionItem,
    mode: NotificationFilterMode,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit
) {
    val targetSelected = !isSelected
    val isModeEditable = mode != NotificationFilterMode.ALLOW_ALL

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = isModeEditable) { onSelectionChange(targetSelected) }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PackageIconImage(
                packageName = app.packageName,
                modifier = Modifier.size(24.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = app.appName,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = app.packageName,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
                Text(
                    text = statusLabel(mode = mode, isSelected = isSelected),
                    color = statusColor(mode = mode, isSelected = isSelected),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Button(
                enabled = isModeEditable,
                colors = actionButtonColors(mode = mode, isSelected = isSelected),
                onClick = { onSelectionChange(targetSelected) }
            ) {
                Text(text = actionLabel(mode = mode, isSelected = isSelected))
            }
        }
    }
}

@Composable
private fun actionButtonColors(
    mode: NotificationFilterMode,
    isSelected: Boolean
) = if (actionTargetsBlocked(mode, isSelected)) {
    ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError
    )
} else {
    ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
private fun statusLabel(mode: NotificationFilterMode, isSelected: Boolean): String {
    return when (mode) {
        NotificationFilterMode.BLOCK_LIST -> {
            if (isSelected) stringResource(R.string.notification_filters_status_blocked)
            else stringResource(R.string.notification_filters_status_allowed)
        }
        NotificationFilterMode.ALLOW_LIST -> {
            if (isSelected) stringResource(R.string.notification_filters_status_allowed)
            else stringResource(R.string.notification_filters_status_blocked)
        }
        NotificationFilterMode.ALLOW_ALL -> stringResource(R.string.notification_filters_status_allowed)
    }
}

@Composable
private fun actionLabel(mode: NotificationFilterMode, isSelected: Boolean): String {
    return when (mode) {
        NotificationFilterMode.BLOCK_LIST ->
            if (isSelected) stringResource(R.string.notification_filters_action_allow)
            else stringResource(R.string.notification_filters_action_block)
        NotificationFilterMode.ALLOW_LIST ->
            if (isSelected) stringResource(R.string.notification_filters_action_block)
            else stringResource(R.string.notification_filters_action_allow)
        NotificationFilterMode.ALLOW_ALL -> stringResource(R.string.notification_filters_action_not_available)
    }
}

@Composable
private fun statusColor(mode: NotificationFilterMode, isSelected: Boolean) =
    when (mode) {
        NotificationFilterMode.BLOCK_LIST ->
            if (isSelected) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        NotificationFilterMode.ALLOW_LIST ->
            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        NotificationFilterMode.ALLOW_ALL -> MaterialTheme.colorScheme.primary
    }

private fun actionTargetsBlocked(mode: NotificationFilterMode, isSelected: Boolean): Boolean {
    return when (mode) {
        NotificationFilterMode.BLOCK_LIST -> !isSelected
        NotificationFilterMode.ALLOW_LIST -> isSelected
        NotificationFilterMode.ALLOW_ALL -> false
    }
}
