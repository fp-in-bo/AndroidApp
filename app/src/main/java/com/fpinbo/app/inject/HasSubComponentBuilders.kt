package com.fpinbo.app.inject

import javax.inject.Provider

interface HasSubComponentBuilders {
    fun subComponentBuilders(): Map<Class<*>, Provider<SubComponentBuilder<*>>>
}