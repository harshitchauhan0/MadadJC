package com.harshit.madad.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshit.madad.common.Resource
import com.harshit.madad.domain.model.ContactItem
import com.harshit.madad.domain.usecase.EmailUseCase
import com.harshit.madad.domain.usecase.GetGuardianListUseCase
import com.harshit.madad.domain.usecase.GetUserNameUseCase
import com.harshit.madad.domain.usecase.RemoveGuardianUseCase
import com.harshit.madad.domain.usecase.SaveNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val saveNameUseCase: SaveNameUseCase,
    private val getGuardianListUseCase: GetGuardianListUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val removeGuardianUseCase: RemoveGuardianUseCase,
    private val getEmailsUseCase: EmailUseCase,
) : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _nameState = MutableStateFlow(EditTextState())
    val nameState: StateFlow<EditTextState> = _nameState.asStateFlow()

    private val _emailState = MutableStateFlow(EditTextState())
    val emailState: StateFlow<EditTextState> = _emailState.asStateFlow()

    init {
        fetchUserName()
        fetchEmail()
        fetchGuardianList()
    }

    private fun fetchEmail() {
        viewModelScope.launch {
            getEmailsUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _emailState.value = EditTextState(text = result.data ?: "")
                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {
                        _profileState.value = _profileState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    private fun fetchUserName() {
        viewModelScope.launch {
            getUserNameUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _nameState.value = EditTextState(text = result.data ?: "", isEnabled = false)
                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {
                        _profileState.value = _profileState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun fetchGuardianList() {
        viewModelScope.launch {
            getGuardianListUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val filteredGuardians = result.data?.filter { it.isSelected } ?: emptyList()
                        _profileState.value = ProfileState(
                            guardians = filteredGuardians.sortedBy { it.name },
                            isLoading = false,
                            error = ""
                        )
                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {
                        _profileState.value = _profileState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun nameValueChange(newName: String) {
        _nameState.value = _nameState.value.copy(text = newName, isEnabled = true)
    }

    fun toggleEditName() {
        _nameState.value = _nameState.value.copy(isEditing = !_nameState.value.isEditing)
    }

    fun updateName() {
        viewModelScope.launch {
            val newName = _nameState.value.text
            saveNameUseCase.invoke(newName).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _nameState.value = _nameState.value.copy(isEditing = false, isEnabled = false)
                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {
                        _profileState.value = _profileState.value.copy(isLoading = true)
                    }
                }
            }.launchIn(this)
        }
    }

    fun removeGuardian(guardian: ContactItem) {
        viewModelScope.launch {
            removeGuardianUseCase.invoke(guardian).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        val updatedGuardians = _profileState.value.guardians - guardian
                        _profileState.value = _profileState.value.copy(guardians = updatedGuardians.sortedBy { it.name })
                    }

                    is Resource.Error -> {
                        // Handle error
                    }

                    is Resource.Loading -> {
                        _profileState.value = _profileState.value.copy(isLoading = true)
                    }
                }
            }.launchIn(this)
        }
    }

}