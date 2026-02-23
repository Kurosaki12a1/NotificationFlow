package com.kuro.notiflow.presentation.notifications.di

import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.notifications.NotificationsFeatureNav
import com.kuro.notiflow.presentation.notifications.ui.details.topbar.DetailsTopBarProvider
import com.kuro.notiflow.presentation.notifications.ui.filter.topbar.FilterTopBarProvider
import com.kuro.notiflow.presentation.notifications.ui.main.topbar.NotificationsTopBarProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationsModule {

    @Binds
    @IntoSet
    abstract fun bindHomeNav(
        impl: NotificationsFeatureNav
    ): FeatureNav

    @Binds
    @IntoSet
    abstract fun bindNotificationsTopBar(
        impl: NotificationsTopBarProvider
    ): TopBarProvider

    @Binds
    @IntoSet
    abstract fun bindFilterTopBar(
        impl: FilterTopBarProvider
    ): TopBarProvider

    @Binds
    @IntoSet
    abstract fun bindNotificationsDetailTopBar(
        impl: DetailsTopBarProvider
    ): TopBarProvider
}
