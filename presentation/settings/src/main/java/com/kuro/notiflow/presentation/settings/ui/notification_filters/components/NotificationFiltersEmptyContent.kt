package com.kuro.notiflow.presentation.settings.ui.notification_filters.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.presentation.settings.R
import com.kuro.notiflow.presentation.settings.ui.notification_filters.NotificationFiltersViewType

@Composable
internal fun NotificationFiltersEmptyContent(
    viewType: NotificationFiltersViewType
) {
    val message = when (viewType) {
        NotificationFiltersViewType.ALL_APPS -> stringResource(R.string.notification_filters_empty)
        NotificationFiltersViewType.ALLOW_LIST ->
            stringResource(R.string.notification_filters_empty_allow_list)
        NotificationFiltersViewType.BLOCKED_LIST ->
            stringResource(R.string.notification_filters_empty_blocked_list)
    }
    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = message,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyMedium
    )
}
