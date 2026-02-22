package com.kuro.notiflow.domain.di

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import com.kuro.notiflow.domain.api.export.NotificationExportRepository
import com.kuro.notiflow.domain.use_case.AutoClearNotificationsUseCase
import com.kuro.notiflow.domain.use_case.ClearAllNotificationsUseCase
import com.kuro.notiflow.domain.use_case.ExportNotificationsUseCase
import com.kuro.notiflow.domain.use_case.FetchNotificationsUseCase
import com.kuro.notiflow.domain.use_case.FetchTopNotificationsUseCase
import com.kuro.notiflow.domain.use_case.GetNotificationUseCase
import com.kuro.notiflow.domain.use_case.GetOverviewNotificationStatsUseCase
import com.kuro.notiflow.domain.use_case.LoadSettingsUseCase
import com.kuro.notiflow.domain.use_case.ResetSettingsUseCase
import com.kuro.notiflow.domain.use_case.UpdateSettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideLoadSettingsUseCase(repository: SettingsMenuRepository) =
        LoadSettingsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideUpdateSettingsUseCase(repository: SettingsMenuRepository) =
        UpdateSettingsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideResetSettingsUseCase(repository: SettingsMenuRepository) =
        ResetSettingsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideFetchNotificationsUseCase(repository: NotificationRepository) =
        FetchNotificationsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideGetNotificationUseCase(repository: NotificationRepository): GetNotificationUseCase =
        GetNotificationUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideFetchTopNotificationsUseCase(repository: NotificationRepository): FetchTopNotificationsUseCase =
        FetchTopNotificationsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideGetOverviewNotificationsStatsUseCase(repository: NotificationRepository): GetOverviewNotificationStatsUseCase =
        GetOverviewNotificationStatsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideClearAllNotificationsUseCase(repository: NotificationRepository): ClearAllNotificationsUseCase =
        ClearAllNotificationsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideExportNotificationsUseCase(
        notificationRepository: NotificationRepository,
        exportRepository: NotificationExportRepository
    ): ExportNotificationsUseCase =
        ExportNotificationsUseCase(notificationRepository, exportRepository)

    @Provides
    @ViewModelScoped
    fun provideAutoClearNotificationsUseCase(
        notificationRepository: NotificationRepository,
        settingsRepository: SettingsMenuRepository
    ): AutoClearNotificationsUseCase {
        return AutoClearNotificationsUseCase(notificationRepository, settingsRepository)
    }
}
