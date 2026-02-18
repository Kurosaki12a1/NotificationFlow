package com.kuro.notiflow.presentation.common.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val color1: Color,
    val color2: Color,
    val color3: Color,
    val color4: Color
)

val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No AppColors provided")
}