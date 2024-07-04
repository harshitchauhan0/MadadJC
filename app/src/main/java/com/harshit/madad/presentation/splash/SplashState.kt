package com.harshit.madad.presentation.splash


sealed class SplashState {
    object Loading : SplashState()
    object NavigateToWelcome : SplashState()
    object NavigateToHome : SplashState()
    object Error : SplashState()
}