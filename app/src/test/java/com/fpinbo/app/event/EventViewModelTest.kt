package com.fpinbo.app.event

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.fx.IO
import arrow.fx.typeclasses.milliseconds
import com.fpinbo.app.analytics.Tracker
import com.fpinbo.app.entities.Event
import com.fpinbo.app.event.view.EventFragmentArgs
import com.fpinbo.app.network.Api
import com.fpinbo.app.network.NetworkEvent
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EventViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val tracker: Tracker = mock()

    @Test
    fun eventInExtras() {

        val intent: Intent = mock {
            on { data } doReturn mock()
        }

        val event = Event(
            1,
            "title",
            "speaker",
            "imageUrl",
            "description",
            "videoUrl",
            "shareUrl"
        )
        val args = EventFragmentArgs.fromBundle(Bundle().apply {
            putParcelable(
                "Event", event
            )
        })

        val sut = buildSut(intent, args, mock())

        sut.state.test()
            .awaitValue()
            .assertValue(Data(event))
    }

    @Test
    fun deeplinkSuccess() {

        val intent: Intent = mock {
            on { data } doReturn Uri.parse("https://fpinbo.dev/1.html")
        }
        val args = EventFragmentArgs.fromBundle(Bundle.EMPTY)
        val api: Api = mock {
            on { events() } doReturn IO.sleep(10.milliseconds).flatMap {
                IO.just(
                    listOf(
                        NetworkEvent(
                            1,
                            "title",
                            "speaker",
                            "imageUrl",
                            "description",
                            "videoUrl"
                        )
                    )
                )
            }
        }


        val sut = buildSut(intent, args, api)

        sut.state.test()
            .awaitValue()
            .assertValue(
                Data(
                    Event(
                        1,
                        "title",
                        "speaker",
                        "imageUrl",
                        "description",
                        "videoUrl",
                        "https://fpinbo.dev/1.html"
                    )
                )
            )
    }

    @Test
    fun deeplinkError() {

        val intent: Intent = mock {
            on { data } doReturn Uri.parse("https://fpinbo.dev/1.html")
        }
        val args = EventFragmentArgs.fromBundle(Bundle.EMPTY)
        val api: Api = mock {
            on { events() } doReturn IO.sleep(10.milliseconds).flatMap {
                IO.raiseError<List<NetworkEvent>>(RuntimeException("Network Error"))
            }
        }


        val sut = buildSut(intent, args, api)

        sut.state.test()
            .awaitValue()
            .assertValue(
                Error("Network Error")
            )
    }

    @Test
    fun invalidInput() {
        val intent: Intent = mock {
            on { data } doReturn null
        }

        val args = EventFragmentArgs.fromBundle(Bundle.EMPTY)

        val sut = buildSut(intent, args, mock())

        sut.state.test()
            .awaitValue()
            .assertValue(Error("Invalid Input"))
    }

    @Test
    fun trackShare() {
        val sut = buildSut(mock(), mock(), mock())

        sut.trackShare(
            Event(
                1,
                "title",
                "speaker",
                "imageUrl",
                "description",
                "videoUrl",
                "shareUrl"
            )
        )

        verify(tracker).share("1")
    }

    private fun buildSut(
        intent: Intent,
        args: EventFragmentArgs,
        api: Api
    ) = EventViewModel(intent, args, api, tracker)
}