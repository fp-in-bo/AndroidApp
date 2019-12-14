package com.fpinbo.app.inject

import com.fpinbo.app.events.EventsFragment
import com.fpinbo.app.events.inject.EventsModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelBuilder::class,
        EventsModule::class
    ]
)
interface AppComponent {

    fun inject(eventsFragment: EventsFragment)
}