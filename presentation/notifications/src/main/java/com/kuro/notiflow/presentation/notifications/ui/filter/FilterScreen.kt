package com.kuro.notiflow.presentation.notifications.ui.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuro.notiflow.domain.models.notifications.NotificationReadFilter
import com.kuro.notiflow.domain.models.notifications.NotificationTimeFilter
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.presentation.common.utils.Utils.formatFilterDateLabel
import com.kuro.notiflow.presentation.common.view.ChoiceButtonFlowRow
import com.kuro.notiflow.presentation.notifications.R
import com.kuro.notiflow.presentation.notifications.ui.main.NotificationsViewModel

@Composable
internal fun FilterScreen(
    notificationsViewModel: NotificationsViewModel,
    filterViewModel: FilterViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val state by notificationsViewModel.state.collectAsStateWithLifecycle()
    val draftState by filterViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        notificationsViewModel.refreshPackageOptions()
        filterViewModel.initializeFromApplied(
            packageName = state.selectedPackageName,
            readFilter = state.readFilter,
            timeFilter = state.timeFilter,
            startTime = state.customStartTime,
            endTime = state.customEndTime
        )
    }

    FilterDialogs(
        draftState = draftState,
        packageOptions = state.packageOptions,
        onDismissDatePicker = filterViewModel::dismissDatePicker,
        onSelectStartDate = filterViewModel::selectStartDate,
        onSelectEndDate = filterViewModel::selectEndDate,
        onDismissAppPicker = filterViewModel::dismissAppPicker,
        onSelectPackage = filterViewModel::selectPackage
    )

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
                    onClick = filterViewModel::showAppPicker
                ) {
                    Text(
                        text = draftState.packageName ?: stringResource(R.string.filter_app_all)
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
                    selectedItem = draftState.readFilter,
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
                    onItemSelected = filterViewModel::selectReadFilter
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
                    selectedItem = draftState.timeFilter,
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
                    onItemSelected = filterViewModel::selectTimeFilter
                )
                if (draftState.timeFilter == NotificationTimeFilter.CUSTOM) {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { filterViewModel.showDatePicker(FilterDatePicker.START) }
                    ) {
                        Text(
                            text = stringResource(
                                R.string.filter_time_start,
                                draftState.startTime.toDateLabel()
                            )
                        )
                    }
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { filterViewModel.showDatePicker(FilterDatePicker.END) }
                    ) {
                        Text(
                            text = stringResource(
                                R.string.filter_time_end,
                                draftState.endTime.toDateLabel()
                            )
                        )
                    }
                }
            }
        }
        item {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = draftState.canApply(),
                onClick = {
                    notificationsViewModel.onPackageFilterChanged(draftState.packageName)
                    notificationsViewModel.onReadFilterChanged(draftState.readFilter)
                    notificationsViewModel.onTimeFilterChanged(draftState.timeFilter)
                    notificationsViewModel.onCustomTimeRangeChanged(
                        startTime = if (draftState.timeFilter == NotificationTimeFilter.CUSTOM) {
                            draftState.startTime
                        } else {
                            null
                        },
                        endTime = if (draftState.timeFilter == NotificationTimeFilter.CUSTOM) {
                            draftState.endTime
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
    return formatFilterDateLabel(this)
}
