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
import com.kuro.notiflow.presentation.common.view.PackageIconImage
import com.kuro.notiflow.presentation.settings.R
import com.kuro.notiflow.presentation.settings.ui.notification_filters.NotificationFiltersViewType

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NotificationFilterAppItem(
    app: AppSelectionItem,
    viewType: NotificationFiltersViewType,
    isBlocked: Boolean,
    onSetBlocked: (Boolean) -> Unit
) {
    val targetBlocked = actionTargetBlocked(viewType, isBlocked)

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
                .clickable { onSetBlocked(targetBlocked) }
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
                if (viewType == NotificationFiltersViewType.ALL_APPS) {
                    Text(
                        text = if (isBlocked) {
                            stringResource(R.string.notification_filters_status_blocked)
                        } else {
                            stringResource(R.string.notification_filters_status_allowed)
                        },
                        color = if (isBlocked) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Button(
                colors = actionButtonColors(targetBlocked),
                onClick = { onSetBlocked(targetBlocked) }
            ) {
                Text(
                    text = when (viewType) {
                        NotificationFiltersViewType.BLOCKED_LIST ->
                            stringResource(R.string.notification_filters_action_allow)
                        NotificationFiltersViewType.ALL_APPS ->
                            if (isBlocked) {
                                stringResource(R.string.notification_filters_action_allow)
                            } else {
                                stringResource(R.string.notification_filters_action_block)
                            }
                        NotificationFiltersViewType.ALLOW_LIST ->
                            stringResource(R.string.notification_filters_action_block)
                    }
                )
            }
        }
    }
}

@Composable
private fun actionButtonColors(
    targetBlocked: Boolean
) = if (targetBlocked) {
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

private fun actionTargetBlocked(
    viewType: NotificationFiltersViewType,
    isBlocked: Boolean
): Boolean {
    return when (viewType) {
        NotificationFiltersViewType.ALL_APPS -> !isBlocked
        NotificationFiltersViewType.ALLOW_LIST -> true
        NotificationFiltersViewType.BLOCKED_LIST -> false
    }
}
