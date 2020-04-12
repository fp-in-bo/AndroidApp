package com.fpinbo.app.analytics

import androidx.lifecycle.LifecycleObserver
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
abstract class AnalyticsModule {

    @Binds
    abstract fun bindFirebaseTracker(impl: FirebaseTracker): Tracker

    @Binds
    @IntoSet
    abstract fun bindLifeCycleTracker(impl: LifeCycleTracker): LifecycleObserver
}