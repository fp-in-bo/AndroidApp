package com.fpinbo.app.inject

import com.fpinbo.app.events.inject.EventsSubComponent
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [EventsSubComponent::class])
abstract class FeatureBinderModule {
    @Binds
    @IntoMap
    @SubComponentKey(EventsSubComponent.Builder::class)
    abstract fun events(impl: EventsSubComponent.Builder): SubComponentBuilder<*>
}