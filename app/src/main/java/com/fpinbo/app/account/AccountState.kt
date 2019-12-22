package com.fpinbo.app.account

sealed class AccountState

data class Logged(
    val user: User
) : AccountState()

object NotLogged : AccountState()

object Loading : AccountState()

data class Error(
    val message: String?
) : AccountState()