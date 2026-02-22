package com.kuro.notiflow.presentation.common.usecase

import com.kuro.notiflow.domain.api.datastore.AppDataRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class OnboardingUseCaseTest {

    @Test
    fun `exposes isFirstLaunch from repository`() = runBlocking {
        val repository = mockk<AppDataRepository>()
        every { repository.isFirstLaunch } returns flowOf(true)
        val useCase = OnboardingUseCase(repository)

        val value = useCase.isFirstLaunch.first()

        assertEquals(true, value)
    }

    @Test
    fun `completes onboarding through repository`() = runBlocking {
        val repository = mockk<AppDataRepository>(relaxed = true)
        val useCase = OnboardingUseCase(repository)

        useCase.completeOnboarding()

        coVerify(exactly = 1) { repository.setOnboardingCompleted() }
    }
}
