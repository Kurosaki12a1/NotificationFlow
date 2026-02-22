package com.kuro.notiflow.presentation.onboarding.ui

import com.kuro.notiflow.domain.use_case.OnboardingUseCase
import com.kuro.notiflow.presentation.onboarding.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class OnboardingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `completeOnboarding delegates to use case`() = runTest {
        val useCase = mockk<OnboardingUseCase>()
        coEvery { useCase.completeOnboarding() } returns Unit

        val viewModel = OnboardingViewModel(useCase)

        viewModel.completeOnboarding()

        coVerify(exactly = 1) { useCase.completeOnboarding() }
    }
}
