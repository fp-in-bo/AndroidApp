package com.fpinbo.app.inject

import androidx.lifecycle.LifecycleObserver
import com.fpinbo.app.utils.LifeCycleLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @IntoSet
    fun provideLifeCycleLogger(impl: LifeCycleLogger): LifecycleObserver = impl

}
