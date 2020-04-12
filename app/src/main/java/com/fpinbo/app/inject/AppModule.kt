package com.fpinbo.app.inject

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.fpinbo.app.utils.LifeCycleLogger
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideContext() = application

    @Provides
    @IntoSet
    fun provideLifeCycleLogger(impl: LifeCycleLogger): LifecycleObserver = impl

}