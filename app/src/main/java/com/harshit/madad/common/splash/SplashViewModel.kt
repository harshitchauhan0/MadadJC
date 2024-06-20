package com.harshit.madad.common.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshit.madad.authentication.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository // Your repository for network calls
) : ViewModel() {

    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val splashState: StateFlow<SplashState> = _splashState.asStateFlow()

    fun isLoggedIn() {
        viewModelScope.launch {
            try {
                val isUserLoggedIn = authRepository.checkUserExist()
                _splashState.value = if (isUserLoggedIn) {
                    SplashState.NavigateToHome
                } else {
                    SplashState.NavigateToWelcome
                }
            } catch (e: Exception) {
                _splashState.value = SplashState.Error
            }
        }
    }
}
