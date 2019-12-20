package com.fpinbo.app.events

sealed class EventsState

object Loading : EventsState()

data class Events(
    val events: List<Event>
) : EventsState()

data class Error(
    val message: String
) : EventsState()


data class Event(
    val title: String,
    val speaker: String,
    val imageUrl: String,
    val description: String
)