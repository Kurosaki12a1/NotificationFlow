package com.kuro.notiflow.domain.models.notifications

data class NotificationFilterSettings(
    val mode: NotificationFilterMode = NotificationFilterMode.ALLOW_ALL,
    val packageNames: Set<String> = emptySet()
)
