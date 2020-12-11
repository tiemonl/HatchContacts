package dev.tiemon.hatchcontacts.model

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.tiemon.hatchcontacts.adapter.Contact
import kotlinx.coroutines.launch

class ContactViewModel @ViewModelInject constructor(
    private val contactProvider: ContactProvider
) : ViewModel() {
    private val mutableContacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> = mutableContacts

    fun getContacts() = viewModelScope.launch {
        mutableContacts.postValue(contactProvider.getContactsInfo())
    }
}