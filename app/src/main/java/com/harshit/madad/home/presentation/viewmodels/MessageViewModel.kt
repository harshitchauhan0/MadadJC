package com.harshit.madad.home.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshit.madad.common.Constants
import com.harshit.madad.home.data.remote.dto.Guardian
import com.harshit.madad.home.util.MessageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(MessageState())
    val state: StateFlow<MessageState> = _state.asStateFlow()

    private val _message = mutableStateOf(Constants.DEFAULT_MESSAGE)
    val message: State<String> = _message

    private val _superGuardianSelected = mutableStateOf(true)
    val superGuardianSelected: State<Boolean> = _superGuardianSelected

    fun loadGuardians() {
        viewModelScope.launch {
            _state.value = MessageState(isLoading = true)
            delay(5000)
            _state.value = MessageState(
                guardians = List(20) {
                    Guardian(
                        id = it,
                        name = "$it. Harshit",
                        phoneNumber = "tel: $it 4546545"
                    )
                },
                superGuardianNumber = "99999999999",
                onHelpClick = true
            )
        }
    }

    fun onSuperGuardianSelected(selected: Boolean) {
        _superGuardianSelected.value = selected
    }

    fun updateMessage(message: String) {
        _message.value = message
    }
}