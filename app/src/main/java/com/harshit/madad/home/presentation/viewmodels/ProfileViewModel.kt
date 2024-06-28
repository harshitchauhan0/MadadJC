package com.harshit.madad.home.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.harshit.madad.home.data.remote.dto.Guardian
import com.harshit.madad.home.util.EditTextState
import com.harshit.madad.home.util.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _nameState = MutableStateFlow(EditTextState())
    val nameState: StateFlow<EditTextState> = _nameState.asStateFlow()

    private val _emailState = MutableStateFlow(EditTextState())
    val emailState: StateFlow<EditTextState> = _emailState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        _nameState.value = EditTextState(
            text = "Harshit"
        )
        _emailState.value = EditTextState(
            text = "imailharshit@gmail.com"
        )
        _state.value = ProfileState(
            guardians = List(20) {
                Guardian(
                    id = it,
                    name = "$it. Harshit",
                    phoneNumber = "tel: $it 4546545"
                )
            }
        )
    }

    fun nameValueChange(newName: String) {
        _nameState.value = _nameState.value.copy(text = newName)
    }

    fun emailValueChange(newEmail: String) {
        _emailState.value = _emailState.value.copy(text = newEmail)
    }

    fun toggleEditName() {
        _nameState.value = _nameState.value.copy(isEditing = !_nameState.value.isEditing)
    }

    fun toggleEditEmail() {
        _emailState.value = _emailState.value.copy(isEditing = !_nameState.value.isEditing)
    }

    fun updateNameAndEmail() {

    }

    fun removeGuardian(guardian: Guardian) {
        val updatedGuardians = _state.value.guardians - guardian
        _state.value = _state.value.copy(guardians = updatedGuardians)
    }
}