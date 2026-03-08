package com.kuro.notiflow.presentation.settings.ui.notification_filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.notifications.NotificationFilterMode
import com.kuro.notiflow.presentation.common.ui.local.LocalNavController
import com.kuro.notiflow.presentation.common.view.ChoiceButtonFlowRow
import com.kuro.notiflow.presentation.settings.R
import com.kuro.notiflow.presentation.settings.ui.notification_filters.components.NotificationFilterAppItem
import com.kuro.notiflow.presentation.settings.ui.notification_filters.components.NotificationFiltersLoadingContent
import kotlinx.coroutines.flow.collectLatest
import com.kuro.notiflow.presentation.common.R as CommonR

@Composable
internal fun NotificationFiltersScreen(
    viewModel: NotificationFiltersViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    var isDiscardDialogVisible by rememberSaveable { mutableStateOf(false) }
    var listFilter by rememberSaveable(state.mode) {
        mutableStateOf(NotificationFiltersListFilter.ALL)
    }

    fun handleExitRequest() {
        if (state.isDirty) {
            isDiscardDialogVisible = true
        } else {
            navController.popBackStack()
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { event ->
            when (event) {
                NotificationFiltersEvent.RequestExit -> handleExitRequest()
            }
        }
    }

    BackHandler(onBack = viewModel::onBackRequested)

    if (state.isLoading) {
        NotificationFiltersLoadingContent()
        return
    }

    val installedPackageNames = remember(state.apps) {
        state.apps.asSequence().map { app -> app.packageName }.toSet()
    }
    val hiddenSelectedApps = remember(state.selectedPackages, installedPackageNames, state.mode) {
        if (state.mode == NotificationFilterMode.ALLOW_ALL) {
            emptyList()
        } else {
            state.selectedPackages
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
    }
    val filteredApps = remember(state.apps, state.mode, state.selectedPackages, listFilter) {
        state.apps.filter { app ->
            matchesListFilter(
                listFilter = listFilter,
                mode = state.mode,
                isSelected = app.packageName in state.selectedPackages
            )
        }
    }
    val filteredHiddenSelectedApps = remember(hiddenSelectedApps, state.mode, listFilter) {
        hiddenSelectedApps.filter {
            matchesListFilter(
                listFilter = listFilter,
                mode = state.mode,
                isSelected = true
            )
        }
    }
    val hasVisibleItems = filteredApps.isNotEmpty() || filteredHiddenSelectedApps.isNotEmpty()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(contentType = MODE_SELECTOR_CONTENT_TYPE) {
            ModeSelectorSection(
                mode = state.mode,
                selectedCount = state.selectedPackages.size,
                isDirty = state.isDirty,
                onModeChanged = viewModel::onModeChanged,
                onApplyClick = viewModel::onApplyClick
            )
        }
        if (state.mode != NotificationFilterMode.ALLOW_ALL) {
            item(contentType = FILTER_SELECTOR_CONTENT_TYPE) {
                FilterSelectorSection(
                    listFilter = listFilter,
                    onFilterChanged = { listFilter = it }
                )
            }
        }
        if (state.apps.isEmpty()) {
            item(contentType = EMPTY_CONTENT_TYPE) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(R.string.notification_filters_empty),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else if (!hasVisibleItems) {
            item(contentType = FILTER_EMPTY_CONTENT_TYPE) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(R.string.notification_filters_filter_empty),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        items(
            items = filteredApps,
            key = { it.packageName },
            contentType = { APP_ITEM_CONTENT_TYPE }
        ) { app ->
            NotificationFilterAppItem(
                app = app,
                mode = state.mode,
                isSelected = app.packageName in state.selectedPackages,
                onSelectionChange = { isSelected ->
                    viewModel.onAppSelectionChanged(app, isSelected)
                }
            )
        }
        if (filteredHiddenSelectedApps.isNotEmpty()) {
            item(contentType = DIVIDER_CONTENT_TYPE) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
            item(contentType = HIDDEN_SECTION_HEADER_CONTENT_TYPE) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(R.string.notification_filters_hidden_selected_section),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            items(
                items = filteredHiddenSelectedApps,
                key = { it.packageName },
                contentType = { HIDDEN_APP_ITEM_CONTENT_TYPE }
            ) { app ->
                NotificationFilterAppItem(
                    app = app,
                    mode = state.mode,
                    isSelected = true,
                    onSelectionChange = { isSelected ->
                        viewModel.onAppSelectionChanged(app, isSelected)
                    }
                )
            }
        }
    }

    if (isDiscardDialogVisible) {
        AlertDialog(
            onDismissRequest = { isDiscardDialogVisible = false },
            title = {
                Text(text = stringResource(R.string.notification_filters_discard_dialog_title))
            },
            text = {
                Text(text = stringResource(R.string.notification_filters_discard_dialog_message))
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.discardDraftChanges()
                        isDiscardDialogVisible = false
                        navController.popBackStack()
                    }
                ) {
                    Text(text = stringResource(R.string.notification_filters_discard_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { isDiscardDialogVisible = false }
                ) {
                    Text(text = stringResource(CommonR.string.cancelTitle))
                }
            }
        )
    }
}

@Composable
private fun FilterSelectorSection(
    listFilter: NotificationFiltersListFilter,
    onFilterChanged: (NotificationFiltersListFilter) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.notification_filters_filter_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall
        )
        ChoiceButtonFlowRow(
            items = NotificationFiltersListFilter.entries,
            selectedItem = listFilter,
            itemLabel = { filter ->
                when (filter) {
                    NotificationFiltersListFilter.ALL ->
                        stringResource(R.string.notification_filters_filter_all)
                    NotificationFiltersListFilter.ALLOWED ->
                        stringResource(R.string.notification_filters_filter_allowed)
                    NotificationFiltersListFilter.BLOCKED ->
                        stringResource(R.string.notification_filters_filter_blocked)
                }
            },
            onItemSelected = onFilterChanged
        )
    }
}

@Composable
private fun ModeSelectorSection(
    mode: NotificationFilterMode,
    selectedCount: Int,
    isDirty: Boolean,
    onModeChanged: (NotificationFilterMode) -> Unit,
    onApplyClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.notification_filters_mode_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium
        )
        ChoiceButtonFlowRow(
            items = NotificationFilterMode.entries,
            selectedItem = mode,
            itemLabel = { filterMode ->
                when (filterMode) {
                    NotificationFilterMode.ALLOW_ALL ->
                        stringResource(R.string.notification_filters_mode_allow_all)
                    NotificationFilterMode.BLOCK_LIST ->
                        stringResource(R.string.notification_filters_mode_block_selected)
                    NotificationFilterMode.ALLOW_LIST ->
                        stringResource(R.string.notification_filters_mode_allow_selected)
                }
            },
            onItemSelected = onModeChanged
        )
        Text(
            text = when (mode) {
                NotificationFilterMode.ALLOW_ALL ->
                    stringResource(R.string.notification_filters_mode_desc_allow_all)
                NotificationFilterMode.BLOCK_LIST ->
                    stringResource(R.string.notification_filters_mode_desc_block_selected)
                NotificationFilterMode.ALLOW_LIST ->
                    stringResource(R.string.notification_filters_mode_desc_allow_selected)
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        if (mode != NotificationFilterMode.ALLOW_ALL) {
            Text(
                text = stringResource(R.string.notification_filters_selected_count, selectedCount),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = isDirty,
            onClick = onApplyClick
        ) {
            Text(text = stringResource(R.string.notification_filters_apply))
        }
    }
}

private enum class NotificationFiltersListFilter {
    ALL,
    ALLOWED,
    BLOCKED
}

private fun matchesListFilter(
    listFilter: NotificationFiltersListFilter,
    mode: NotificationFilterMode,
    isSelected: Boolean
): Boolean {
    val isAllowed = when (mode) {
        NotificationFilterMode.ALLOW_ALL -> true
        NotificationFilterMode.BLOCK_LIST -> !isSelected
        NotificationFilterMode.ALLOW_LIST -> isSelected
    }
    return when (listFilter) {
        NotificationFiltersListFilter.ALL -> true
        NotificationFiltersListFilter.ALLOWED -> isAllowed
        NotificationFiltersListFilter.BLOCKED -> !isAllowed
    }
}

private const val MODE_SELECTOR_CONTENT_TYPE = "mode_selector"
private const val FILTER_SELECTOR_CONTENT_TYPE = "filter_selector"
private const val EMPTY_CONTENT_TYPE = "empty_content"
private const val FILTER_EMPTY_CONTENT_TYPE = "filter_empty_content"
private const val APP_ITEM_CONTENT_TYPE = "app_item"
private const val DIVIDER_CONTENT_TYPE = "divider"
private const val HIDDEN_SECTION_HEADER_CONTENT_TYPE = "hidden_section_header"
private const val HIDDEN_APP_ITEM_CONTENT_TYPE = "hidden_app_item"
