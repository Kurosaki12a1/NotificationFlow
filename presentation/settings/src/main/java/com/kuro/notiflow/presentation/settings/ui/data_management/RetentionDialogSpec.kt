package com.kuro.notiflow.presentation.settings.ui.data_management

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kuro.notiflow.presentation.common.R as CommonR
import com.kuro.notiflow.presentation.common.ui.dialog.AppDialogSpec
import com.kuro.notiflow.presentation.common.ui.dialog.DialogController
import com.kuro.notiflow.presentation.settings.R
import kotlin.math.roundToInt

data class RetentionDialogSpec(
    val selectedMode: RetentionMode,
    val sliderDays: Int,
    val onModeChange: (RetentionMode) -> Unit,
    val onSliderDaysChange: (Int) -> Unit,
    val onConfirm: () -> Unit,
    val onCancel: () -> Unit
) : AppDialogSpec {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Render(controller: DialogController) {
        val maxDays = 90
        val currentMode = selectedMode
        val currentDays = sliderDays.coerceIn(1, maxDays)
        val sliderValue = currentDays.toFloat()

        val configuration = LocalConfiguration.current
        val maxDialogHeight = (configuration.screenHeightDp * 0.8f).dp
        val scrollState = rememberScrollState()

        Dialog(
            onDismissRequest = {
                onCancel()
                controller.hide()
            }
        ) {
            Surface(
                modifier = Modifier.heightIn(max = maxDialogHeight),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.data_management_keep_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .verticalScroll(scrollState)
                            .padding(top = 12.dp, bottom = 12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            RetentionOptionRow(
                                option = RetentionOption.Always,
                                selected = currentMode == RetentionMode.ALWAYS,
                                onSelect = { onModeChange(RetentionMode.ALWAYS) }
                            )
                            RetentionOptionRow(
                                option = RetentionOption.Days(maxDays),
                                selected = currentMode == RetentionMode.CUSTOM,
                                onSelect = { onModeChange(RetentionMode.CUSTOM) },
                                labelOverride = stringResource(R.string.data_management_keep_custom)
                            )
                            if (currentMode == RetentionMode.CUSTOM) {
                                Text(
                                    text = stringResource(
                                        R.string.data_management_days_value,
                                        sliderValue.roundToInt()
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Slider(
                                    value = sliderValue,
                                    onValueChange = { onSliderDaysChange(it.roundToInt()) },
                                    valueRange = 1f..maxDays.toFloat(),
                                    steps = 0,
                                    colors = SliderDefaults.colors(
                                        thumbColor = MaterialTheme.colorScheme.primary,
                                        activeTrackColor = MaterialTheme.colorScheme.primary,
                                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                                    ),
                                    thumb = {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = CircleShape
                                                )
                                        )
                                    },
                                    track = { sliderState ->
                                        val range = sliderState.valueRange
                                        val fraction = if (range.endInclusive == range.start) {
                                            0f
                                        } else {
                                            (sliderState.value - range.start) /
                                                    (range.endInclusive - range.start)
                                        }
                                        val activeRangeEnd = fraction.coerceIn(0f, 1f)
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth(activeRangeEnd)
                                                    .heightIn(min = 4.dp)
                                                    .background(
                                                        color = MaterialTheme.colorScheme.primary,
                                                        shape = CircleShape
                                                    )
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(min = 4.dp)
                                                    .background(
                                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                                        shape = CircleShape
                                                    )
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable {
                                    onCancel()
                                    controller.hide()
                                },
                            text = stringResource(CommonR.string.cancelTitle),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            modifier = Modifier
                                .clickable {
                                    onConfirm()
                                    controller.hide()
                                },
                            text = stringResource(CommonR.string.confirmTitle),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }

}

private sealed interface RetentionOption {
    data object Always : RetentionOption
    data class Days(val value: Int) : RetentionOption
}

@Composable
private fun RetentionOptionRow(
    option: RetentionOption,
    selected: Boolean,
    onSelect: () -> Unit,
    labelOverride: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = labelOverride ?: when (option) {
                RetentionOption.Always -> stringResource(R.string.data_management_keep_always)
                is RetentionOption.Days -> stringResource(
                    R.string.data_management_days_value,
                    option.value
                )
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
