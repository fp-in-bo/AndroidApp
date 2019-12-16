package com.fpinbo.app.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.unsafeRunAsyncInViewModel
import arrow.fx.IO
import arrow.fx.typeclasses.seconds
import javax.inject.Inject

class EventsViewModel @Inject constructor() : ViewModel() {

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

    private fun retrieveData(): IO<Events> {

        return IO.sleep(2.seconds).flatMap {

            IO.just(
                Events(
                    listOf(
                        Event(
                            "(IM)Practical Functional Programming, adopting FP in industry",
                            "Alessandro Zoffoli",
                            "https://scontent-fco1-1.xx.fbcdn.net/v/t1.0-9/78409006_10162737638040261_986400575954354176_o.jpg?_nc_cat=104&_nc_ohc=hiQmuclhmuAAQl56rV1Z8TU-eK4EeEPLgwJevwCCQ-bz_9gVAYnpuweRQ&_nc_ht=scontent-fco1-1.xx&oh=85e7121d1792569bd7a6b23f5a1c7b6f&oe=5E7C0C26"
                        ),
                        Event(
                            "KindOf<Polymorphism>",
                            "Daniele Campogiani",
                            "https://scontent-fco1-1.xx.fbcdn.net/v/t1.0-9/72071204_10162500322720261_6644187464858599424_o.jpg?_nc_cat=111&_nc_ohc=HVNoKnl-MKQAQknY1qHaIRDw9Jw2D-Fz1qxzmgSsPqUkVUSM6B2j6PmFA&_nc_ht=scontent-fco1-1.xx&oh=2bf933781e0e3bc455a579382ced0fa4&oe=5E75D343"
                        )
                    )
                )
            )
        }
    }
}