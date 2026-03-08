package com.kuro.notiflow.presentation.settings.ui.notification_filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.presentation.settings.R
import com.kuro.notiflow.presentation.settings.ui.notification_filters.components.NotificationFilterAppItem
import com.kuro.notiflow.presentation.settings.ui.notification_filters.components.NotificationFiltersEmptyContent
import com.kuro.notiflow.presentation.settings.ui.notification_filters.components.NotificationFiltersLoadingContent

@Composable
internal fun NotificationFiltersScreen(
    viewModel: NotificationFiltersViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val installedPackageNames = remember(state.apps) {
        state.apps.asSequence().map { app -> app.packageName }.toSet()
    }
    val displayedApps = remember(state.apps, state.blockedPackages, state.viewType) {
        when (state.viewType) {
            NotificationFiltersViewType.ALL_APPS -> state.apps
            NotificationFiltersViewType.ALLOW_LIST ->
                state.apps.filter { app -> app.packageName !in state.blockedPackages }
            NotificationFiltersViewType.BLOCKED_LIST ->
                state.apps.filter { app -> app.packageName in state.blockedPackages }
        }
    }
    val hiddenBlockedApps = remember(state.blockedPackages, installedPackageNames) {
        state.blockedPackages
            .asSequence()
            .filter { packageName -> packageName !in installedPackageNames }
            .sorted()
            .map { packageName ->
                AppSelectionItem(
                    packageName = packageName,
                    appName = packageName
                )
            }
            .toList()
    }
    val hasVisibleContent = displayedApps.isNotEmpty() ||
        (state.viewType == NotificationFiltersViewType.BLOCKED_LIST && hiddenBlockedApps.isNotEmpty())

    when {
        state.isLoading -> NotificationFiltersLoadingContent()
        !hasVisibleContent -> NotificationFiltersEmptyContent(viewType = state.viewType)
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(R.string.notification_filters_screen_desc),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                items(
                    items = displayedApps,
                    key = { it.packageName },
                    contentType = { APP_ITEM_CONTENT_TYPE }
                ) { app ->
                    NotificationFilterAppItem(
                        app = app,
                        viewType = state.viewType,
                        isBlocked = app.packageName in state.blockedPackages,
                        onSetBlocked = { isBlocked ->
                            viewModel.setAppBlocked(app, isBlocked)
                        }
                    )
                }
                if (state.viewType == NotificationFiltersViewType.BLOCKED_LIST &&
                    hiddenBlockedApps.isNotEmpty()
                ) {
                    item(contentType = DIVIDER_CONTENT_TYPE) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                    item(contentType = HIDDEN_SECTION_HEADER_CONTENT_TYPE) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = stringResource(R.string.notification_filters_hidden_blocked_section),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    items(
                        items = hiddenBlockedApps,
                        key = { it.packageName },
                        contentType = { HIDDEN_APP_ITEM_CONTENT_TYPE }
                    ) { app ->
                        NotificationFilterAppItem(
                            app = app,
                            viewType = NotificationFiltersViewType.BLOCKED_LIST,
                            isBlocked = true,
                            onSetBlocked = { isBlocked ->
                                viewModel.setAppBlocked(app, isBlocked)
                            }
                        )
                    }
                }
            }
        }
    }
}

private const val APP_ITEM_CONTENT_TYPE = "app_item"
private const val DIVIDER_CONTENT_TYPE = "divider"
private const val HIDDEN_SECTION_HEADER_CONTENT_TYPE = "hidden_section_header"
private const val HIDDEN_APP_ITEM_CONTENT_TYPE = "hidden_app_item"
