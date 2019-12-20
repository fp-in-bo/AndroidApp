package com.fpinbo.app.inject

import dagger.Component
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelBuilder::class,
        FeatureBinderModule::class
    ]
)
interface AppComponent {

    fun subComponentBuilders(): Map<Class<*>, Provider<SubComponentBuilder<*>>>

}