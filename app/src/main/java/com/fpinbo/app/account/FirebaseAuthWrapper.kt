package com.fpinbo.app.account

import android.app.Application
import android.content.Intent
import arrow.core.left
import arrow.core.right
import arrow.fx.IO
import com.firebase.ui.auth.AuthUI
import com.fpinbo.app.R
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthWrapper @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authUI: AuthUI,
    private val application: Application
) : Authenticator {

    override fun currentUser(): User? = firebaseAuth.currentUser?.let {
        User(it.displayName, it.email, it.photoUrl)
    }

    override fun logout(): IO<Unit> = IO.async { callback ->

        authUI.signOut(application).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                callback(Unit.right())
            } else {
                callback(task.exception!!.left())
            }
        }
    }

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