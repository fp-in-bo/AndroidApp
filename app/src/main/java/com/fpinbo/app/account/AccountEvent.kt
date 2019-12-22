package com.fpinbo.app.account

import android.content.Intent

sealed class AccountEvent

data class PerformLogin(
    val intent: Intent
) : AccountEvent()