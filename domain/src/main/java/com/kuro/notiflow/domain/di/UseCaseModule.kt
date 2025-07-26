package com.kuro.notiflow.domain.di

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import com.kuro.notiflow.domain.use_case.FetchNotificationsUseCase
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
}
