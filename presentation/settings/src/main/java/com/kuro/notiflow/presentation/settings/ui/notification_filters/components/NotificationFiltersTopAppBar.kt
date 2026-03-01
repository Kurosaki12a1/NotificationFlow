package com.kuro.notiflow.presentation.settings.ui.notification_filters.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kuro.notiflow.presentation.common.R as CommonR
import com.kuro.notiflow.presentation.common.view.TopAppBarButton
import com.kuro.notiflow.presentation.settings.R
import com.kuro.notiflow.presentation.settings.ui.notification_filters.NotificationFiltersViewType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationFiltersTopAppBar(
    viewType: NotificationFiltersViewType,
    onBackClick: () -> Unit,
    onViewTypeChange: (NotificationFiltersViewType) -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            TopAppBarButton(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                imageDescription = stringResource(CommonR.string.back),
                onButtonClick = onBackClick
            )
        },
        actions = {
            IconButton(onClick = { isMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.notification_filters_view_all_apps)) },
                    trailingIcon = {
                        if (viewType == NotificationFiltersViewType.ALL_APPS) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null
                            )
                        }
                    },
                    onClick = {
                        isMenuExpanded = false
                        onViewTypeChange(NotificationFiltersViewType.ALL_APPS)
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.notification_filters_view_allow_list)) },
                    trailingIcon = {
                        if (viewType == NotificationFiltersViewType.ALLOW_LIST) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null
                            )
                        }
                    },
                    onClick = {
                        isMenuExpanded = false
                        onViewTypeChange(NotificationFiltersViewType.ALLOW_LIST)
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.notification_filters_view_blocked_list)) },
                    trailingIcon = {
                        if (viewType == NotificationFiltersViewType.BLOCKED_LIST) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null
                            )
                        }
                    },
                    onClick = {
                        isMenuExpanded = false
                        onViewTypeChange(NotificationFiltersViewType.BLOCKED_LIST)
                    }
                )
            }
        },
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = titleFor(viewType),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
private fun titleFor(viewType: NotificationFiltersViewType): String {
    return when (viewType) {
        NotificationFiltersViewType.ALL_APPS ->
            stringResource(R.string.notification_filters_view_all_apps)
        NotificationFiltersViewType.ALLOW_LIST ->
            stringResource(R.string.notification_filters_view_allow_list)
        NotificationFiltersViewType.BLOCKED_LIST ->
            stringResource(R.string.notification_filters_view_blocked_list)
    }
}
