package com.kuro.notiflow.presentation.common.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> ChoiceButtonFlowRow(
    items: List<T>,
    selectedItem: T,
    itemLabel: @Composable (T) -> String,
    onItemSelected: (T) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            val label = itemLabel(item)
            if (item == selectedItem) {
                Button(
                    onClick = { onItemSelected(item) }
                ) {
                    Text(
                        text = label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                OutlinedButton(
                    onClick = { onItemSelected(item) }
                ) {
                    Text(
                        text = label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
