package com.harshit.madad.common.splash


sealed class SplashState {
    object Loading : SplashState()
    object NavigateToWelcome : SplashState()
    object NavigateToHome : SplashState()
    object Error : SplashState()
}