package com.kuro.notiflow.data.utils

import com.kuro.notiflow.data.entity.NotificationEntity
import com.kuro.notiflow.domain.models.notifications.NotificationModel

object NotificationFactory {
    fun model(
        id: Long = 0L,
        packageName: String = "pkg",
        text: String? = "text",
        postTime: Long = 1_000L
    ) = NotificationModel(
        id = id,
        packageName = packageName,
        title = "title",
        text = text,
        subText = null,
        bigText = null,
        summaryText = null,
        infoText = null,
        textLines = null,
        postTime = postTime,
        priority = 0,
        category = "cat",
        smallIconResId = null,
        iconBase64 = null,
        groupKey = null,
        channelId = null,
        isRead = false,
        isBookmarked = false
    )

    fun entity(
        id: Long = 0L,
        packageName: String = "pkg",
        text: String? = "text",
        postTime: Long = 1_000L
    ) = NotificationEntity(
        id = id,
        packageName = packageName,
        title = "title",
        text = text,
        subText = null,
        bigText = null,
        summaryText = null,
        infoText = null,
        textLines = null,
        postTime = postTime,
        priority = 0,
        category = "cat",
        smallIconResId = null,
        iconBase64 = null,
        groupKey = null,
        channelId = null,
        isRead = false,
        isBookmarked = false
    )
}
