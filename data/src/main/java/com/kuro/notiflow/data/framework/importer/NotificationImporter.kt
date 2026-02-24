package com.kuro.notiflow.data.framework.importer

import com.kuro.notiflow.domain.models.notifications.NotificationModel
import java.io.InputStream

interface NotificationImporter {
    fun import(inputStream: InputStream): List<NotificationModel>
}