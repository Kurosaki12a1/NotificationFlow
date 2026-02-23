package com.kuro.notiflow.data.di

import com.kuro.notiflow.data.data_source.notification.NotificationLocalDataSource
import com.kuro.notiflow.data.data_source.settings.SettingsLocalDataSource
import com.kuro.notiflow.data.data_source.data_store.AppDataStoreDataSource
import com.kuro.notiflow.data.data_source.data_store.AppDataStoreDataSourceImpl
import com.kuro.notiflow.data.export.AndroidExportFileWriter
import com.kuro.notiflow.data.export.ExportFileWriter
import com.kuro.notiflow.data.export.NotificationCsvExporter
import com.kuro.notiflow.data.importer.AndroidImportFileReader
import com.kuro.notiflow.data.importer.ImportFileReader
import com.kuro.notiflow.data.importer.NotificationCsvImporter
import com.kuro.notiflow.data.importer.NotificationExcelImporter
import com.kuro.notiflow.data.impl.AndroidAppLauncher
import com.kuro.notiflow.data.impl.AppDataRepositoryImpl
import com.kuro.notiflow.data.impl.NotificationExportRepositoryImpl
import com.kuro.notiflow.data.impl.NotificationImportRepositoryImpl
import com.kuro.notiflow.data.impl.NotificationRepositoryImpl
import com.kuro.notiflow.data.impl.SettingsMenuRepositoryImpl
import com.kuro.notiflow.data.impl.SystemTimeProvider
import com.kuro.notiflow.domain.api.app.AppLauncher
import com.kuro.notiflow.domain.api.datastore.AppDataRepository
import com.kuro.notiflow.domain.api.export.NotificationExportRepository
import com.kuro.notiflow.domain.api.importer.NotificationImportRepository
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import com.kuro.notiflow.domain.utils.TimeProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideSettingsMenuRepository(
        dataSource: SettingsLocalDataSource
    ): SettingsMenuRepository = SettingsMenuRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideNotificationRepository(
        dataSource: NotificationLocalDataSource
    ): NotificationRepository = NotificationRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideDataRepository(
        dataSource: AppDataStoreDataSource
    ): AppDataRepository = AppDataRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideAppDataStoreDataSource(
        impl: AppDataStoreDataSourceImpl
    ): AppDataStoreDataSource = impl


    @Provides
    @Singleton
    fun provideNotificationExportRepository(
        exporter: NotificationCsvExporter,
        fileWriter: ExportFileWriter
    ): NotificationExportRepository = NotificationExportRepositoryImpl(exporter, fileWriter)

    @Provides
    @Singleton
    fun provideExportFileWriter(
        androidWriter: AndroidExportFileWriter
    ): ExportFileWriter = androidWriter

    @Provides
    @Singleton
    fun provideImportFileReader(
        androidReader: AndroidImportFileReader
    ): ImportFileReader = androidReader

    @Provides
    @Singleton
    fun provideNotificationImportRepository(
        csvImporter: NotificationCsvImporter,
        excelImporter: NotificationExcelImporter,
        fileReader: ImportFileReader
    ): NotificationImportRepository =
        NotificationImportRepositoryImpl(csvImporter, excelImporter, fileReader)

    @Provides
    @Singleton
    fun provideSystemTimeProvider(): TimeProvider = SystemTimeProvider()

    @Provides
    @Singleton
    fun provideAppLauncher(
        impl: AndroidAppLauncher
    ): AppLauncher = impl
}
