package com.harshit.madad.home.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshit.madad.common.Resource
import com.harshit.madad.home.domain.use_cases.LogOutUseCase
import com.harshit.madad.home.domain.use_cases.UserInfoUseCase
import com.harshit.madad.home.util.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logoutUseCase: LogOutUseCase, private val userInfoUseCase: UserInfoUseCase
) : ViewModel() {
    private val _callState = MutableStateFlow(HomeScreenState())
    val callState: StateFlow<HomeScreenState> = _callState

    init {
        fetchUserInfo()
    }

     fun fetchUserInfo() {
        viewModelScope.launch {
            userInfoUseCase.invoke().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _callState.value = _callState.value.copy(
                            error = result.message ?: "Unknown Error"
                        )
                    }

                    is Resource.Loading -> {
                        _callState.value = _callState.value.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        _callState.value = _callState.value.copy(
                            name = result.data?.name ?: "",
                            number = result.data?.phone ?: "",
                            isLoading = false,
                            error = ""
                        )
                    }
                }
            }.launchIn(this)
        }
    }


    fun onLogout() {
        viewModelScope.launch {
            logoutUseCase.invoke().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _callState.value =
                            _callState.value.copy(error = result.message ?: "Unknown Error")
                    }

                    is Resource.Loading -> {
                        _callState.value = _callState.value.copy(isLoading = true)
                    }

                    is Resource.Success -> {

                    }
                }
            }.launchIn(this)
        }
    }
}