package com.harshit.madad.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshit.madad.common.Resource
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.domain.use_cases.EmailUseCase
import com.harshit.madad.home.domain.use_cases.GetGuardianListUseCase
import com.harshit.madad.home.domain.use_cases.GetUserNameUseCase
import com.harshit.madad.home.domain.use_cases.RemoveGuardianUseCase
import com.harshit.madad.home.domain.use_cases.SaveNameUseCase
import com.harshit.madad.home.util.ContactState
import com.harshit.madad.home.util.EditTextState
import com.harshit.madad.home.util.GuardianDialogState
import com.harshit.madad.home.util.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private val getEmailsUseCase: EmailUseCase
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _nameState = MutableStateFlow(EditTextState())
    val nameState: StateFlow<EditTextState> = _nameState.asStateFlow()

    private val _emailState = MutableStateFlow(EditTextState())
    val emailState: StateFlow<EditTextState> = _emailState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            getUserNameUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _nameState.value = EditTextState(text = result.data ?: "")
                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {
                        _profileState.value = _profileState.value.copy(isLoading = true)
                    }
                }
            }

            getGuardianListUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _profileState.value = ProfileState(guardians = result.data ?: emptyList())
                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {
                        _profileState.value = _profileState.value.copy(isLoading = true)
                    }
                }
            }

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

    fun nameValueChange(newName: String) {
        _nameState.value = _nameState.value.copy(text = newName)
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
                        _nameState.value = _nameState.value.copy(isEditing = false)
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
                        _profileState.value = _profileState.value.copy(guardians = updatedGuardians)
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