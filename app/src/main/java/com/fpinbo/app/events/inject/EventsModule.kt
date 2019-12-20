package com.fpinbo.app.events.inject

import androidx.lifecycle.ViewModel
import com.fpinbo.app.events.EventsViewModel
import com.fpinbo.app.inject.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class EventsModule {

    @Provides
    @IntoMap
    @ViewModelKey(EventsViewModel::class)
    internal fun bindEventsViewModel(impl: EventsViewModel): ViewModel = impl
}