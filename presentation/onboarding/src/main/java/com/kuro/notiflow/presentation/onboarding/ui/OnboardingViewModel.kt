package com.kuro.notiflow.presentation.onboarding.ui

import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.presentation.common.usecase.OnboardingUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingUseCase: OnboardingUseCase
) : BaseViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch(Dispatchers.IO) {
            onboardingUseCase.completeOnboarding()
        }
    }
}
