package com.kuro.notiflow.presentation.common.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.kuro.notiflow.domain.models.settings.ColorType
import com.kuro.notiflow.domain.models.settings.LanguageType
import com.kuro.notiflow.domain.models.settings.ThemeType
import com.kuro.notiflow.domain.models.settings.fetchAppLanguage
import com.kuro.notiflow.presentation.common.theme.materials.blueDarkColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.blueLightColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.blue_theme_dark_primary
import com.kuro.notiflow.presentation.common.theme.materials.blue_theme_light_primary
import com.kuro.notiflow.presentation.common.theme.materials.pinkDarkColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.pinkLightColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.pink_theme_dark_primary
import com.kuro.notiflow.presentation.common.theme.materials.pink_theme_light_primary
import com.kuro.notiflow.presentation.common.theme.materials.purpleDarkColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.purpleLightColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.purple_theme_dark_primary
import com.kuro.notiflow.presentation.common.theme.materials.purple_theme_light_primary
import com.kuro.notiflow.presentation.common.theme.materials.redDarkColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.redLightColorScheme
import com.kuro.notiflow.presentation.common.theme.materials.red_theme_dark_primary
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
    languageType: LanguageType = LanguageType.DEFAULT,
    themeType: ThemeType = ThemeType.DEFAULT,
    colorType: ColorType = ColorType.BLUE,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isSystemInDarkTheme()) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                context
            )
        }

        themeType == ThemeType.DEFAULT -> {
            if (isSystemInDarkTheme()) fetchDarkColorScheme(colorType) else fetchLightColorScheme(
                colorType
            )
        }

        themeType == ThemeType.LIGHT -> fetchLightColorScheme(colorType)
        else -> fetchDarkColorScheme(colorType)
    }

    val isDark = when {
        dynamicColor || themeType == ThemeType.DEFAULT -> isSystemInDarkTheme()
        themeType == ThemeType.LIGHT -> false
        themeType == ThemeType.DARK -> true
        else -> false
    }

    val appColors = AppColors(
        color1 = if (isDark) blue_theme_dark_primary else blue_theme_light_primary,
        color2 = if (isDark) pink_theme_dark_primary else pink_theme_light_primary,
        color3 = if (isDark) purple_theme_dark_primary else purple_theme_light_primary,
        color4 = if (isDark) red_theme_dark_primary else red_theme_light_primary
    )

    fetchAppLanguage(languageType.code)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
    ) {
        CompositionLocalProvider(
            LocalAppColors provides appColors
        ) {
            content()
        }
    }
}



