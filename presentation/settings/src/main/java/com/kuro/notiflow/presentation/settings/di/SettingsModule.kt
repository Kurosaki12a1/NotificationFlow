package com.kuro.notiflow.presentation.settings.di

import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.settings.SettingsFeatureNav
import com.kuro.notiflow.presentation.settings.ui.topbar.SettingsTopBarProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    @IntoSet
    abstract fun bindHomeNav(
        impl: SettingsFeatureNav
    ): FeatureNav

    @Binds
    @IntoSet
    abstract fun bindSettingsTopBar(
        impl: SettingsTopBarProvider
    ): TopBarProvider
}