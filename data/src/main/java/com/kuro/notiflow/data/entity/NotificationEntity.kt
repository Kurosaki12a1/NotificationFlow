package com.kuro.notiflow.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_table")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val packageName: String,
    val title: String?,
    val text: String?,
    val subText: String?,
    val bigText: String?,
    val summaryText: String?,
    val infoText: String?,
    val textLines: List<String>?,
    val postTime: Long,
    val priority: Int,
    val category: String,
    val smallIconResId: Int?,
    val iconBase64: String?,
    val groupKey: String?,
    val channelId: String?,
    val isRead: Boolean,
    val isBookmarked: Boolean
)