package com.kuro.notiflow.presentation.common.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface SegmentedButtonItem {
    val title: String
        @Composable
        @ReadOnlyComposable
        get
}

@Composable
fun <Item : SegmentedButtonItem> SegmentedButtons(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    items: Array<Item>,
    selectedItem: Item,
    onItemClick: (Item) -> Unit,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        items.forEachIndexed { index, item ->
            if (index == 0) {
                SegmentedButton(
                    modifier = Modifier.weight(1f),
                    enabled = enabled,
                    title = item.title,
                    isSelected = item == selectedItem,
                    shape = SegmentedButtonDefaults.firstButtonShape(),
                    onClick = { onItemClick.invoke(item) },
                )
            } else if (items.lastIndex == index) {
                SegmentedButton(
                    modifier = Modifier.weight(1f),
                    enabled = enabled,
                    title = item.title,
                    isSelected = item == selectedItem,
                    shape = SegmentedButtonDefaults.lastButtonShape(),
                    onClick = { onItemClick.invoke(item) },
                )
            } else {
                SegmentedButton(
                    modifier = Modifier.weight(1f),
                    enabled = enabled,
                    title = item.title,
                    isSelected = item == selectedItem,
                    shape = SegmentedButtonDefaults.centerButtonShape(),
                    onClick = { onItemClick.invoke(item) },
                )
            }
        }
    }
}


@Composable
fun SegmentedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    title: String,
    shape: Shape,
    isSelected: Boolean,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(SegmentedButtonDefaults.height),
        enabled = enabled,
        contentPadding = SegmentedButtonDefaults.contentPadding(),
        colors = SegmentedButtonDefaults.buttonColors(isSelected = isSelected),
        shape = shape,
    ) {
        if (isSelected) {
            Icon(
                modifier = Modifier
                    .size(SegmentedButtonDefaults.selectedIconSize)
                    .padding(end = SegmentedButtonDefaults.selectedIconPadding),
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun segmentedButtonCornerShape(
    cornerStart: Dp = 0.dp,
    cornerEnd: Dp = 0.dp,
) = RoundedCornerShape(
    topStart = cornerStart,
    bottomStart = cornerStart,
    topEnd = cornerEnd,
    bottomEnd = cornerEnd,
)

object SegmentedButtonDefaults {

    val height = 40.dp

    val selectedIconSize = 16.dp

    val selectedIconPadding = 4.dp

    private val horizontalContentPadding = 4.dp

    private val verticalContentPadding = 0.dp

    private val shapeCorner = 100.dp

    @Composable
    fun contentPadding(
        horizontal: Dp = horizontalContentPadding,
        vertical: Dp = verticalContentPadding,
    ) = PaddingValues(horizontal = horizontal, vertical = vertical)

    @Composable
    fun selectedButtonColors(): ButtonColors = ButtonDefaults.filledTonalButtonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )

    @Composable
    fun defaultButtonColors(): ButtonColors = ButtonDefaults.outlinedButtonColors()

    @Composable
    fun buttonColors(isSelected: Boolean): ButtonColors = if (isSelected) {
        selectedButtonColors()
    } else {
        defaultButtonColors()
    }

    @Composable
    fun firstButtonShape(corner: Dp = shapeCorner): RoundedCornerShape =
        segmentedButtonCornerShape(cornerStart = corner)

    @Composable
    fun centerButtonShape(corner: Dp = 0.dp): RoundedCornerShape =
        segmentedButtonCornerShape(cornerStart = corner, cornerEnd = corner)

    @Composable
    fun lastButtonShape(corner: Dp = shapeCorner): RoundedCornerShape =
        segmentedButtonCornerShape(cornerEnd = corner)
}