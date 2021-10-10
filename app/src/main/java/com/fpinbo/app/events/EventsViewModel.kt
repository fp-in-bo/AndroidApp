package com.fpinbo.app.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.fpinbo.app.analytics.Tracker
import com.fpinbo.app.entities.Event
import com.fpinbo.app.network.Api
import com.fpinbo.app.network.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
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
        viewModelScope.launch {
            _state.postValue(retrieveData().fold(
                ifLeft = { Error(it.message.orEmpty()) },
                ifRight = { Events(it.events) }
            ))
        }
    }

    private suspend fun retrieveData(): Either<Throwable, Events> = api.events()
        .map { listOfNetworkEvent ->
            listOfNetworkEvent.map {
                it.toEntity()
            }
        }.map {
            Events(it)
        }

    fun trackSelectItem(event: Event) = tracker.selectItem(event.id.toString(), event.title)
}
