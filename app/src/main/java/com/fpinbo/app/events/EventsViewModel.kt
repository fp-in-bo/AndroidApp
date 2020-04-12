package com.fpinbo.app.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.fx.IO
import arrow.integrations.kotlinx.unsafeRunScoped
import com.fpinbo.app.analytics.Tracker
import com.fpinbo.app.entities.Event
import com.fpinbo.app.network.Api
import com.fpinbo.app.network.toEntity
import javax.inject.Inject

class EventsViewModel @Inject constructor(
    private val api: Api,
    private val tracker: Tracker
) : ViewModel() {

    private val _state = MutableLiveData<EventsState>()

    val state: LiveData<EventsState>
        get() = _state

    init {
        loadData()
    }

    private fun loadData() {
        _state.value = Loading

        retrieveData()
            .unsafeRunScoped(viewModelScope) { result ->
                val viewState = result.fold(
                    ifLeft = { Error(it.message.orEmpty()) },
                    ifRight = { Events(it.events) }
                )
                _state.postValue(viewState)
            }
    }

    private fun retrieveData(): IO<Events> = api.events()
        .map { listOfNetworkEvent ->
            listOfNetworkEvent.map {
                it.toEntity()
            }
        }.map {
            Events(it)
        }

    fun trackSelectItem(event: Event) = tracker.selectItem(event.id.toString(), event.title)
}