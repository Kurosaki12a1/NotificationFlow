package com.kuro.notiflow.presentation.onboarding.di

import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.onboarding.OnboardingFeatureNav
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class OnboardingModule {
    @Binds
    @IntoSet
    abstract fun bindOnboardingNav(
        impl: OnboardingFeatureNav
    ): FeatureNav
}
