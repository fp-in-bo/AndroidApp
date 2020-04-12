package com.fpinbo.app.events

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.fx.IO
import arrow.fx.typeclasses.milliseconds
import com.fpinbo.app.analytics.Tracker
import com.fpinbo.app.entities.Event
import com.fpinbo.app.network.Api
import com.fpinbo.app.network.NetworkEvent
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Rule
import org.junit.Test

class EventsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val tracker: Tracker = mock()

    @Test
    fun loadDataSuccess() {
        val api: Api = mock {
            on { events() } doReturn IO.sleep(10.milliseconds).flatMap {
                IO.just(
                    listOf(
                        NetworkEvent(
                            id = 1,
                            title = "title",
                            speaker = "speaker",
                            imageUrl = "imageUrl",
                            description = "description"
                        )
                    )
                )
            }
        }

        val sut = buildSut(api)

        sut.state.test()
            .awaitNextValue()
            .assertValueHistory(
                Loading,
                Events(
                    listOf(
                        Event(
                            id = 1,
                            title = "title",
                            speaker = "speaker",
                            imageUrl = "imageUrl",
                            description = "description",
                            videoUrl = null,
                            shareUrl = "https://fpinbo.dev/1.html"
                        )
                    )
                )
            )
    }

    @Test
    fun loadDataError() {
        val api: Api = mock {
            on { events() } doReturn IO.sleep(10.milliseconds).flatMap {
                IO.raiseError<List<NetworkEvent>>(RuntimeException("error message"))
            }
        }

        val sut = buildSut(api)

        sut.state.test()
            .awaitNextValue()
            .assertValueHistory(
                Loading,
                Error("error message")
            )
    }

    @Test
    fun trackSelectItem() {
        val api: Api = mock {
            on { events() } doReturn IO.just(emptyList())
        }
        val sut = buildSut(api)
        sut.trackSelectItem(
            Event(
                id = 1,
                title = "title",
                speaker = "speaker",
                imageUrl = "imageUrl",
                description = "description",
                videoUrl = "videoUrl",
                shareUrl = "shareUrl"
            )
        )

        verify(tracker).selectItem("1", "title")
    }

    private fun buildSut(api: Api): EventsViewModel = EventsViewModel(api, tracker)
}