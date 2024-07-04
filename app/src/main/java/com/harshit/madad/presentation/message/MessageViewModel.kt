package com.harshit.madad.presentation.message

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshit.madad.common.Constants
import com.harshit.madad.common.Resource
import com.harshit.madad.domain.usecase.GetHelperGuardianListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val getHelperGuardianListUseCase: GetHelperGuardianListUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MessageState())
    val state: StateFlow<MessageState> = _state.asStateFlow()

    private val _message = mutableStateOf(Constants.DEFAULT_MESSAGE)
    val message: State<String> = _message

    private val _superGuardianSelected = mutableStateOf(true)
    val superGuardianSelected: State<Boolean> = _superGuardianSelected

    private val _locationMessage = mutableStateOf("")
    val locationMessage: State<String> = _locationMessage

    fun loadGuardians() {
        viewModelScope.launch {
            getHelperGuardianListUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val filteredGuardians = result.data?.filter { it.isSelected } ?: emptyList()
                        val superGuardian = filteredGuardians.find { it.isSuperGuardian }
                        _state.value = MessageState(
                            guardians = filteredGuardians.sortedBy { it.name },
                            isLoading = false,
                            error = "",
                            superGuardianNumber = superGuardian?.phoneNumber ?: "",
                            onHelpClick = true
                        )
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(error = result.message ?: "")
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun onSuperGuardianSelected(selected: Boolean) {
        _superGuardianSelected.value = selected
    }

    fun updateMessage(message: String) {
        _message.value = message
    }

    fun updateLocation(location: String){
        _locationMessage.value = location
    }
}