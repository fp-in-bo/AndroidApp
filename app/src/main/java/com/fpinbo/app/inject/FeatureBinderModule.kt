package com.fpinbo.app.inject

import com.fpinbo.app.account.inject.AccountSubComponent
import com.fpinbo.app.events.inject.EventsSubComponent
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [EventsSubComponent::class, AccountSubComponent::class])
abstract class FeatureBinderModule {

    @Binds
    @IntoMap
    @SubComponentKey(EventsSubComponent.Builder::class)
    abstract fun events(impl: EventsSubComponent.Builder): SubComponentBuilder<*>

    @Binds
    @IntoMap
    @SubComponentKey(AccountSubComponent.Builder::class)
    abstract fun account(impl: AccountSubComponent.Builder): SubComponentBuilder<*>
}