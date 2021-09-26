package com.fpinbo.app.account

import CoroutineRule
import android.app.Activity
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.right
import com.firebase.ui.auth.FirebaseUiException
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.util.ExtraConstants
import com.fpinbo.app.analytics.Tracker
import com.fpinbo.app.utils.ViewEvent
import com.jraska.livedata.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AccountViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val tracker: Tracker = mockk(relaxed = true)

    @Test
    fun logged() {
        val user = User("Name", "mail", "avatar")

        val authenticator: Authenticator = mockk {
            every { currentUser() } returns user
        }
        val sut = buildSut(authenticator)

        sut.state.test()
            .awaitValue()
            .assertValue(Logged(user))
    }

    @Test
    fun notLogged() {
        val authenticator: Authenticator = mockk {
            every { currentUser() } returns null
        }
        val sut = buildSut(authenticator)

        sut.state.test()
            .awaitValue()
            .assertValue(NotLogged)
    }

    @Test
    fun login() {
        val signInIntent: Intent = mockk(relaxed = true)
        val authenticator: Authenticator = mockk(relaxed = true) {
            every { signInIntent() } returns signInIntent
        }

        val sut = buildSut(authenticator)

        sut.logIn()

        sut.viewEvent.test()
            .awaitValue()
            .assertValue(ViewEvent(PerformLogin(signInIntent)))
    }

    @Test
    fun logout()  {
        val authenticator: Authenticator = mockk(relaxed = true) {
            coEvery { logout() } coAnswers {
                Unit.right()
            }
        }
        val sut = buildSut(authenticator)

        sut.logOut()

        sut.state.test()
            .assertValue(NotLogged)
    }

    @Test
    fun onLoginSuccess() {
        val user = User("Name", "mail", "avatar")
        val authenticator: Authenticator = mockk {
            every { currentUser() } returns user
        }

        val sut = buildSut(authenticator)

        val response: IdpResponse = mockk(relaxed = true)
        val data: Intent = mockk {
            every { getParcelableExtra<IdpResponse>(ExtraConstants.IDP_RESPONSE) } returns response
        }
        sut.onLoginResult(Activity.RESULT_OK, data)

        sut.state.test()
            .awaitValue()
            .assertValue(Logged(user))
    }

    @Test
    fun onLoginError() {
        val error = FirebaseUiException(1, "errorMessage")
        val response: IdpResponse = mockk(relaxed = true) {
            every { getError() } returns error
        }
        val data: Intent = mockk {
            every { getParcelableExtra<IdpResponse>(ExtraConstants.IDP_RESPONSE) } returns response
        }

        val sut = buildSut(mockk(relaxed = true))

        sut.onLoginResult(Activity.RESULT_CANCELED, data)

        sut.state.test()
            .awaitValue()
            .assertValue(Error("errorMessage"))
    }

    @Test
    fun trackLogin() {
        val response: IdpResponse = mockk {
            every { isNewUser } returns false
            every { providerType } returns "provider"
        }
        val data: Intent = mockk {
            every { getParcelableExtra<IdpResponse>(ExtraConstants.IDP_RESPONSE) } returns response
        }

        val user = User("Name", "mail", "avatar")
        val authenticator: Authenticator = mockk {
            every { currentUser() } returns user
        }

        val sut = buildSut(authenticator)

        sut.onLoginResult(Activity.RESULT_OK, data)

        verify { tracker.login("provider") }
    }

    @Test
    fun trackSignUp() {
        val response: IdpResponse = mockk {
            every { isNewUser } returns true
            every { providerType } returns "provider"
        }
        val data: Intent = mockk {
            every { getParcelableExtra<IdpResponse>(ExtraConstants.IDP_RESPONSE) } returns response
        }

        val user = User("Name", "mail", "avatar")
        val authenticator: Authenticator = mockk {
            every { currentUser() } returns user
        }

        val sut = buildSut(authenticator)

        sut.onLoginResult(Activity.RESULT_OK, data)

        verify { tracker.signUp("provider") }
    }

    private fun buildSut(authenticator: Authenticator) =
        AccountViewModel(authenticator, tracker)
}
