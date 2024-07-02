package com.harshit.madad.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.util.ContactState
import com.harshit.madad.home.util.GuardianDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuardianViewModel @Inject constructor() : ViewModel(){

    private val _contactState = MutableStateFlow(ContactState())
    val contactState: StateFlow<ContactState> = _contactState.asStateFlow()

    private val _showGuardianDialog = MutableStateFlow(GuardianDialogState())
    val showGuardianDialog: StateFlow<GuardianDialogState> = _showGuardianDialog.asStateFlow()

    init {
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

    fun saveSuperGuardian(contact: ContactItem?) {
        if (contact != null) {
            val updatedList = _contactState.value.contactList.map { contactItem ->
                if (contact == contactItem) {
                    contactItem.copy(isSuperGuardian = true)
                } else if (contactItem.isSuperGuardian) {
                    contactItem.copy(isSuperGuardian = false)
                } else {
                    contactItem
                }
            }
            _contactState.value = _contactState.value.copy(contactList = updatedList)
        }
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