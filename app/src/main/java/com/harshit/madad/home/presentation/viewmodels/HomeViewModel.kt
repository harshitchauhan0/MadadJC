package com.harshit.madad.home.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.harshit.madad.home.util.CallState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor():ViewModel() {
    fun onLogout(){

    }

    private val _callState = MutableStateFlow(CallState())
    val callState:StateFlow<CallState> = _callState

    fun onCallClick(){
        viewModelScope.launch {
//          TODO -> HERE I HAVE TO CHANGE THE LOADING STATE AND DO THE NUMBER SAVING AND GETTING SAVED DATA
            _callState.value = CallState(isLoading = true)
            delay(5000)
            _callState.value = CallState(isCalling = true, number = "1234567890")
        }
    }
}