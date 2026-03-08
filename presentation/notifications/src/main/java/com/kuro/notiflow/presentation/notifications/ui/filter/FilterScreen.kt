package com.kuro.notiflow.presentation.notifications.ui.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuro.notiflow.domain.models.notifications.NotificationReadFilter
import com.kuro.notiflow.domain.models.notifications.NotificationTimeFilter
import com.kuro.notiflow.presentation.common.R as CommonR
import com.kuro.notiflow.presentation.common.extensions.getAppName
import com.kuro.notiflow.presentation.common.ui.dialog.SelectionDialog
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.presentation.common.view.ChoiceButtonFlowRow
import com.kuro.notiflow.presentation.common.view.PackageIconImage
import com.kuro.notiflow.presentation.notifications.R
import com.kuro.notiflow.presentation.notifications.ui.main.NotificationsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun FilterScreen(
    viewModel: NotificationsViewModel
) {
    val context = LocalContext.current
    val navigator = LocalNavigator.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showAppPicker by remember { mutableStateOf(false) }
    var draftPackageName by remember { mutableStateOf<String?>(null) }
    var draftReadFilter by remember { mutableStateOf(NotificationReadFilter.ALL) }
    var draftTimeFilter by remember { mutableStateOf(NotificationTimeFilter.ALL) }
    var draftStartTime by remember { mutableStateOf<Long?>(null) }
    var draftEndTime by remember { mutableStateOf<Long?>(null) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.refreshPackageOptions()
    }
    LaunchedEffect(
        state.selectedPackageName,
        state.readFilter,
        state.timeFilter,
        state.customStartTime,
        state.customEndTime
    ) {
        draftPackageName = state.selectedPackageName
        draftReadFilter = state.readFilter
        draftTimeFilter = state.timeFilter
        draftStartTime = state.customStartTime
        draftEndTime = state.customEndTime
    }

    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = draftStartTime)
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        draftStartTime = datePickerState.selectedDateMillis
                        showStartDatePicker = false
                    }
                ) {
                    Text(text = stringResource(CommonR.string.confirmTitle))
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text(text = stringResource(CommonR.string.cancelTitle))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = draftEndTime)
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        draftEndTime = datePickerState.selectedDateMillis?.let { selected ->
                            // End date is inclusive up to end of selected day.
                            selected + 86_399_999L
                        }
                        showEndDatePicker = false
                    }
                ) {
                    Text(text = stringResource(CommonR.string.confirmTitle))
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text(text = stringResource(CommonR.string.cancelTitle))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showAppPicker) {
        SelectionDialog(
            title = stringResource(R.string.filter_app_title),
            onDismiss = { showAppPicker = false }
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
                        onClick = {
                            draftPackageName = null
                            showAppPicker = false
                        },
                        colors = if (draftPackageName == null) {
                            ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        } else {
                            ButtonDefaults.outlinedButtonColors()
                        }
                    ) {
                        Text(text = stringResource(R.string.filter_app_all))
                    }
                }
                items(state.packageOptions) { packageName ->
                    val appName = packageName.getAppName(context)
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            draftPackageName = packageName
                            showAppPicker = false
                        },
                        colors = if (draftPackageName == packageName) {
                            ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        } else {
                            ButtonDefaults.outlinedButtonColors()
                        }
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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.filter_app_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showAppPicker = true }
                ) {
                    Text(
                        text = draftPackageName ?: stringResource(R.string.filter_app_all)
                    )
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.filter_read_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                ChoiceButtonFlowRow(
                    items = NotificationReadFilter.entries,
                    selectedItem = draftReadFilter,
                    itemLabel = { filter ->
                        when (filter) {
                            NotificationReadFilter.ALL ->
                                stringResource(R.string.filter_read_all)
                            NotificationReadFilter.READ ->
                                stringResource(R.string.filter_read_read)
                            NotificationReadFilter.UNREAD ->
                                stringResource(R.string.filter_read_unread)
                        }
                    },
                    onItemSelected = { selected ->
                        draftReadFilter = selected
                    }
                )
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.filter_time_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                ChoiceButtonFlowRow(
                    items = NotificationTimeFilter.entries,
                    selectedItem = draftTimeFilter,
                    itemLabel = { filter ->
                        when (filter) {
                            NotificationTimeFilter.ALL ->
                                stringResource(R.string.filter_time_all)
                            NotificationTimeFilter.TODAY ->
                                stringResource(R.string.filter_time_today)
                            NotificationTimeFilter.THIS_WEEK ->
                                stringResource(R.string.filter_time_this_week)
                            NotificationTimeFilter.CUSTOM ->
                                stringResource(R.string.filter_time_custom)
                        }
                    },
                    onItemSelected = { selected ->
                        draftTimeFilter = selected
                        if (selected != NotificationTimeFilter.CUSTOM) {
                            draftStartTime = null
                            draftEndTime = null
                        }
                    }
                )
                if (draftTimeFilter == NotificationTimeFilter.CUSTOM) {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showStartDatePicker = true }
                    ) {
                        Text(
                            text = stringResource(
                                R.string.filter_time_start,
                                draftStartTime.toDateLabel()
                            )
                        )
                    }
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showEndDatePicker = true }
                    ) {
                        Text(
                            text = stringResource(
                                R.string.filter_time_end,
                                draftEndTime.toDateLabel()
                            )
                        )
                    }
                }
            }
        }
        item {
            val canApply = draftTimeFilter != NotificationTimeFilter.CUSTOM ||
                (draftStartTime != null &&
                    draftEndTime != null &&
                    draftStartTime!! <= draftEndTime!!)
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = canApply,
                onClick = {
                    viewModel.onPackageFilterChanged(draftPackageName)
                    viewModel.onReadFilterChanged(draftReadFilter)
                    viewModel.onTimeFilterChanged(draftTimeFilter)
                    viewModel.onCustomTimeRangeChanged(
                        startTime = if (draftTimeFilter == NotificationTimeFilter.CUSTOM) {
                            draftStartTime
                        } else {
                            null
                        },
                        endTime = if (draftTimeFilter == NotificationTimeFilter.CUSTOM) {
                            draftEndTime
                        } else {
                            null
                        }
                    )
                    navigator.popBackStack()
                }
            ) {
                Text(text = stringResource(R.string.filter_apply))
            }
        }
    }
}

private fun Long?.toDateLabel(): String {
    if (this == null) return "-"
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}
