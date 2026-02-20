package com.kuro.notiflow.domain.utils

import com.kuro.notiflow.domain.models.notifications.NotificationModel

object NotificationFactory {
    fun model(
        id: Long = 1L,
        packageName: String = "pkg",
        title: String? = "title",
        text: String? = "text",
        postTime: Long = 1_000L,
        priority: Int = 0,
        category: String = "cat",
        isRead: Boolean = false,
        isBookmarked: Boolean = false
    ) = NotificationModel(
        id = id,
        packageName = packageName,
        title = title,
        text = text,
        subText = null,
        bigText = null,
        summaryText = null,
        infoText = null,
        textLines = null,
        postTime = postTime,
        priority = priority,
        category = category,
        smallIconResId = null,
        iconBase64 = null,
        groupKey = null,
        channelId = null,
        isRead = isRead,
        isBookmarked = isBookmarked
    )
}
