package com.kuro.notiflow.presentation.notifications.ui.main.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Stable
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.presentation.notifications.R
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun NotificationSwipeToDelete(
    notification: NotificationModel,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onSwipeStateChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onBookmarkClick: (Boolean) -> Unit,
) {
    val swipeState = rememberNotificationSwipeDeleteState()
    val onDeleteState by rememberUpdatedState(onDelete)
    val onSwipeStateChangeState by rememberUpdatedState(onSwipeStateChange)

    val animatedOffsetX by animateFloatAsState(
        targetValue = if (swipeState.isDragging) swipeState.dragOffsetX else swipeState.settleTargetX,
        animationSpec = if (swipeState.isDragging) snap() else tween(240),
        finishedListener = { value ->
            if (!swipeState.isDragging && swipeState.pendingDelete && swipeState.itemWidthPx > 0) {
                val isAtEdge = abs(value) >= swipeState.itemWidthPx.toFloat()
                if (isAtEdge) {
                    swipeState.pendingDelete = false
                    onSwipeStateChangeState(false)
                    onDeleteState()
                }
            } else if (!swipeState.isDragging && abs(value) < 1f) {
                onSwipeStateChangeState(false)
            }
        }
    )

    val itemHeightDp = with(LocalDensity.current) { swipeState.itemHeightPx.toDp() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged {
                swipeState.itemWidthPx = it.width
                swipeState.itemHeightPx = it.height
            }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    val next = (swipeState.dragOffsetX + delta).coerceIn(
                        -swipeState.itemWidthPx.toFloat(),
                        swipeState.itemWidthPx.toFloat()
                    )
                    swipeState.dragOffsetX = next
                    swipeState.settleTargetX = next
                },
                onDragStarted = {
                    swipeState.isDragging = true
                    swipeState.pendingDelete = false
                    swipeState.dragOffsetX = animatedOffsetX
                    swipeState.settleTargetX = animatedOffsetX
                    onSwipeStateChangeState(true)
                },
                onDragStopped = {
                    swipeState.isDragging = false
                    val threshold = swipeState.itemWidthPx * 0.5f
                    val shouldDelete = abs(swipeState.dragOffsetX) > threshold
                    if (shouldDelete && swipeState.itemWidthPx > 0) {
                        swipeState.settleTargetX = if (swipeState.dragOffsetX > 0f) {
                            swipeState.itemWidthPx.toFloat()
                        } else {
                            -swipeState.itemWidthPx.toFloat()
                        }
                        swipeState.pendingDelete = true
                    } else {
                        swipeState.settleTargetX = 0f
                        swipeState.pendingDelete = false
                        swipeState.dragOffsetX = 0f
                        onSwipeStateChangeState(false)
                    }
                }
            )
    ) {
        NotificationDeleteBackground(
            offsetX = animatedOffsetX,
            itemWidthPx = swipeState.itemWidthPx,
            itemHeightDp = itemHeightDp,
            description = notification.title
        )
        NotificationRowItem(
            modifier = Modifier.offset { IntOffset(animatedOffsetX.roundToInt(), 0) },
            notification = notification,
            isSelected = isSelected,
            isSelectionMode = isSelectionMode,
            onClick = onClick,
            onLongClick = onLongClick,
            onBookmarkClick = onBookmarkClick,
        )
    }
}

@Composable
private fun BoxWithConstraintsScope.NotificationDeleteBackground(
    offsetX: Float,
    itemWidthPx: Int,
    itemHeightDp: Dp,
    description: String?,
) {
    val revealedFraction by remember(offsetX, itemWidthPx) {
        derivedStateOf {
            if (itemWidthPx > 0) abs(offsetX) / itemWidthPx.toFloat() else 0f
        }
    }
    if (revealedFraction <= 0f) return
    val alignment = if (offsetX >= 0f) Alignment.CenterStart else Alignment.CenterEnd
    val revealedWidth = maxWidth * revealedFraction
    Column(
        modifier = Modifier
            .width(revealedWidth)
            .height(itemHeightDp)
            .align(alignment)
            .background(MaterialTheme.colorScheme.errorContainer, NotificationRowShape)
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onErrorContainer
        )
        if (!description.isNullOrBlank()) {
            Text(
                text = stringResource(R.string.delete),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun rememberNotificationSwipeDeleteState(): NotificationSwipeDeleteState {
    return remember { NotificationSwipeDeleteState() }
}

@Stable
private class NotificationSwipeDeleteState {
    var itemWidthPx by mutableIntStateOf(0)
    var itemHeightPx by mutableIntStateOf(0)
    var dragOffsetX by mutableFloatStateOf(0f)
    var settleTargetX by mutableFloatStateOf(0f)
    var isDragging by mutableStateOf(false)
    var pendingDelete by mutableStateOf(false)
}

private val NotificationRowShape = RoundedCornerShape(16.dp)
