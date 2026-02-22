package com.kuro.notiflow.domain.models.settings

import java.util.Locale

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
}

fun fetchAppLanguage(code: String?): LanguageType {
    val currentLanguage: LanguageType =
        LanguageType.entries.find { it.code == code } ?: LanguageType.EN
    val locale: Locale = currentLanguage.code
        ?.let { Locale.forLanguageTag(it) }
        ?: Locale.ENGLISH
    Locale.setDefault(locale)
    return currentLanguage
}
