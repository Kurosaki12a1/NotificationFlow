package com.kuro.notiflow.presentation.bookmark.di

import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.bookmark.BookmarkFeatureNav
import com.kuro.notiflow.presentation.bookmark.ui.topbar.BookmarkTopBarProvider
import com.kuro.notiflow.presentation.bookmark.ui.topbar.BookmarkRulesTopBarProvider
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class BookmarkModule {

    @Binds
    @IntoSet
    abstract fun bindBookmarkNav(
        impl: BookmarkFeatureNav
    ): FeatureNav

    @Binds
    @IntoSet
    abstract fun bindBookmarkTopBar(
        impl: BookmarkTopBarProvider
    ): TopBarProvider

    @Binds
    @IntoSet
    abstract fun bindBookmarkRulesTopBar(
        impl: BookmarkRulesTopBarProvider
    ): TopBarProvider
}
