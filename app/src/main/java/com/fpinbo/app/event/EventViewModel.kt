package com.fpinbo.app.event

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.*
import arrow.fx.IO
import com.fpinbo.app.entities.Event
import com.fpinbo.app.event.view.EventFragmentArgs
import com.fpinbo.app.network.Api
import com.fpinbo.app.network.toEntity
import com.fpinbo.app.network.toIO
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventViewModel @Inject constructor(
    private val intent: Intent,
    private val args: EventFragmentArgs,
    private val api: Api
) : ViewModel() {

    private val _state = MutableLiveData<EventState>()

    val state: LiveData<EventState>
        get() = _state

    private val invalidInput = Error("Invalid Input")

    init {
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        val eventFromArgs = args.event
        val intentData = intent.data
        when {
            eventInExtras(eventFromArgs) -> {
                IO.just(Data(eventFromArgs!!))
            }
            idInDeeplink(intentData) -> {
                loadEventFromNetwork(intentData!!.lastPathSegment!!)
            }
            else -> IO.just(invalidInput)
        }
            .unsafeRunAsyncInViewModel(this@EventViewModel) { result ->
                val viewState = result.fold(
                    ifLeft = { Error(it.message.orEmpty()) },
                    ifRight = { it }
                )
                _state.postValue(viewState)
            }
    }

    private fun eventInExtras(eventFromArgs: Event?) = eventFromArgs != null

    private fun idInDeeplink(intentData: Uri?) =
        intentData != null && intentData.host == "event" && !intentData.lastPathSegment.isNullOrBlank()

    private fun loadEventFromNetwork(lastPathSegment: String): IO<EventState> {
        return api.events().toIO().map { listOfNetworkEvent ->
            val networkEvent =
                listOfNetworkEvent.firstOrNull { it.id.toString() == lastPathSegment }
            if (networkEvent != null) {
                Data(networkEvent.toEntity())
            } else {
                invalidInput
            }
        }
    }
}