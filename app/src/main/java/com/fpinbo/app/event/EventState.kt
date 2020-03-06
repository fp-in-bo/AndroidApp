package com.fpinbo.app.event

import com.fpinbo.app.entities.Event

sealed class EventState

data class Data(
    val event: Event
) : EventState()

data class Error(
    val message: String
) : EventState()


