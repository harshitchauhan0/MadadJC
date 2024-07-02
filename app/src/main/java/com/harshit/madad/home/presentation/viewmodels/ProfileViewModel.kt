package com.harshit.madad.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.data.remote.dto.Guardian
import com.harshit.madad.home.util.ContactState
import com.harshit.madad.home.util.EditTextState
import com.harshit.madad.home.util.GuardianDialogState
import com.harshit.madad.home.util.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _nameState = MutableStateFlow(EditTextState())
    val nameState: StateFlow<EditTextState> = _nameState.asStateFlow()

    private val _emailState = MutableStateFlow(EditTextState())
    val emailState: StateFlow<EditTextState> = _emailState.asStateFlow()

    private val _contactState = MutableStateFlow(ContactState())
    val contactState: StateFlow<ContactState> = _contactState.asStateFlow()

    private val _showGuardianDialog = MutableStateFlow(GuardianDialogState())
    val showGuardianDialog: StateFlow<GuardianDialogState> = _showGuardianDialog.asStateFlow()

    init {
        loadProfileData()
        loadContacts()
    }

    private fun loadContacts() {
        _contactState.value = ContactState(
            contactList = List(20) { index ->
                ContactItem(
                    name = "Item $index",
                    isSelected = (index % 4 == 0),
                    phoneNumber = "9999999999",
                    isSuperGuardian = (index % 11 == 0)
                )
            }
        )
    }

    private fun loadProfileData() {
        _nameState.value = EditTextState(
            text = "Harshit"
        )
        _emailState.value = EditTextState(
            text = "imailharshit@gmail.com"
        )
        _profileState.value = ProfileState(
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
        val updatedGuardians = _profileState.value.guardians - guardian
        _profileState.value = _profileState.value.copy(guardians = updatedGuardians)
    }

    fun toggleSelection(index: Int) {
        val updatedList = _contactState.value.contactList.mapIndexed { i, item ->
            if (i == index) {
                item.copy(isSelected = !item.isSelected)
            } else {
                item
            }
        }
        _contactState.value = _contactState.value.copy(contactList = updatedList)
    }

    fun showGuardianDialog(show: Boolean, contactItem: ContactItem? = null) {
        _showGuardianDialog.value = GuardianDialogState(show, contactItem)
    }

    fun saveSuperGuardian(contactItem: ContactItem?) {

    }

    fun fetchContacts() {
        viewModelScope.launch {
            _contactState.value = _contactState.value.copy(isLoading = true)
            delay(5000)
            _contactState.value = ContactState(
                contactList = List(20) { index ->
                    ContactItem(
                        name = "Item $index",
                        isSelected = (index % 4 == 0),
                        phoneNumber = "9111999999"
                    )
                }
            )
        }
    }

}