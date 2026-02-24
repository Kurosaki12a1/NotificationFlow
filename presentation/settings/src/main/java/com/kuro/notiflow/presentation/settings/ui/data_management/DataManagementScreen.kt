package com.kuro.notiflow.presentation.settings.ui.data_management

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.kuro.notiflow.presentation.common.ui.dialog.ConfirmDialogSpec
import com.kuro.notiflow.presentation.common.ui.local.LocalDialogController
import com.kuro.notiflow.presentation.common.ui.local.LocalSnackBarController
import com.kuro.notiflow.presentation.common.utils.ImportMimeTypes
import com.kuro.notiflow.presentation.common.utils.SnackBarType
import com.kuro.notiflow.presentation.common.utils.rememberCsvExportLauncher
import com.kuro.notiflow.presentation.common.utils.rememberImportLauncher
import com.kuro.notiflow.presentation.settings.R
import com.kuro.notiflow.presentation.settings.ui.data_management.components.DataManagementSection
import com.kuro.notiflow.presentation.settings.ui.data_management.components.DataRetentionSection
import com.kuro.notiflow.presentation.settings.ui.data_management.dialog.RetentionDialogSpec
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.kuro.notiflow.presentation.common.R as CommonR

@Composable
fun DataManagementScreen(
    viewModel: DataManagementViewModel = hiltViewModel()
) {
    val scrollState = rememberLazyListState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarController = LocalSnackBarController.current
    val resource = LocalResources.current
    val dialogController = LocalDialogController.current
    val coroutineScope = rememberCoroutineScope()
    val exportLauncher = rememberCsvExportLauncher { uri ->
        if (uri != null) {
            viewModel.onExportData(uri.toString())
        } else {
            coroutineScope.launch {
                snackBarController.show(
                    message = resource.getString(R.string.data_management_export_cancelled),
                    type = SnackBarType.ERROR
                )
            }
        }
    }
    val importLauncher = rememberImportLauncher { uri ->
        if (uri != null) {
            viewModel.onImportData(uri.toString())
        } else {
            coroutineScope.launch {
                snackBarController.show(
                    message = resource.getString(R.string.data_management_import_cancelled),
                    type = SnackBarType.ERROR
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is DataManagementEvent.ShowSnackBar -> {
                    val args = event.formatArgs.toTypedArray()
                    snackBarController.show(
                        message = if (args.isEmpty()) {
                            resource.getString(event.messageResId)
                        } else {
                            resource.getString(event.messageResId, *args)
                        },
                        type = event.type
                    )
                }
                is DataManagementEvent.RequestExport -> {
                    exportLauncher.launch(event.fileName)
                }
                is DataManagementEvent.RequestImport -> {
                    importLauncher.launch(ImportMimeTypes)
                }
            }
        }
    }

    LaunchedEffect(state.isLoading) {
        if (state.isLoading) {
            dialogController.showLoading(message = resource.getString(CommonR.string.loading))
        } else {
            dialogController.hideIfLoading()
        }
    }

    LaunchedEffect(
        state.isRetentionDialogVisible,
        state.dialogRetentionMode,
        state.dialogRetentionDays
    ) {
        if (state.isRetentionDialogVisible) {
            dialogController.show(
                RetentionDialogSpec(
                    selectedMode = state.dialogRetentionMode,
                    sliderDays = state.dialogRetentionDays,
                    onModeChange = { mode -> viewModel.onRetentionDialogModeChanged(mode) },
                    onSliderDaysChange = { days -> viewModel.onRetentionDialogDaysChanged(days) },
                    onConfirm = { viewModel.onRetentionDialogConfirm() },
                    onCancel = { viewModel.onRetentionDialogCancel() }
                )
            )
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
                onClick = { viewModel.onImportClick() }
            )
        }
        item {
            DataManagementSection(
                title = stringResource(R.string.data_management_export_title),
                description = stringResource(R.string.data_management_export_desc),
                onClick = {
                    viewModel.onExportClick()
                }
            )
        }
        item {
            DataRetentionSection(
                title = stringResource(R.string.data_management_keep_title),
                description = stringResource(R.string.data_management_keep_desc),
                retentionValue = if (state.retentionDays <= 0) {
                    stringResource(R.string.data_management_keep_always)
                } else {
                    stringResource(R.string.data_management_days_value, state.retentionDays)
                },
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
                            title = resource.getString(R.string.data_management_clear_confirm_title),
                            message = resource.getString(R.string.data_management_clear_confirm_message),
                            confirmText = resource.getString(CommonR.string.okConfirmTitle),
                            cancelText = resource.getString(CommonR.string.cancelTitle),
                            onConfirm = { viewModel.onClearData() }
                        )
                    )
                }
            )
        }
    }
}
