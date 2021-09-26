package com.fpinbo.app.account

import android.app.Application
import android.content.Intent
import arrow.core.Either
import com.firebase.ui.auth.AuthUI
import com.fpinbo.app.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthWrapper @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authUI: AuthUI,
    private val application: Application
) : Authenticator {

    override fun currentUser(): User? = firebaseAuth.currentUser?.let {
        User(it.displayName, it.email, it.photoUrl.toString())
    }

    override suspend fun logout(): Either<Throwable, Unit> =
        Either.catch { authUI.signOut(application).await() }

    override fun signInIntent(): Intent = authUI
        .createSignInIntentBuilder()
        .setLogo(R.mipmap.ic_launcher)
        .setTheme(R.style.AppTheme)
        .setAvailableProviders(
            listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.TwitterBuilder().build()
            )
        )
        .build()
}
