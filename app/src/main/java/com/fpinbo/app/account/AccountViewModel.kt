package com.fpinbo.app.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class AccountViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableLiveData<AccountState>()

    val state: LiveData<AccountState>
        get() = _state

    init {
        loadData()
    }

    private fun loadData() {
    }

}
