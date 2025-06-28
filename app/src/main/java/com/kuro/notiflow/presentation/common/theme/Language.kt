package com.kuro.notiflow.presentation.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kuro.notiflow.R

enum class LanguageType(val code: String?) {
    DEFAULT(null),
    EN("en"),
    RU("ru"),
    DE("de"),
    ES("es"),
    FA("fa"),
    FR("fr"),
    PT_BR("pt"),
    TR("tr"),
    VN("vi"),
    PL("pl");

    @Composable
    fun toLanguageName(): String {
        return when (this) {
            DEFAULT -> stringResource(R.string.defaultLanguageTitle)
            EN -> stringResource(R.string.engLanguageTitle)
            RU -> stringResource(R.string.rusLanguageTitle)
            DE -> stringResource(R.string.gerLanguageTitle)
            ES -> stringResource(R.string.spaLanguageTitle)
            FA -> stringResource(R.string.perLanguageTitle)
            FR -> stringResource(R.string.freLanguageTitle)
            PT_BR -> stringResource(R.string.brazilLanguageTitle)
            TR -> stringResource(R.string.turLanguageTitle)
            VN -> stringResource(R.string.vieLanguageTitle)
            PL -> stringResource(R.string.polLanguageTitle)
        }
    }
}