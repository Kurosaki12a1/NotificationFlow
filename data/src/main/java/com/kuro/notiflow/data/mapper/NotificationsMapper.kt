package com.kuro.notiflow.data.mapper

import com.kuro.notiflow.data.entity.NotificationEntity
import com.kuro.notiflow.domain.models.notifications.NotificationModel

fun NotificationEntity.toDomain(): NotificationModel {
    return NotificationModel(
        id = id,
        packageName = packageName,
        title = title,
        text = text,
        subText = subText,
        bigText = bigText,
        postTime = postTime,
        smallIconResId = smallIconResId,
        iconBase64 = iconBase64,
        groupKey = groupKey,
        channelId = channelId,
        isRead = isRead
    )
}

fun NotificationModel.toEntity(): NotificationEntity {
    return NotificationEntity(
        packageName = packageName,
        title = title,
        text = text,
        subText = subText,
        bigText = bigText,
        postTime = postTime,
        smallIconResId = smallIconResId,
        iconBase64 = iconBase64,
        groupKey = groupKey,
        channelId = channelId,
        isRead = isRead
    )
}