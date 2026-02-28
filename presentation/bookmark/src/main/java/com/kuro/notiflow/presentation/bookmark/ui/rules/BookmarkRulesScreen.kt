package com.kuro.notiflow.presentation.bookmark.ui.rules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuro.notiflow.presentation.common.ui.dialog.ConfirmDialogSpec
import com.kuro.notiflow.presentation.common.ui.local.LocalDialogController
import com.kuro.notiflow.presentation.common.ui.local.LocalSnackBarController
import com.kuro.notiflow.presentation.common.view.PackageIconImage
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.bookmark.BookmarkRule
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchField
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchType
import com.kuro.notiflow.presentation.bookmark.R
import com.kuro.notiflow.presentation.common.R as CommonR
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun BookmarkRulesScreen(
    viewModel: BookmarkRulesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarController = LocalSnackBarController.current
    val dialogController = LocalDialogController.current
    val resources = LocalResources.current

    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is BookmarkRulesEvent.ShowSnackBar -> {
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
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BookmarkRuleEditor(
                state = state,
                onAppSelected = viewModel::onAppSelected,
                onKeywordChanged = viewModel::onKeywordChanged,
                onMatchFieldChanged = viewModel::onMatchFieldChanged,
                onMatchTypeChanged = viewModel::onMatchTypeChanged,
                onSaveClick = viewModel::onSaveRule,
                onCancelEdit = viewModel::onCancelEdit
            )
        }
        item {
            Text(
                text = stringResource(R.string.bookmark_rules_saved_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (state.rules.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.bookmark_rules_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(state.rules, key = { it.id }) { rule ->
                BookmarkRuleItem(
                    appName = state.availableApps
                        .firstOrNull { it.packageName == rule.packageName }
                        ?.appName,
                    packageName = rule.packageName,
                    rule = rule,
                    onEditClick = { viewModel.onEditRule(rule) },
                    onEnabledChanged = { isEnabled ->
                        viewModel.onRuleEnabledChanged(rule, isEnabled)
                    },
                    onDeleteClick = {
                        dialogController.show(
                            ConfirmDialogSpec(
                                title = resources.getString(R.string.bookmark_rules_delete_confirm_title),
                                message = resources.getString(R.string.bookmark_rules_delete_confirm_message),
                                confirmText = resources.getString(CommonR.string.okConfirmTitle),
                                cancelText = resources.getString(CommonR.string.cancelTitle),
                                onConfirm = { viewModel.onDeleteRule(rule.id) }
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun BookmarkRuleEditor(
    state: BookmarkRulesState,
    onAppSelected: (AppSelectionItem?) -> Unit,
    onKeywordChanged: (String) -> Unit,
    onMatchFieldChanged: (BookmarkRuleMatchField) -> Unit,
    onMatchTypeChanged: (BookmarkRuleMatchType) -> Unit,
    onSaveClick: () -> Unit,
    onCancelEdit: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = if (state.editingRuleId == null) {
                    stringResource(R.string.bookmark_rules_create_title)
                } else {
                    stringResource(R.string.bookmark_rules_edit_title)
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.bookmark_rules_create_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            var appExpanded by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = state.selectedApp?.appName
                        ?: stringResource(R.string.bookmark_rules_all_apps),
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { appExpanded = true },
                    readOnly = true,
                    label = { Text(text = stringResource(R.string.bookmark_rules_app_label)) },
                    trailingIcon = {
                        Text(
                            text = stringResource(R.string.bookmark_rules_change_app),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                DropdownMenu(
                    expanded = appExpanded,
                    onDismissRequest = { appExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.bookmark_rules_all_apps)) },
                        onClick = {
                            onAppSelected(null)
                            appExpanded = false
                        }
                    )
                    state.availableApps.forEach { app ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    PackageIconImage(
                                        packageName = app.packageName,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = app.appName,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            },
                            onClick = {
                                onAppSelected(app)
                                appExpanded = false
                            }
                        )
                    }
                }
            }
            if (state.selectedApp != null) {
                Text(
                    text = state.selectedApp.packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else if (state.availableApps.isEmpty()) {
                Text(
                    text = stringResource(R.string.bookmark_rules_no_apps_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedTextField(
                value = state.keyword,
                onValueChange = onKeywordChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.bookmark_rules_keyword_label)) },
                singleLine = true
            )

            Text(
                text = stringResource(R.string.bookmark_rules_field_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FlowChoiceRow(
                items = BookmarkRuleMatchField.entries,
                selectedItem = state.matchField,
                itemLabel = { matchField -> matchFieldLabel(matchField) },
                onItemSelected = onMatchFieldChanged
            )

            Text(
                text = stringResource(R.string.bookmark_rules_condition_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FlowChoiceRow(
                items = BookmarkRuleMatchType.entries,
                selectedItem = state.matchType,
                itemLabel = { matchType -> matchTypeLabel(matchType) },
                onItemSelected = onMatchTypeChanged
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (state.editingRuleId != null) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onCancelEdit,
                        enabled = !state.isSaving
                    ) {
                        Text(text = stringResource(R.string.bookmark_rules_cancel_edit))
                    }
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onSaveClick,
                    enabled = !state.isSaving && state.keyword.isNotBlank()
                ) {
                    Text(
                        text = if (state.editingRuleId == null) {
                            stringResource(R.string.bookmark_rules_save)
                        } else {
                            stringResource(R.string.bookmark_rules_update)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun <T> FlowChoiceRow(
    items: List<T>,
    selectedItem: T,
    itemLabel: @Composable (T) -> String,
    onItemSelected: (T) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            FilterChip(
                selected = item == selectedItem,
                onClick = { onItemSelected(item) },
                label = { Text(text = itemLabel(item)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun BookmarkRuleItem(
    appName: String?,
    packageName: String?,
    rule: BookmarkRule,
    onEditClick: () -> Unit,
    onEnabledChanged: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!packageName.isNullOrBlank()) {
                    PackageIconImage(
                        packageName = packageName,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    modifier = Modifier.weight(1f),
                    text = appName ?: rule.packageName
                    ?: stringResource(R.string.bookmark_rules_all_apps),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Switch(
                    checked = rule.isEnabled,
                    onCheckedChange = onEnabledChanged
                )
            }
            Text(
                text = stringResource(
                    R.string.bookmark_rules_summary,
                    matchFieldLabel(rule.matchField),
                    matchTypeLabel(rule.matchType),
                    rule.keyword
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (!packageName.isNullOrBlank()) {
                Text(
                    text = packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onEditClick
                ) {
                    Text(text = stringResource(R.string.bookmark_rules_edit))
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onDeleteClick
                ) {
                    Text(text = stringResource(R.string.bookmark_rules_delete))
                }
            }
        }
    }
}

@Composable
private fun matchFieldLabel(matchField: BookmarkRuleMatchField): String {
    return when (matchField) {
        BookmarkRuleMatchField.TITLE -> stringResource(R.string.bookmark_rules_field_title)
        BookmarkRuleMatchField.TEXT -> stringResource(R.string.bookmark_rules_field_text)
        BookmarkRuleMatchField.TITLE_OR_TEXT ->
            stringResource(R.string.bookmark_rules_field_title_or_text)
    }
}

@Composable
private fun matchTypeLabel(matchType: BookmarkRuleMatchType): String {
    return when (matchType) {
        BookmarkRuleMatchType.CONTAINS -> stringResource(R.string.bookmark_rules_condition_contains)
        BookmarkRuleMatchType.EQUALS -> stringResource(R.string.bookmark_rules_condition_equals)
        BookmarkRuleMatchType.STARTS_WITH ->
            stringResource(R.string.bookmark_rules_condition_starts_with)
    }
}
