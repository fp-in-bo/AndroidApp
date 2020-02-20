package com.fpinbo.app.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.unsafeRunAsyncInViewModel
import arrow.fx.IO
import com.fpinbo.app.entities.Event
import com.fpinbo.app.network.Api
import com.fpinbo.app.network.toIO
import javax.inject.Inject

class EventsViewModel @Inject constructor(
    private val api: Api
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
            .unsafeRunAsyncInViewModel(this) { result ->
                val viewState = result.fold(
                    ifLeft = { Error(it.message.orEmpty()) },
                    ifRight = { Events(it.events) }
                )
                _state.postValue(viewState)
            }
    }

    private fun retrieveData(): IO<Events> = api.events().toIO()
        .map { listOfNetworkEvent ->
            listOfNetworkEvent.map {
                Event(
                    it.title,
                    it.speaker,
                    it.imageUrl,
                    it.description
                )
            }
        }.map {
            Events(it)
        }
}