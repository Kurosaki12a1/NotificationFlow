package com.kuro.notiflow.presentation.common.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.kuro.notiflow.presentation.common.theme.materials.blueDarkColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.blueLightColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.pinkDarkColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.pinkLightColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.purpleDarkColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.purpleLightColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.redDarkColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.redLightColorScheme

private val AppTypography = Typography(
    titleLarge = Typography().titleLarge.copy(letterSpacing = 0.15.sp),
    bodyLarge = Typography().bodyLarge.copy(letterSpacing = 0.25.sp),
    labelMedium = Typography().labelMedium.copy(letterSpacing = 0.5.sp),
)

@Composable
fun fetchLightColorScheme(
    themeType: ThemeType
) = when (themeType) {
    ThemeType.RED -> redLightColorScheme
    ThemeType.PINK -> pinkLightColorScheme
    ThemeType.PURPLE -> purpleLightColorScheme
    ThemeType.BLUE -> blueLightColorScheme
}

@Composable
fun fetchDarkColorScheme(
    themeType: ThemeType
) = when (themeType) {
    ThemeType.RED -> redDarkColorScheme
    ThemeType.PINK -> pinkDarkColorScheme
    ThemeType.PURPLE -> purpleDarkColorScheme
    ThemeType.BLUE -> blueDarkColorScheme
}


@Composable
fun NotificationFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,     // Dynamic color is available on Android 12+
    themeType: ThemeType = ThemeType.RED,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> fetchDarkColorScheme(themeType)
        else -> fetchLightColorScheme(themeType)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

enum class ThemeType {
    RED, PINK, PURPLE, BLUE
}