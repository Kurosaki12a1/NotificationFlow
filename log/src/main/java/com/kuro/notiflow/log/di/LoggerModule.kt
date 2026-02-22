package com.kuro.notiflow.log.di

import com.kuro.notiflow.log.AndroidLogger
import com.kuro.notiflow.domain.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {
    @Provides
    @Singleton
    fun provideLogger(impl: AndroidLogger): Logger = impl
}
