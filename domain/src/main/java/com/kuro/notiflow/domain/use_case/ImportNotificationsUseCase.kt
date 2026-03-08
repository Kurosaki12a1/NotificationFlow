package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.importer.NotificationImportRepository
import com.kuro.notiflow.domain.api.datastore.AppDataRepository
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.notifications.NotificationFilterMode
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class ImportNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val importRepository: NotificationImportRepository,
    private val appDataRepository: AppDataRepository
) {
    suspend operator fun invoke(
        uriString: String,
        skipBlockedPackages: Boolean = false
    ): Result<Int> {
        val importedResult = importRepository.importNotifications(uriString)
        val imported = importedResult.getOrElse { return Result.failure(it) }
        val filtered = if (skipBlockedPackages) {
            val settings = appDataRepository.notificationFilterSettings.first()
            when (settings.mode) {
                NotificationFilterMode.BLOCK_LIST ->
                    imported.filterNot { it.packageName in settings.packageNames }
                else -> imported
            }
        } else {
            imported
        }
        if (filtered.isEmpty()) {
            return Result.success(0)
        }
        notificationRepository.addNotifications(filtered)
        return Result.success(filtered.size)
    }
}
