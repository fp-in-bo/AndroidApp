package com.fpinbo.app.utils

import android.content.Context
import com.fpinbo.app.inject.HasSubComponentBuilders
import com.fpinbo.app.inject.SubComponentBuilder

inline fun <reified T : SubComponentBuilder<*>> Context.subComponentBuilder(): T =
    (applicationContext as HasSubComponentBuilders).subComponentBuilders()[T::class.java]?.get() as T