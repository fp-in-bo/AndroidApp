package com.fpinbo.app.analytics

import android.app.Application
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event.*
import com.google.firebase.analytics.FirebaseAnalytics.Param.*
import javax.inject.Inject

class FirebaseTracker @Inject constructor(application: Application) : Tracker {

    private val firebaseInstance = FirebaseAnalytics.getInstance(application)
    override fun appOpen() = firebaseInstance.logEvent(APP_OPEN, null)

    override fun login(method: String) = firebaseInstance.logEvent(LOGIN, Bundle(1).apply {
        putString(METHOD, method)
    })

    override fun signUp(method: String) = firebaseInstance.logEvent(SIGN_UP, Bundle(1).apply {
        putString(METHOD, method)
    })

    override fun selectItem(id: String, name: String) =
        firebaseInstance.logEvent(SELECT_ITEM, Bundle(2).apply {
            putString(ITEM_LIST_ID, id)
            putString(ITEM_LIST_NAME, name)
        })

    override fun share(id: String) = firebaseInstance.logEvent(SHARE, Bundle(1).apply {
        putString(ITEM_ID, id)
    })
}