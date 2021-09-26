package com.fpinbo.app.account

import android.content.Intent
import arrow.core.Either

interface Authenticator {

    fun currentUser(): User?

    suspend fun logout(): Either<Throwable,Unit>

    fun signInIntent(): Intent
}
