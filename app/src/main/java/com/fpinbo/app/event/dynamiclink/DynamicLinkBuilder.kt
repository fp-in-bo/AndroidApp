package com.fpinbo.app.event.dynamiclink

import arrow.fx.IO
import com.fpinbo.app.entities.Event


interface DynamicLinkBuilder {

    fun build(event: Event): IO<String>

}

