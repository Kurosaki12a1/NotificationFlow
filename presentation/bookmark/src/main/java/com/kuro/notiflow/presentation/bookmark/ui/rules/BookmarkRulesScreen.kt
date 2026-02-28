package com.kuro.notiflow.presentation.bookmark.ui.rules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuro.notiflow.presentation.bookmark.R
import com.kuro.notiflow.presentation.bookmark.ui.rules.components.BookmarkRuleEditor
import com.kuro.notiflow.presentation.bookmark.ui.rules.components.BookmarkRuleItem
import com.kuro.notiflow.presentation.common.ui.dialog.ConfirmDialogSpec
import com.kuro.notiflow.presentation.common.ui.local.LocalDialogController
import com.kuro.notiflow.presentation.common.ui.local.LocalSnackBarController
import com.kuro.notiflow.presentation.common.R as CommonR
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
internal fun BookmarkRulesScreen(
    viewModel: BookmarkRulesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
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
            .fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(bottom = 16.dp),
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
                    onEditClick = {
                        viewModel.onEditRule(rule)
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
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
