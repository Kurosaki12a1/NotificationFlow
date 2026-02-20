package com.kuro.notiflow.presentation.common.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.kuro.notiflow.domain.models.settings.ColorType
import com.kuro.notiflow.domain.models.settings.ColorType.BLUE
import com.kuro.notiflow.domain.models.settings.ColorType.PINK
import com.kuro.notiflow.domain.models.settings.ColorType.PURPLE
import com.kuro.notiflow.domain.models.settings.ColorType.RED
import com.kuro.notiflow.domain.models.settings.LanguageType
import com.kuro.notiflow.domain.models.settings.LanguageType.DE
import com.kuro.notiflow.domain.models.settings.LanguageType.DEFAULT
import com.kuro.notiflow.domain.models.settings.LanguageType.EN
import com.kuro.notiflow.domain.models.settings.LanguageType.ES
import com.kuro.notiflow.domain.models.settings.LanguageType.FA
import com.kuro.notiflow.domain.models.settings.LanguageType.FR
import com.kuro.notiflow.domain.models.settings.LanguageType.PL
import com.kuro.notiflow.domain.models.settings.LanguageType.PT_BR
import com.kuro.notiflow.domain.models.settings.LanguageType.RU
import com.kuro.notiflow.domain.models.settings.LanguageType.TR
import com.kuro.notiflow.domain.models.settings.LanguageType.VN
import com.kuro.notiflow.presentation.common.theme.materials.blueSeed
import com.kuro.notiflow.presentation.common.theme.materials.blue_theme_light_primary
import com.kuro.notiflow.presentation.common.theme.materials.pinkSeed
import com.kuro.notiflow.presentation.common.theme.materials.pink_theme_light_primary
import com.kuro.notiflow.presentation.common.theme.materials.purpleSeed
import com.kuro.notiflow.presentation.common.theme.materials.purple_theme_light_primary
import com.kuro.notiflow.presentation.common.theme.materials.redSeed
import com.kuro.notiflow.presentation.common.theme.materials.red_theme_light_primary
import com.kuro.notiflow.presentation.common.R as CommonR

@Composable
fun LanguageType.toLanguageName(): String {
    return when (this) {
        DEFAULT -> stringResource(CommonR.string.defaultLanguageTitle)
        EN -> stringResource(CommonR.string.engLanguageTitle)
        RU -> stringResource(CommonR.string.rusLanguageTitle)
        DE -> stringResource(CommonR.string.gerLanguageTitle)
        ES -> stringResource(CommonR.string.spaLanguageTitle)
        FA -> stringResource(CommonR.string.perLanguageTitle)
        FR -> stringResource(CommonR.string.freLanguageTitle)
        PT_BR -> stringResource(CommonR.string.brazilLanguageTitle)
        TR -> stringResource(CommonR.string.turLanguageTitle)
        VN -> stringResource(CommonR.string.vieLanguageTitle)
        PL -> stringResource(CommonR.string.polLanguageTitle)
    }
}

fun ColorType.seed(): Color {
    return when (this) {
        RED -> redSeed
        PINK -> pinkSeed
        PURPLE -> purpleSeed
        BLUE -> blueSeed
    }
}

fun ColorType.onSeed(): Color {
    return when (this) {
        RED -> red_theme_light_primary
        PINK -> pink_theme_light_primary
        PURPLE -> purple_theme_light_primary
        BLUE -> blue_theme_light_primary
    }
}
