package com.kuro.notiflow.presentation.notifications.ui.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.presentation.common.R as CommonR
import com.kuro.notiflow.presentation.common.extensions.getAppName
import com.kuro.notiflow.presentation.common.ui.dialog.SelectionDialog
import com.kuro.notiflow.presentation.common.view.PackageIconImage
import com.kuro.notiflow.presentation.notifications.R

private const val END_OF_DAY_OFFSET_MILLIS = 86_399_999L

@Composable
internal fun FilterDialogs(
    draftState: FilterDraftState,
    packageOptions: List<String>,
    onDismissDatePicker: () -> Unit,
    onSelectStartDate: (Long?) -> Unit,
    onSelectEndDate: (Long?) -> Unit,
    onDismissAppPicker: () -> Unit,
    onSelectPackage: (String?) -> Unit
) {
    FilterDatePickerDialog(
        activeDatePicker = draftState.activeDatePicker,
        startTime = draftState.startTime,
        endTime = draftState.endTime,
        onDismiss = onDismissDatePicker,
        onSelectStartDate = onSelectStartDate,
        onSelectEndDate = onSelectEndDate
    )
    FilterAppPickerDialog(
        isVisible = draftState.showAppPicker,
        packageOptions = packageOptions,
        selectedPackageName = draftState.packageName,
        onDismiss = onDismissAppPicker,
        onSelectPackage = onSelectPackage
    )
}

@Composable
private fun FilterDatePickerDialog(
    activeDatePicker: FilterDatePicker?,
    startTime: Long?,
    endTime: Long?,
    onDismiss: () -> Unit,
    onSelectStartDate: (Long?) -> Unit,
    onSelectEndDate: (Long?) -> Unit
) {
    when (activeDatePicker) {
        FilterDatePicker.START -> {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = startTime)
            DatePickerDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(onClick = { onSelectStartDate(datePickerState.selectedDateMillis) }) {
                        Text(text = stringResource(CommonR.string.confirmTitle))
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(CommonR.string.cancelTitle))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        FilterDatePicker.END -> {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = endTime)
            DatePickerDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(
                        onClick = {
                            onSelectEndDate(
                                datePickerState.selectedDateMillis?.let { selected ->
                                    // End date is inclusive up to end of selected day.
                                    selected + END_OF_DAY_OFFSET_MILLIS
                                }
                            )
                        }
                    ) {
                        Text(text = stringResource(CommonR.string.confirmTitle))
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(CommonR.string.cancelTitle))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        null -> Unit
    }
}

@Composable
private fun FilterAppPickerDialog(
    isVisible: Boolean,
    packageOptions: List<String>,
    selectedPackageName: String?,
    onDismiss: () -> Unit,
    onSelectPackage: (String?) -> Unit
) {
    if (!isVisible) return

    val context = LocalContext.current
    SelectionDialog(
        title = stringResource(R.string.filter_app_title),
        onDismiss = onDismiss
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 320.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSelectPackage(null) },
                    colors = filterAppButtonColors(selectedPackageName == null)
                ) {
                    Text(text = stringResource(R.string.filter_app_all))
                }
            }
            items(packageOptions) { packageName ->
                val appName = packageName.getAppName(context)
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSelectPackage(packageName) },
                    colors = filterAppButtonColors(selectedPackageName == packageName)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PackageIconImage(
                            packageName = packageName,
                            modifier = Modifier.size(20.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(text = appName)
                            if (appName != packageName) {
                                Text(
                                    text = packageName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun filterAppButtonColors(isSelected: Boolean) =
    if (isSelected) {
        ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    } else {
        ButtonDefaults.outlinedButtonColors()
    }
