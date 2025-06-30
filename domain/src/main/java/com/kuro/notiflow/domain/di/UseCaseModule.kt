package com.kuro.notiflow.domain.di

import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import com.kuro.notiflow.domain.use_case.LoadSettingsUseCase
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
}
