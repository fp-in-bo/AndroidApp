package com.fpinbo.app.event

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.left
import arrow.core.right
import com.fpinbo.app.analytics.Tracker
import com.fpinbo.app.entities.Event
import com.fpinbo.app.event.dynamiclink.DynamicLinkBuilder
import com.fpinbo.app.event.view.EventFragmentArgs
import com.fpinbo.app.network.Api
import com.fpinbo.app.network.NetworkEvent
import com.jraska.livedata.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EventViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val tracker: Tracker = mockk(relaxed = true)

    @Test
    fun eventInExtras() {

        val intent: Intent = mockk {
            every { data } returns mockk()
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

        val sut = buildSut(intent, args, mockk(), mockk())

        sut.state.test()
            .awaitValue()
            .assertValue(Data(event))
    }

    @Test
    fun deeplinkSuccess() {

        val intent: Intent = mockk {
            every { data } returns Uri.parse("https://fpinbo.dev/1.html")
        }
        val args = EventFragmentArgs.fromBundle(Bundle.EMPTY)
        val api: Api = mockk {
            coEvery { events() } returns
                    listOf(
                        NetworkEvent(
                            1,
                            "title",
                            "speaker",
                            "imageUrl",
                            "description",
                            "videoUrl"
                        )
                    ).right()

        }


        val sut = buildSut(intent, args, api, mockk())

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

        val intent: Intent = mockk {
            every { data } returns Uri.parse("https://fpinbo.dev/1.html")
        }
        val args = EventFragmentArgs.fromBundle(Bundle.EMPTY)
        val api: Api = mockk {
            coEvery { events() } returns
                    RuntimeException("Network Error").left()

        }


        val sut = buildSut(intent, args, api, mockk())

        sut.state.test()
            .awaitValue()
            .assertValue(
                Error("Network Error")
            )
    }

    @Test
    fun invalidInput() {
        val intent: Intent = mockk {
            every { data } returns null
        }

        val args = EventFragmentArgs.fromBundle(Bundle.EMPTY)

        val sut = buildSut(intent, args, mockk(), mockk())

        sut.state.test()
            .awaitValue()
            .assertValue(Error("Invalid Input"))
    }

    @Test
    fun shareSuccess() {
        val event = Event(
            1,
            "title",
            "speaker",
            "imageUrl",
            "description",
            "videoUrl",
            "shareUrl"
        )

        val dynamicLinkBuilder = mockk<DynamicLinkBuilder> {
            coEvery { build(event) } returns "link".right()
        }

        val sut = buildSut(mockk(), mockk(), mockk(), dynamicLinkBuilder)

        sut.onShare(event)

        sut.viewEvent.test()
            .awaitValue()
            .assertValue {
                val intent = (it.content as ShareEvent.Success).intent
                intent.action == Intent.ACTION_SEND &&
                        intent.type == "text/plain" &&
                        intent.getStringExtra(Intent.EXTRA_TEXT) == "title - link"
            }
    }

    @Test
    fun shareError() {
        val event = Event(
            1,
            "title",
            "speaker",
            "imageUrl",
            "description",
            "videoUrl",
            "shareUrl"
        )

        val dynamicLinkBuilder = mockk<DynamicLinkBuilder> {
            coEvery { build(event) } returns RuntimeException("Error Message").left()
        }

        val sut = buildSut(mockk(), mockk(), mockk(), dynamicLinkBuilder)

        sut.onShare(event)

        sut.viewEvent.test()
            .awaitValue()
            .assertValue {
                val message = (it.content as ShareEvent.Error).message
                "Error Message" == message
            }
    }

    @Test
    fun trackShare() {
        val event = Event(
            1,
            "title",
            "speaker",
            "imageUrl",
            "description",
            "videoUrl",
            "shareUrl"
        )

        val dynamicLinkBuilder = mockk<DynamicLinkBuilder> {
            coEvery { build(event) } returns "link".right()
        }

        val sut = buildSut(mockk(), mockk(), mockk(), dynamicLinkBuilder)

        sut.onShare(event)

        verify { tracker.share("1") }
    }

    private fun buildSut(
        intent: Intent,
        args: EventFragmentArgs,
        api: Api,
        dynamicLinkBuilder: DynamicLinkBuilder
    ) = EventViewModel(intent, args, api, dynamicLinkBuilder, tracker)
}
