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

val Restart: ImageVector
    get() {
        if (_restart != null) {
            return _restart!!
        }
        _restart = Builder(
            name =
                "_4213447ArrowLoadLoadingRefreshReloadIcon", defaultWidth = 24.0.dp, defaultHeight
            = 24.0.dp, viewportWidth = 128.0f, viewportHeight = 128.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(96.1f, 103.6f)
                curveToRelative(-10.4f, 8.4f, -23.5f, 12.4f, -36.8f, 11.1f)
                curveToRelative(-10.5f, -1.0f, -20.3f, -5.1f, -28.2f, -11.8f)
                horizontalLineTo(44.0f)
                verticalLineToRelative(-8.0f)
                horizontalLineTo(18.0f)
                verticalLineToRelative(26.0f)
                horizontalLineToRelative(8.0f)
                verticalLineToRelative(-11.9f)
                curveToRelative(9.1f, 7.7f, 20.4f, 12.5f, 32.6f, 13.6f)
                curveToRelative(1.9f, 0.2f, 3.7f, 0.3f, 5.5f, 0.3f)
                curveToRelative(13.5f, 0.0f, 26.5f, -4.6f, 37.0f, -13.2f)
                curveToRelative(19.1f, -15.4f, 26.6f, -40.5f, 19.1f, -63.9f)
                lineToRelative(-7.6f, 2.4f)
                curveTo(119.0f, 68.6f, 112.6f, 90.3f, 96.1f, 103.6f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(103.0f, 19.7f)
                curveToRelative(-21.2f, -18.7f, -53.5f, -20.0f, -76.1f, -1.6f)
                curveTo(7.9f, 33.5f, 0.4f, 58.4f, 7.7f, 81.7f)
                lineToRelative(7.6f, -2.4f)
                curveTo(9.0f, 59.2f, 15.5f, 37.6f, 31.9f, 24.4f)
                curveTo(51.6f, 8.4f, 79.7f, 9.6f, 98.0f, 26.0f)
                horizontalLineTo(85.0f)
                verticalLineToRelative(8.0f)
                horizontalLineToRelative(26.0f)
                verticalLineTo(8.0f)
                horizontalLineToRelative(-8.0f)
                verticalLineTo(19.7f)
                close()
            }
        }
            .build()
        return _restart!!
    }

private var _restart: ImageVector? = null