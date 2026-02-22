package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.datastore.AppDataRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class OnboardingUseCaseTest {

    private lateinit var repository: AppDataRepository
    private lateinit var useCase: OnboardingUseCase

    @Before
    fun setup() {
        repository = mockk()
        every { repository.isFirstLaunch } returns flowOf(true)
        useCase = OnboardingUseCase(repository)
    }

    @Test
    fun `exposes isFirstLaunch from repository`() = runTest {
        val value = useCase.isFirstLaunch

        val result = value.first()
        assertEquals(true, result)
    }

    @Test
    fun `completeOnboarding delegates to repository`() = runTest {
        coEvery { repository.setOnboardingCompleted() } returns Unit

        useCase.completeOnboarding()

        coVerify(exactly = 1) { repository.setOnboardingCompleted() }
    }
}
