package com.fpinbo.app

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FPInBoApplication : Application() {

    @Inject
    lateinit var appListeners: Set<@JvmSuppressWildcards LifecycleObserver>

    override fun onCreate() {
        super.onCreate()
        setupAppObservers()
    }

    private fun setupAppObservers() {
        val appLifecycle = ProcessLifecycleOwner.get().lifecycle
        appListeners.forEach {
            appLifecycle.addObserver(it)
        }
    }

}
