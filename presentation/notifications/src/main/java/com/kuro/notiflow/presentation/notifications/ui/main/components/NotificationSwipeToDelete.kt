package com.kuro.notiflow.presentation.notifications.ui.main.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.presentation.notifications.R
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun NotificationSwipeToDelete(
    notification: NotificationModel,
    isEven: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    var itemWidthPx by remember { mutableIntStateOf(0) }
    var itemHeightPx by remember { mutableIntStateOf(0) }
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var settleTargetX by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var pendingDelete by remember { mutableStateOf(false) }
    val onDeleteState by rememberUpdatedState(onDelete)

    val animatedOffsetX by animateFloatAsState(
        targetValue = if (isDragging) dragOffsetX else settleTargetX,
        animationSpec = if (isDragging) snap() else tween(240),
        finishedListener = { value ->
            if (!isDragging && pendingDelete && itemWidthPx > 0) {
                val isAtEdge = abs(value) >= itemWidthPx.toFloat()
                if (isAtEdge) {
                    pendingDelete = false
                    onDeleteState()
                }
            }
        }
    )

    val itemHeightDp = with(LocalDensity.current) { itemHeightPx.toDp() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged {
                itemWidthPx = it.width
                itemHeightPx = it.height
            }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    val next = (dragOffsetX + delta)
                        .coerceIn(-itemWidthPx.toFloat(), itemWidthPx.toFloat())
                    dragOffsetX = next
                    settleTargetX = next
                },
                onDragStarted = {
                    isDragging = true
                    pendingDelete = false
                    dragOffsetX = animatedOffsetX
                    settleTargetX = animatedOffsetX
                },
                onDragStopped = {
                    isDragging = false
                    val threshold = itemWidthPx * 0.5f
                    val shouldDelete = abs(dragOffsetX) > threshold
                    if (shouldDelete && itemWidthPx > 0) {
                        settleTargetX = if (dragOffsetX > 0f) {
                            itemWidthPx.toFloat()
                        } else {
                            -itemWidthPx.toFloat()
                        }
                        pendingDelete = true
                    } else {
                        settleTargetX = 0f
                        pendingDelete = false
                        dragOffsetX = 0f
                    }
                }
            )
    ) {
        NotificationDeleteBackground(
            offsetX = animatedOffsetX,
            itemWidthPx = itemWidthPx,
            itemHeightDp = itemHeightDp
        )
        NotificationRowItem(
            modifier = Modifier.offset { IntOffset(animatedOffsetX.roundToInt(), 0) },
            notification = notification,
            isEven = isEven,
            onClick = onClick
        )
    }
}

@Composable
private fun BoxWithConstraintsScope.NotificationDeleteBackground(
    offsetX: Float,
    itemWidthPx: Int,
    itemHeightDp: Dp,
) {
    val revealedFraction by remember(offsetX, itemWidthPx) {
        derivedStateOf {
            if (itemWidthPx > 0) abs(offsetX) / itemWidthPx.toFloat() else 0f
        }
    }
    if (revealedFraction <= 0f) return
    val alignment = if (offsetX >= 0f) Alignment.CenterStart else Alignment.CenterEnd
    val revealedWidth = maxWidth * revealedFraction
    val showText = itemHeightDp >= 48.dp
    val iconSize = if (itemHeightDp < 48.dp) 20.dp else 24.dp
    val contentPadding = if (itemHeightDp < 48.dp) 4.dp else 8.dp

    Column(
        modifier = Modifier
            .width(revealedWidth)
            .height(itemHeightDp)
            .align(alignment)
            .background(MaterialTheme.colorScheme.errorContainer, NotificationRowShape)
            .padding(contentPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onErrorContainer
        )
        if (showText) {
            Text(
                text = stringResource(R.string.delete),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

private val NotificationRowShape = RoundedCornerShape(16.dp)
