package com.fpinbo.app.event.dynamiclink

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DynamicLinkModule {

    @Binds
    abstract fun bindDynamicLinkBuilder(impl: FirebaseDynamicLinkBuilder): DynamicLinkBuilder
}
