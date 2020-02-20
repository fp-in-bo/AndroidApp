package com.fpinbo.app.event.inject

import com.fpinbo.app.event.view.EventFragment
import com.fpinbo.app.inject.FragmentScope
import com.fpinbo.app.inject.SubComponentBuilder
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [EventModule::class])
interface EventSubComponent {

    fun inject(eventFragment: EventFragment)

    @Subcomponent.Builder
    interface Builder : SubComponentBuilder<EventSubComponent> {
        fun eventModule(eventModule: EventModule): Builder
    }
}