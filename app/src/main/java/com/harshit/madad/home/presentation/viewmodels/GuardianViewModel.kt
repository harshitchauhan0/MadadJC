package com.harshit.madad.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshit.madad.common.Resource
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.domain.use_cases.GetGuardianListUseCase
import com.harshit.madad.home.domain.use_cases.SaveGuardianListUseCase
import com.harshit.madad.home.util.ContactState
import com.harshit.madad.home.util.GuardianDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuardianViewModel @Inject constructor(
    private val getGuardianListUseCase: GetGuardianListUseCase,
    private val saveGuardianListUseCase: SaveGuardianListUseCase
) : ViewModel() {

    private val _contactState = MutableStateFlow(ContactState())
    val contactState: StateFlow<ContactState> = _contactState.asStateFlow()

    private val _showGuardianDialog = MutableStateFlow(GuardianDialogState())
    val showGuardianDialog: StateFlow<GuardianDialogState> = _showGuardianDialog.asStateFlow()

    init {
        fetchGuardianList()
    }

    private fun fetchGuardianList() {
        viewModelScope.launch {
            getGuardianListUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _contactState.value = ContactState(
                            contactList = result.data ?: emptyList(), isLoading = false, error = ""
                        )
                    }

                    is Resource.Error -> {
                        _contactState.value = ContactState(error = result.message ?: "")
                    }

                    is Resource.Loading -> {
                        _contactState.value = _contactState.value.copy(isLoading = true)
                    }
                }
            }
        }
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
                    contactItem.copy(isSuperGuardian = true, isSelected = true)
                } else if (contactItem.isSuperGuardian) {
                    contactItem.copy(isSuperGuardian = false, isSelected = false)
                } else {
                    contactItem
                }
            }
            _contactState.value = _contactState.value.copy(contactList = updatedList)
        }
    }

    fun fetchContacts() {
        viewModelScope.launch {
            val contacts = MutableList(30) {
                ContactItem(
                    name = "Contact $it",
                    phoneNumber = "1234567890",
                    id = it
                )
            }
            val contactList: List<ContactItem> = contacts.map { contactItem ->
                (if (_contactState.value.contactList.any { it.id == contactItem.id }) {
                    _contactState.value.contactList.find { it.id == contactItem.id }
                } else {
                    contactItem
                })!!
            }
            saveGuardianList(contactList)
        }
    }

    fun saveGuardianList(list: List<ContactItem> = _contactState.value.contactList) {
        val selectedCount = list.count { it.isSelected }
        val superGuardianCount = list.count { it.isSuperGuardian }
        if (selectedCount > 5 || superGuardianCount > 1) {
            _contactState.value = _contactState.value.copy(
                error = "You can select up to 5 guardians including only one super guardian."
            )
            return
        }
        viewModelScope.launch {
            saveGuardianListUseCase.invoke(list).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _contactState.value = _contactState.value.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        _contactState.value = _contactState.value.copy(
                            isLoading = false, error = "", contactList = list
                        )
                    }

                    is Resource.Error -> {
                        _contactState.value = _contactState.value.copy(
                            isLoading = false, error = result.message ?: "Unknown error"
                        )
                    }
                }
            }
        }
    }
}