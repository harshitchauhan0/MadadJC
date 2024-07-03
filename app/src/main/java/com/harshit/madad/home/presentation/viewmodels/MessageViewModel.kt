package com.harshit.madad.home.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshit.madad.common.Constants
import com.harshit.madad.common.Resource
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.domain.use_cases.GetGuardianListUseCase
import com.harshit.madad.home.domain.use_cases.GetHelperGuardianListUseCase
import com.harshit.madad.home.util.MessageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
}