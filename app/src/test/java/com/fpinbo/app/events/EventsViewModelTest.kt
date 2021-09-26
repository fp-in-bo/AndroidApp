package com.fpinbo.app.events

import CoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.left
import arrow.core.right
import com.fpinbo.app.analytics.Tracker
import com.fpinbo.app.entities.Event
import com.fpinbo.app.network.Api
import com.fpinbo.app.network.NetworkEvent
import com.jraska.livedata.test
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EventsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val tracker: Tracker = mockk(relaxed = true)

    @Test
    fun loadDataSuccess() {
        val api: Api = mockk {
            coEvery { events() } returns
                    listOf(
                        NetworkEvent(
                            id = 1,
                            title = "title",
                            speaker = "speaker",
                            imageUrl = "imageUrl",
                            description = "description"
                        )
                    ).right()

        }

        val sut = buildSut(api)

        sut.state.test()
            .assertValue(
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
        val api: Api = mockk {
            coEvery { events() } returns RuntimeException("error message").left()
        }

        val sut = buildSut(api)

        sut.state.test()
            .assertValue(
                Error("error message")
            )
    }

    @Test
    fun trackSelectItem() {
        val api: Api = mockk {
            coEvery { events() } returns emptyList<NetworkEvent>().right()
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

        verify { tracker.selectItem("1", "title") }
    }

    private fun buildSut(api: Api): EventsViewModel = EventsViewModel(api, tracker)
}
