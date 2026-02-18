package com.kuro.notiflow.presentation.common.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

val SIZE = 40.dp
val SHAPE = CircleShape
const val DisabledIconOpacity = 0.38f

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ExtendedIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    val containerColor: Color = Color.Transparent
    val contentColor: Color = LocalContentColor.current
    val disabledContainerColor: Color = Color.Transparent
    val disabledContentColor: Color = contentColor.copy(alpha = DisabledIconOpacity)

    val backgroundColor = if (enabled) containerColor else disabledContainerColor

    // Copied from `IconButton` with some modifications
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .size(SIZE)
            .clip(SHAPE)
            .background(color = backgroundColor)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                onDoubleClick = onDoubleClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = LocalIndication.current,
            ),
        contentAlignment = Alignment.Center,
    ) {
        val color = if (enabled) contentColor else disabledContentColor
        CompositionLocalProvider(LocalContentColor provides color, content = content)
    }
}
