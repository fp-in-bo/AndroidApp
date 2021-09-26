package com.fpinbo.app.event

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.right
import com.fpinbo.app.analytics.Tracker
import com.fpinbo.app.entities.Event
import com.fpinbo.app.event.dynamiclink.DynamicLinkBuilder
import com.fpinbo.app.event.view.EventFragmentArgs
import com.fpinbo.app.network.Api
import com.fpinbo.app.network.toEntity
import com.fpinbo.app.utils.ViewEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventViewModel @Inject constructor(
    private val intent: Intent,
    private val args: EventFragmentArgs,
    private val api: Api,
    private val dynamicLinkBuilder: DynamicLinkBuilder,
    private val tracker: Tracker
) : ViewModel() {

    private val _state = MutableLiveData<EventState>()

    private val _viewEvent = MutableLiveData<ViewEvent<ShareEvent>>()

    val state: LiveData<EventState>
        get() = _state

    val viewEvent: LiveData<ViewEvent<ShareEvent>>
        get() = _viewEvent

    private val invalidInput = Error("Invalid Input")

    init {
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        val eventFromArgs = args.event
        val intentData = intent.data
        val viewState: EventState = when {
            eventInExtras(eventFromArgs) -> {
                Data(eventFromArgs!!).right()
            }
            idInDeeplink(intentData) -> {
                loadEventFromNetwork(intentData!!.lastPathSegment!!)
            }
            else -> invalidInput.right()
        }.fold(
            ifLeft = { Error(it.message.orEmpty()) },
            ifRight = { it }
        )
        _state.postValue(viewState)

    }

    private fun eventInExtras(eventFromArgs: Event?) = eventFromArgs != null

    private fun idInDeeplink(intentData: Uri?) =
        intentData != null && intentData.host == "fpinbo.dev" && !intentData.lastPathSegment.isNullOrBlank()

    private suspend fun loadEventFromNetwork(lastPathSegment: String): Either<Throwable, EventState> {
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

    fun onShare(event: Event) {
        viewModelScope.launch {
            val result = dynamicLinkBuilder.build(event)
            val share = result.fold(
                ifLeft = { ShareEvent.Error(it.message.orEmpty()) },
                ifRight = {
                    ShareEvent.Success(
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "${event.title} - $it")
                        }
                    )
                }
            )
            _viewEvent.postValue(ViewEvent(share))
        }
        tracker.share(event.id.toString())


    }
}
