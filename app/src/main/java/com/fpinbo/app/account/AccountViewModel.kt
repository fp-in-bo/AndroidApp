package com.fpinbo.app.account

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.integrations.kotlinx.unsafeRunScoped
import com.firebase.ui.auth.IdpResponse
import com.fpinbo.app.analytics.Tracker
import com.fpinbo.app.utils.ViewEvent
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private val authenticator: Authenticator,
    private val tracker: Tracker
) : ViewModel() {

    private val _state = MutableLiveData<AccountState>()
    private val _event = MutableLiveData<ViewEvent<AccountEvent>>()

    val state: LiveData<AccountState>
        get() = _state

    val viewEvent: LiveData<ViewEvent<AccountEvent>>
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
        _event.value = ViewEvent(PerformLogin(authenticator.signInIntent()))
    }

    fun logOut() {
        _state.value = Loading
        authenticator.logout().unsafeRunScoped(viewModelScope) { result ->
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

        val response: IdpResponse = IdpResponse.fromResultIntent(data)!!

        val state = if (resultCode == Activity.RESULT_OK) {
            Logged(authenticator.currentUser()!!)
        } else {
            Error(response.error?.message)
        }
        _state.value = state
        track(response)
    }

    private fun track(response: IdpResponse) {
        val provider = response.providerType.orEmpty()

        if (!response.isNewUser) {
            tracker.login(provider)
        } else {
            tracker.signUp(provider)
        }
    }

}
