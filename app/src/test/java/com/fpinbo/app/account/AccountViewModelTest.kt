package com.fpinbo.app.account

import android.app.Activity
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.fx.IO
import arrow.fx.typeclasses.milliseconds
import com.firebase.ui.auth.FirebaseUiException
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.util.ExtraConstants
import com.fpinbo.app.analytics.Tracker
import com.fpinbo.app.utils.ViewEvent
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Rule
import org.junit.Test

class AccountViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val tracker: Tracker = mock()

    @Test
    fun logged() {
        val user = User("Name", "mail", "avatar")

        val authenticator: Authenticator = mock {
            on { currentUser() } doReturn user
        }
        val sut = buildSut(authenticator)

        sut.state.test()
            .awaitValue()
            .assertValue(Logged(user))
    }

    @Test
    fun notLogged() {
        val authenticator: Authenticator = mock {
            on { currentUser() } doReturn null
        }
        val sut = buildSut(authenticator)

        sut.state.test()
            .awaitValue()
            .assertValue(NotLogged)
    }

    @Test
    fun login() {
        val signInIntent: Intent = mock()
        val authenticator: Authenticator = mock {
            on { signInIntent() } doReturn signInIntent
        }

        val sut = buildSut(authenticator)

        sut.logIn()

        sut.viewEvent.test()
            .awaitValue()
            .assertValue(ViewEvent(PerformLogin(signInIntent)))
    }

    @Test
    fun logout() {
        val authenticator: Authenticator = mock {
            on { logout() } doReturn IO.sleep(10.milliseconds)
        }
        val sut = buildSut(authenticator)

        sut.logOut()

        sut.state.test()
            .awaitNextValue()
            .assertValueHistory(Loading, NotLogged)
    }

    @Test
    fun onLoginSuccess() {
        val user = User("Name", "mail", "avatar")
        val authenticator: Authenticator = mock {
            on { currentUser() } doReturn user
        }

        val sut = buildSut(authenticator)

        val response: IdpResponse = mock()
        val data: Intent = mock {
            on { getParcelableExtra<IdpResponse>(ExtraConstants.IDP_RESPONSE) } doReturn response
        }
        sut.onLoginResult(Activity.RESULT_OK, data)

        sut.state.test()
            .awaitValue()
            .assertValue(Logged(user))
    }

    @Test
    fun onLoginError() {
        val error = FirebaseUiException(1, "errorMessage")
        val response: IdpResponse = mock {
            on { getError() } doReturn error
        }
        val data: Intent = mock {
            on { getParcelableExtra<IdpResponse>(ExtraConstants.IDP_RESPONSE) } doReturn response
        }

        val sut = buildSut(mock())

        sut.onLoginResult(Activity.RESULT_CANCELED, data)

        sut.state.test()
            .awaitValue()
            .assertValue(Error("errorMessage"))
    }

    @Test
    fun trackLogin() {
        val response: IdpResponse = mock {
            on { isNewUser } doReturn false
            on { providerType } doReturn "provider"
        }
        val data: Intent = mock {
            on { getParcelableExtra<IdpResponse>(ExtraConstants.IDP_RESPONSE) } doReturn response
        }

        val user = User("Name", "mail", "avatar")
        val authenticator: Authenticator = mock {
            on { currentUser() } doReturn user
        }

        val sut = buildSut(authenticator)

        sut.onLoginResult(Activity.RESULT_OK, data)

        verify(tracker).login("provider")
    }

    @Test
    fun trackSignUp() {
        val response: IdpResponse = mock {
            on { isNewUser } doReturn true
            on { providerType } doReturn "provider"
        }
        val data: Intent = mock {
            on { getParcelableExtra<IdpResponse>(ExtraConstants.IDP_RESPONSE) } doReturn response
        }

        val user = User("Name", "mail", "avatar")
        val authenticator: Authenticator = mock {
            on { currentUser() } doReturn user
        }

        val sut = buildSut(authenticator)

        sut.onLoginResult(Activity.RESULT_OK, data)

        verify(tracker).signUp("provider")
    }

    private fun buildSut(authenticator: Authenticator) =
        AccountViewModel(authenticator, tracker)
}