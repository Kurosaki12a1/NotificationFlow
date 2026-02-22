package com.kuro.notiflow.domain.api.importer

import com.kuro.notiflow.domain.models.notifications.NotificationModel

interface NotificationImportRepository {
    suspend fun importNotifications(uriString: String): Result<List<NotificationModel>>
}
