package com.kuro.notiflow.presentation.common.vector

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Clock: ImageVector
    get() {
        if (clock != null) {
            return clock!!
        }
        clock = Builder(
            name = "Clock", defaultWidth = 32.0.dp, defaultHeight = 32.0.dp,
            viewportWidth = 32.0f, viewportHeight = 32.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF101820)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(16.0f, 31.0f)
                arcTo(15.0f, 15.0f, 0.0f, true, true, 31.0f, 16.0f)
                arcTo(15.0f, 15.0f, 0.0f, false, true, 16.0f, 31.0f)
                close()
                moveTo(16.0f, 3.0f)
                arcTo(13.0f, 13.0f, 0.0f, true, false, 29.0f, 16.0f)
                arcTo(13.0f, 13.0f, 0.0f, false, false, 16.0f, 3.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF101820)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(20.24f, 21.66f)
                lineToRelative(-4.95f, -4.95f)
                arcTo(1.0f, 1.0f, 0.0f, false, true, 15.0f, 16.0f)
                verticalLineTo(8.0f)
                horizontalLineToRelative(2.0f)
                verticalLineToRelative(7.59f)
                lineToRelative(4.66f, 4.65f)
                close()
            }
        }
            .build()
        return clock!!
    }

private var clock: ImageVector? = null