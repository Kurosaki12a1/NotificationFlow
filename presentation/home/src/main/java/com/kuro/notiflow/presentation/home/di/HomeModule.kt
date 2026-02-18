package com.kuro.notiflow.presentation.home.di

import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.home.HomeFeatureNav
import com.kuro.notiflow.presentation.home.ui.topbar.HomeTopBarProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @IntoSet
    abstract fun bindHomeNav(
        impl: HomeFeatureNav
    ): FeatureNav

    @Binds
    @IntoSet
    abstract fun bindHomeTopBar(
        impl: HomeTopBarProvider
    ): TopBarProvider
}