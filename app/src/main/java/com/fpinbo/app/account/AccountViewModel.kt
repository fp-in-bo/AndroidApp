package com.fpinbo.app.account

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.unsafeRunAsyncInViewModel
import com.firebase.ui.auth.IdpResponse
import com.fpinbo.app.utils.Event
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private val authenticator: Authenticator
) : ViewModel() {

    private val _state = MutableLiveData<AccountState>()
    private val _event = MutableLiveData<Event<AccountEvent>>()

    val state: LiveData<AccountState>
        get() = _state

    val event: LiveData<Event<AccountEvent>>
        get() = _event

    init {
        loadData()
    }

    private fun loadData() {
        val user = authenticator.currentUser()
        val state = if (user != null) {
            Logged(user)
        } else {
            NotLogged
        }
        _state.value = state
    }

    fun logIn() {
        _event.value = Event(PerformLogin(authenticator.signInIntent()))
    }

    fun logOut() {
        _state.value = Loading
        authenticator.logout().unsafeRunAsyncInViewModel(this) { result ->
            val state = result.fold(
                ifLeft = {
                    Error(it.message.orEmpty())
                },
                ifRight = { NotLogged }
            )
            _state.value = state
        }
    }

    fun onLoginResult(resultCode: Int, data: Intent?) {

        val response = IdpResponse.fromResultIntent(data)

        val state = if (resultCode == Activity.RESULT_OK) {
            Logged(authenticator.currentUser()!!)
        } else {
            Error(response?.error?.message)
        }
        _state.value = state
    }

}
