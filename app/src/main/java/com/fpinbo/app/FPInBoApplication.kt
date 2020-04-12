package com.fpinbo.app

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.fpinbo.app.inject.*
import javax.inject.Inject
import javax.inject.Provider

class FPInBoApplication : Application(), HasSubComponentBuilders {


    private lateinit var appComponent: AppComponent

    @Inject
    lateinit var appListeners: Set<@JvmSuppressWildcards LifecycleObserver>

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        appComponent.inject(this)
        setupAppObservers()
    }

    override fun subComponentBuilders(): Map<Class<*>, Provider<SubComponentBuilder<*>>> =
        appComponent.subComponentBuilders()

    private fun setupAppObservers() {
        val appLifecycle = ProcessLifecycleOwner.get().lifecycle
        appListeners.forEach {
            appLifecycle.addObserver(it)
        }
    }

}