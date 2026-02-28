package com.kuro.notiflow.presentation.bookmark.ui.rules.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchField
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchType
import com.kuro.notiflow.presentation.bookmark.R
import com.kuro.notiflow.presentation.bookmark.ui.rules.BookmarkRulesState
import com.kuro.notiflow.presentation.common.view.ChoiceButtonFlowRow
import com.kuro.notiflow.presentation.common.view.PackageIconImage

@Composable
internal fun BookmarkRuleEditor(
    state: BookmarkRulesState,
    onAppSelected: (AppSelectionItem?) -> Unit,
    onKeywordChanged: (String) -> Unit,
    onMatchFieldChanged: (BookmarkRuleMatchField) -> Unit,
    onMatchTypeChanged: (BookmarkRuleMatchType) -> Unit,
    onSaveClick: () -> Unit,
    onCancelEdit: () -> Unit
) {
    val availableApps = remember(state.availableApps, state.selectedApp) {
        buildList {
            state.selectedApp?.let { selectedApp ->
                if (state.availableApps.none { it.packageName == selectedApp.packageName }) {
                    add(selectedApp)
                }
            }
            addAll(state.availableApps)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
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
            Text(
                text = stringResource(R.string.bookmark_rules_effective_note),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            var isAppDialogVisible by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { isAppDialogVisible = true },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.selectedApp?.let { selectedApp ->
                            PackageIconImage(
                                packageName = selectedApp.packageName,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            modifier = Modifier.weight(1f),
                            text = state.selectedApp?.appName
                                ?: stringResource(R.string.bookmark_rules_all_apps),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.bookmark_rules_change_app),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            if (isAppDialogVisible) {
                AppSelectionDialog(
                    availableApps = availableApps,
                    selectedPackageName = state.selectedApp?.packageName,
                    onDismiss = { isAppDialogVisible = false },
                    onSelectApp = { selectedApp ->
                        onAppSelected(selectedApp)
                        isAppDialogVisible = false
                    }
                )
            }
            if (state.selectedApp != null) {
                Text(
                    text = state.selectedApp.packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else if (availableApps.isEmpty()) {
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
            ChoiceButtonFlowRow(
                items = BookmarkRuleMatchField.entries,
                selectedItem = state.matchField,
                itemLabel = { matchField -> bookmarkRuleMatchFieldLabel(matchField) },
                onItemSelected = onMatchFieldChanged
            )

            Text(
                text = stringResource(R.string.bookmark_rules_condition_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            ChoiceButtonFlowRow(
                items = BookmarkRuleMatchType.entries,
                selectedItem = state.matchType,
                itemLabel = { matchType -> bookmarkRuleMatchTypeLabel(matchType) },
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
                    enabled = !state.isSaving &&
                        (state.selectedApp != null || state.keyword.isNotBlank())
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
internal fun bookmarkRuleMatchFieldLabel(matchField: BookmarkRuleMatchField): String {
    return when (matchField) {
        BookmarkRuleMatchField.TITLE -> stringResource(R.string.bookmark_rules_field_title)
        BookmarkRuleMatchField.TEXT -> stringResource(R.string.bookmark_rules_field_text)
        BookmarkRuleMatchField.TITLE_OR_TEXT ->
            stringResource(R.string.bookmark_rules_field_title_or_text)
    }
}

@Composable
internal fun bookmarkRuleMatchTypeLabel(matchType: BookmarkRuleMatchType): String {
    return when (matchType) {
        BookmarkRuleMatchType.CONTAINS -> stringResource(R.string.bookmark_rules_condition_contains)
        BookmarkRuleMatchType.EQUALS -> stringResource(R.string.bookmark_rules_condition_equals)
        BookmarkRuleMatchType.STARTS_WITH ->
            stringResource(R.string.bookmark_rules_condition_starts_with)
    }
}
