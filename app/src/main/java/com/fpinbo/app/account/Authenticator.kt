package com.fpinbo.app.account

import android.content.Intent
import arrow.fx.IO

interface Authenticator {

    fun currentUser(): User?

    fun logout(): IO<Unit>

    fun signInIntent(): Intent
}