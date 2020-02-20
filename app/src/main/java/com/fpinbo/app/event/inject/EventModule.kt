package com.fpinbo.app.event.inject

import androidx.lifecycle.ViewModel
import com.fpinbo.app.event.EventViewModel
import com.fpinbo.app.inject.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class EventModule {

    @Provides
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    internal fun bindEventsViewModel(impl: EventViewModel): ViewModel = impl
}