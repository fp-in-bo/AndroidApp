package com.fpinbo.app.analytics

interface Tracker {

    fun appOpen()

    fun login(method: String)

    fun signUp(method: String)

    fun selectItem(id: String, name: String)

    fun share(id: String)
}