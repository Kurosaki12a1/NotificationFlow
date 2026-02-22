package com.kuro.notiflow.presentation.common.di

import com.kuro.notiflow.presentation.common.ui.dialog.DialogController
import com.kuro.notiflow.presentation.common.ui.dialog.DialogControllerImpl
import com.kuro.notiflow.presentation.common.usecase.OnboardingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {
    @Provides
    @Singleton
    fun provideDialogController(): DialogController = DialogControllerImpl()

    @Provides
    @Singleton
    fun provideOnboardingUseCase(
        repository: com.kuro.notiflow.domain.api.datastore.AppDataRepository
    ): OnboardingUseCase = OnboardingUseCase(repository)
}
