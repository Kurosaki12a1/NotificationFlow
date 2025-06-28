package com.kuro.notiflow.presentation.common.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.kuro.notiflow.presentation.common.theme.materials.blueDarkColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.blueLightColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.blueSeed
import com.kuro.notiflow.presentation.common.theme.materials.blue_theme_light_primary
import com.kuro.notiflow.presentation.common.theme.materials.pinkDarkColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.pinkLightColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.pinkSeed
import com.kuro.notiflow.presentation.common.theme.materials.pink_theme_light_primary
import com.kuro.notiflow.presentation.common.theme.materials.purpleDarkColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.purpleLightColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.purpleSeed
import com.kuro.notiflow.presentation.common.theme.materials.purple_theme_light_primary
import com.kuro.notiflow.presentation.common.theme.materials.redDarkColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.redLightColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.redSeed
import com.kuro.notiflow.presentation.common.theme.materials.red_theme_light_primary

private val AppTypography = Typography(
    titleLarge = Typography().titleLarge.copy(letterSpacing = 0.15.sp),
    bodyLarge = Typography().bodyLarge.copy(letterSpacing = 0.25.sp),
    labelMedium = Typography().labelMedium.copy(letterSpacing = 0.5.sp),
)

@Composable
fun fetchLightColorScheme(
    colorType: ColorType
) = when (colorType) {
    ColorType.RED -> redLightColorScheme
    ColorType.PINK -> pinkLightColorScheme
    ColorType.PURPLE -> purpleLightColorScheme
    ColorType.BLUE -> blueLightColorScheme
}

@Composable
fun fetchDarkColorScheme(
    colorType: ColorType
) = when (colorType) {
    ColorType.RED -> redDarkColorScheme
    ColorType.PINK -> pinkDarkColorScheme
    ColorType.PURPLE -> purpleDarkColorScheme
    ColorType.BLUE -> blueDarkColorScheme
}


@Composable
fun NotificationFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,     // Dynamic color is available on Android 12+
    colorType: ColorType = ColorType.RED,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> fetchDarkColorScheme(colorType)
        else -> fetchLightColorScheme(colorType)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

enum class ColorType {
    RED, PINK, PURPLE, BLUE;

    fun seed(): Color {
        return when (this) {
            RED -> redSeed
            PINK -> pinkSeed
            PURPLE -> purpleSeed
            BLUE -> blueSeed
        }
    }

    fun onSeed(): Color {
        return when (this) {
            RED -> red_theme_light_primary
            PINK -> pink_theme_light_primary
            PURPLE -> purple_theme_light_primary
            BLUE -> blue_theme_light_primary
        }
    }
}

enum class ThemeType {
    DEFAULT, LIGHT, DARK
}