package com.fpinbo.app.events

import com.fpinbo.app.entities.Event

sealed class EventsState

object Loading : EventsState()

data class Events(
    val events: List<Event>
) : EventsState()

data class Error(
    val message: String
) : EventsState()


