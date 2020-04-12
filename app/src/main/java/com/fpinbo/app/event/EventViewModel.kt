package com.fpinbo.app.event

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.fx.IO
import arrow.integrations.kotlinx.unsafeRunScoped
import com.fpinbo.app.analytics.Tracker
import com.fpinbo.app.entities.Event
import com.fpinbo.app.event.view.EventFragmentArgs
import com.fpinbo.app.network.Api
import com.fpinbo.app.network.toEntity
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventViewModel @Inject constructor(
    private val intent: Intent,
    private val args: EventFragmentArgs,
    private val api: Api,
    private val tracker: Tracker
) : ViewModel() {

    private val _state = MutableLiveData<EventState>()

    val state: LiveData<EventState>
        get() = _state

    private val invalidInput = Error("Invalid Input")

    init {
        loadData()
    }

    fun trackShare(event: Event) = tracker.share(event.id.toString())

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
            .unsafeRunScoped(viewModelScope) { result ->
                val viewState = result.fold(
                    ifLeft = { Error(it.message.orEmpty()) },
                    ifRight = { it }
                )
                _state.postValue(viewState)
            }
    }

    private fun eventInExtras(eventFromArgs: Event?) = eventFromArgs != null

    private fun idInDeeplink(intentData: Uri?) =
        intentData != null && intentData.host == "fpinbo.dev" && !intentData.lastPathSegment.isNullOrBlank()

    private fun loadEventFromNetwork(lastPathSegment: String): IO<EventState> {
        val idFromUri = lastPathSegment.replace(".html", "")
        return api.events().map { listOfNetworkEvent ->
            val networkEvent =
                listOfNetworkEvent.firstOrNull { it.id.toString() == idFromUri }
            if (networkEvent != null) {
                Data(networkEvent.toEntity())
            } else {
                invalidInput
            }
        }
    }
}