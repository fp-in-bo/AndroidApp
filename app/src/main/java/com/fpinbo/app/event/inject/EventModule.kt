package com.fpinbo.app.event.inject

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.fpinbo.app.event.EventViewModel
import com.fpinbo.app.event.dynamiclink.DynamicLinkBuilder
import com.fpinbo.app.event.dynamiclink.FirebaseDynamicLinkBuilder
import com.fpinbo.app.event.view.EventFragmentArgs
import com.fpinbo.app.inject.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class EventModule(
    private val intent: Intent,
    private val args: EventFragmentArgs
) {

    @Provides
    fun provideIntent(): Intent = intent

    @Provides
    fun provideArgs(): EventFragmentArgs = args

    @Provides
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    internal fun bindEventsViewModel(impl: EventViewModel): ViewModel = impl

    @Provides
    fun provideDynamicLinkBuilder(impl: FirebaseDynamicLinkBuilder): DynamicLinkBuilder = impl
}