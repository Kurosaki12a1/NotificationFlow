package com.kuro.notiflow.presentation.settings.ui.data_management

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuro.notiflow.presentation.common.R as CommonR
import com.kuro.notiflow.presentation.settings.R
import com.kuro.notiflow.presentation.common.ui.dialog.ConfirmDialogSpec
import com.kuro.notiflow.presentation.common.ui.local.LocalDialogController
import com.kuro.notiflow.presentation.common.ui.local.LocalSnackBarHostState
import com.kuro.notiflow.presentation.settings.ui.data_management.components.DataManagementSection
import com.kuro.notiflow.presentation.settings.ui.data_management.components.DataRetentionSection
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DataManagementScreen(
    viewModel: DataManagementViewModel = hiltViewModel()
) {
    val scrollState = rememberLazyListState()
    val state by viewModel.state
    val snackBarHostState = LocalSnackBarHostState.current
    val context = LocalContext.current
    val dialogController = LocalDialogController.current

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is DataManagementEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = context.getString(event.messageResId)
                    )
                }
            }
        }
    }

    LaunchedEffect(state.isLoading) {
        if (state.isLoading) {
            dialogController.showLoading(message = context.getString(CommonR.string.loading))
        } else {
            dialogController.hideIfLoading()
        }
    }

    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            DataManagementSection(
                title = stringResource(R.string.data_management_import_title),
                description = stringResource(R.string.data_management_import_desc),
                onClick = { viewModel.onImportData() }
            )
        }
        item {
            DataManagementSection(
                title = stringResource(R.string.data_management_export_title),
                description = stringResource(R.string.data_management_export_desc),
                onClick = { viewModel.onExportData() }
            )
        }
        item {
            DataRetentionSection(
                title = stringResource(R.string.data_management_keep_title),
                description = stringResource(R.string.data_management_keep_desc),
                retentionValue = stringResource(
                    R.string.data_management_days_value,
                    state.retentionDays
                ),
                onRetentionClick = { viewModel.onRetentionClick() }
            )
        }
        item {
            DataManagementSection(
                title = stringResource(R.string.data_management_clear_title),
                description = stringResource(R.string.data_management_clear_desc),
                isDestructive = true,
                enabled = !state.isLoading,
                supportingText = if (state.isLoading) stringResource(CommonR.string.loading) else null,
                onClick = {
                    dialogController.show(
                        ConfirmDialogSpec(
                            title = context.getString(R.string.data_management_clear_confirm_title),
                            message = context.getString(R.string.data_management_clear_confirm_message),
                            confirmText = context.getString(CommonR.string.okConfirmTitle),
                            cancelText = context.getString(CommonR.string.cancelTitle),
                            onConfirm = { viewModel.onClearData() }
                        )
                    )
                }
            )
        }
    }
}
