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

val ErrorImage: ImageVector
    get() {
        if (_errorImage != null) {
            return _errorImage!!
        }
        _errorImage = Builder(
            name = "PlaceHolder", defaultWidth = 32.0.dp, defaultHeight =
                32.0.dp, viewportWidth = 32.0f, viewportHeight = 32.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(30.0f, 3.414f)
                lineTo(28.586f, 2.0f)
                lineTo(2.0f, 28.586f)
                lineTo(3.414f, 30.0f)
                lineToRelative(2.0f, -2.0f)
                lineTo(26.0f, 28.0f)
                arcToRelative(2.003f, 2.003f, 0.0f, false, false, 2.0f, -2.0f)
                lineTo(28.0f, 5.414f)
                close()
                moveTo(26.0f, 26.0f)
                lineTo(7.414f, 26.0f)
                lineToRelative(7.793f, -7.793f)
                lineToRelative(2.379f, 2.379f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, false, 2.828f, 0.0f)
                lineTo(22.0f, 19.0f)
                lineToRelative(4.0f, 3.997f)
                close()
                moveTo(26.0f, 20.168f)
                lineTo(23.414f, 17.582f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, false, -2.828f, 0.0f)
                lineTo(19.0f, 19.168f)
                lineToRelative(-2.377f, -2.377f)
                lineTo(26.0f, 7.414f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(6.0f, 22.0f)
                verticalLineTo(19.0f)
                lineToRelative(5.0f, -4.997f)
                lineToRelative(1.373f, 1.373f)
                lineToRelative(1.416f, -1.416f)
                lineToRelative(-1.375f, -1.375f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, false, -2.828f, 0.0f)
                lineTo(6.0f, 16.172f)
                verticalLineTo(6.0f)
                horizontalLineTo(22.0f)
                verticalLineTo(4.0f)
                horizontalLineTo(6.0f)
                arcTo(2.002f, 2.002f, 0.0f, false, false, 4.0f, 6.0f)
                verticalLineTo(22.0f)
                close()
            }
        }
            .build()
        return _errorImage!!
    }

private var _errorImage: ImageVector? = null
