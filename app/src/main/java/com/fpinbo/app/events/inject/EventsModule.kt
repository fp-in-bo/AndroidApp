package com.fpinbo.app.events.inject

import androidx.lifecycle.ViewModel
import com.fpinbo.app.events.EventsViewModel
import com.fpinbo.app.inject.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class EventsModule {

    @Binds
    @IntoMap
    @ViewModelKey(EventsViewModel::class)
    internal abstract fun bindEventsViewModel(viewModel: EventsViewModel): ViewModel
}