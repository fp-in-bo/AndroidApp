package com.fpinbo.app.events.inject

import com.fpinbo.app.events.view.EventsFragment
import com.fpinbo.app.inject.FragmentScope
import com.fpinbo.app.inject.SubComponentBuilder
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [EventsModule::class])
interface EventsSubComponent {
    fun inject(eventsFragment: EventsFragment)


    @Subcomponent.Builder
    interface Builder : SubComponentBuilder<EventsSubComponent> {
        fun eventsModule(eventsModule: EventsModule) : Builder
    }
}