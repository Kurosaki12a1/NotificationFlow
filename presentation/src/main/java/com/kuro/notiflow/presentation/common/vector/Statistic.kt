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

val Statistic: ImageVector
    get() {
        if (statistic != null) {
            return statistic!!
        }
        statistic = Builder(
            name = "Statistic", defaultWidth = 8.47.dp, defaultHeight = 8.47.dp,
            viewportWidth = 846.66f, viewportHeight = 846.66f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(31.33f, 513.64f)
                lineToRelative(118.34f, 0.0f)
                curveToRelative(11.39f, 0.0f, 20.63f, 9.24f, 20.63f, 20.63f)
                lineToRelative(0.0f, 214.49f)
                curveToRelative(0.0f, 11.39f, -9.24f, 20.63f, -20.63f, 20.63f)
                lineToRelative(-118.34f, 0.0f)
                curveToRelative(-11.39f, 0.0f, -20.63f, -9.24f, -20.63f, -20.63f)
                lineToRelative(0.0f, -214.49f)
                curveToRelative(0.0f, -11.39f, 9.24f, -20.63f, 20.63f, -20.63f)
                close()
                moveTo(696.99f, 77.27f)
                lineToRelative(118.34f, 0.0f)
                curveToRelative(11.39f, 0.0f, 20.63f, 9.24f, 20.63f, 20.63f)
                lineToRelative(0.0f, 650.86f)
                curveToRelative(0.0f, 11.39f, -9.24f, 20.63f, -20.63f, 20.63f)
                lineToRelative(-118.34f, 0.0f)
                curveToRelative(-11.39f, 0.0f, -20.63f, -9.24f, -20.63f, -20.63f)
                lineToRelative(0.0f, -650.86f)
                curveToRelative(0.0f, -11.39f, 9.24f, -20.63f, 20.63f, -20.63f)
                close()
                moveTo(794.7f, 118.53f)
                lineToRelative(-77.08f, 0.0f)
                lineToRelative(0.0f, 609.6f)
                lineToRelative(77.08f, 0.0f)
                lineToRelative(0.0f, -609.6f)
                close()
                moveTo(475.1f, 206.7f)
                lineToRelative(118.34f, 0.0f)
                curveToRelative(11.39f, 0.0f, 20.63f, 9.24f, 20.63f, 20.63f)
                lineToRelative(0.0f, 521.43f)
                curveToRelative(0.0f, 11.39f, -9.24f, 20.63f, -20.63f, 20.63f)
                lineToRelative(-118.34f, 0.0f)
                curveToRelative(-11.39f, 0.0f, -20.63f, -9.24f, -20.63f, -20.63f)
                lineToRelative(0.0f, -521.43f)
                curveToRelative(0.0f, -11.39f, 9.24f, -20.63f, 20.63f, -20.63f)
                close()
                moveTo(572.81f, 247.96f)
                lineToRelative(-77.08f, 0.0f)
                lineToRelative(0.0f, 480.17f)
                lineToRelative(77.08f, 0.0f)
                lineToRelative(0.0f, -480.17f)
                close()
                moveTo(253.22f, 362.02f)
                lineToRelative(118.34f, 0.0f)
                curveToRelative(11.39f, 0.0f, 20.63f, 9.24f, 20.63f, 20.63f)
                lineToRelative(0.0f, 366.11f)
                curveToRelative(0.0f, 11.39f, -9.24f, 20.63f, -20.63f, 20.63f)
                lineToRelative(-118.34f, 0.0f)
                curveToRelative(-11.4f, 0.0f, -20.63f, -9.24f, -20.63f, -20.63f)
                lineToRelative(0.0f, -366.11f)
                curveToRelative(0.0f, -11.39f, 9.23f, -20.63f, 20.63f, -20.63f)
                close()
                moveTo(350.93f, 403.28f)
                lineToRelative(-77.09f, 0.0f)
                lineToRelative(0.0f, 324.85f)
                lineToRelative(77.09f, 0.0f)
                lineToRelative(0.0f, -324.85f)
                close()
                moveTo(129.04f, 554.9f)
                lineToRelative(-77.08f, 0.0f)
                lineToRelative(0.0f, 173.23f)
                lineToRelative(77.08f, 0.0f)
                lineToRelative(0.0f, -173.23f)
                close()
            }
        }
            .build()
        return statistic!!
    }

private var statistic: ImageVector? = null
