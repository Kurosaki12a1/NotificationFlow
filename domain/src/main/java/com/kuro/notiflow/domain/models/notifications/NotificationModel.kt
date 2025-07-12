package com.kuro.notiflow.domain.models.notifications

data class NotificationModel(
    val id: Long = 0,
    val packageName: String,
    val title: String?,
    val text: String?,
    val subText: String?,
    val bigText: String?,
    val postTime: Long,
    val smallIconResId: Int?,
    val iconBase64: String?,
    val groupKey: String?,
    val channelId: String?,
    val isRead: Boolean
)