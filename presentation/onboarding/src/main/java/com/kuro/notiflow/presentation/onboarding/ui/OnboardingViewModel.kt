package com.kuro.notiflow.presentation.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.presentation.common.usecase.OnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingUseCase: OnboardingUseCase
) : ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch(Dispatchers.IO) {
            onboardingUseCase.completeOnboarding()
        }
    }
}
