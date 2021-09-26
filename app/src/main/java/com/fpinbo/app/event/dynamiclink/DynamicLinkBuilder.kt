package com.fpinbo.app.event.dynamiclink

import arrow.core.Either
import com.fpinbo.app.entities.Event


interface DynamicLinkBuilder {

    suspend fun build(event: Event): Either<Throwable, String>

}

