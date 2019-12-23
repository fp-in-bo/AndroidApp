package com.fpinbo.app.inject

import com.fpinbo.app.network.NetworkModule
import dagger.Component
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelBuilder::class,
        FeatureBinderModule::class,
        NetworkModule::class
    ]
)
interface AppComponent {

    fun subComponentBuilders(): Map<Class<*>, Provider<SubComponentBuilder<*>>>

}