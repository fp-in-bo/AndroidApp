package com.fpinbo.app.analytics

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject

class LifeCycleTracker @Inject constructor(private val tracker: Tracker) :
    DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        tracker.appOpen()
    }
}