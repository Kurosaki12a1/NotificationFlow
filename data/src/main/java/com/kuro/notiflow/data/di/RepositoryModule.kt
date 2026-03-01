package com.kuro.notiflow.data.di

import com.kuro.notiflow.data.data_source.bookmark.BookmarkRuleLocalDataSource
import com.kuro.notiflow.data.data_source.notification.NotificationLocalDataSource
import com.kuro.notiflow.data.data_source.settings.SettingsLocalDataSource
import com.kuro.notiflow.data.data_source.datastore.AppDataStoreDataSource
import com.kuro.notiflow.data.data_source.datastore.AppDataStoreDataSourceImpl
import com.kuro.notiflow.data.framework.app.AndroidAppInfoProvider
import com.kuro.notiflow.data.framework.exporter.AndroidExportFileWriter
import com.kuro.notiflow.data.framework.exporter.ExportFileWriter
import com.kuro.notiflow.data.framework.exporter.NotificationCsvExporter
import com.kuro.notiflow.data.framework.importer.AndroidImportFileReader
import com.kuro.notiflow.data.framework.importer.ImportFileReader
import com.kuro.notiflow.data.framework.importer.NotificationCsvImporter
import com.kuro.notiflow.data.framework.importer.NotificationExcelImporter
import com.kuro.notiflow.data.framework.importer.NotificationImporter
import com.kuro.notiflow.data.impl.BookmarkRuleRepositoryImpl
import com.kuro.notiflow.data.impl.AndroidAppLauncher
import com.kuro.notiflow.data.impl.AppDataRepositoryImpl
import com.kuro.notiflow.data.impl.NotificationExportRepositoryImpl
import com.kuro.notiflow.data.impl.NotificationImportRepositoryImpl
import com.kuro.notiflow.data.impl.NotificationRepositoryImpl
import com.kuro.notiflow.data.impl.SettingsMenuRepositoryImpl
import com.kuro.notiflow.data.impl.SystemTimeProvider
import com.kuro.notiflow.domain.api.app.AppLauncher
import com.kuro.notiflow.domain.api.app.AppInfoProvider
import com.kuro.notiflow.domain.api.bookmark.BookmarkRuleRepository
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
import javax.inject.Named
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
        dataSource: NotificationLocalDataSource,
        bookmarkRuleDataSource: BookmarkRuleLocalDataSource
    ): NotificationRepository = NotificationRepositoryImpl(dataSource, bookmarkRuleDataSource)

    @Provides
    @Singleton
    fun provideBookmarkRuleRepository(
        ruleDataSource: BookmarkRuleLocalDataSource,
        notificationDataSource: NotificationLocalDataSource,
        appInfoProvider: AppInfoProvider
    ): BookmarkRuleRepository =
        BookmarkRuleRepositoryImpl(ruleDataSource, notificationDataSource, appInfoProvider)

    @Provides
    @Singleton
    fun provideAppInfoProvider(
        impl: AndroidAppInfoProvider
    ): AppInfoProvider = impl

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
    @Named("csv")
    fun provideNotificationCsvImporter(): NotificationImporter = NotificationCsvImporter()

    @Provides
    @Singleton
    @Named("excel")
    fun provideNotificationExcelImporter(): NotificationImporter = NotificationExcelImporter()

    @Provides
    @Singleton
    fun provideNotificationImportRepository(
        @Named("csv") csvImporter: NotificationImporter,
        @Named("excel") excelImporter: NotificationImporter,
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
