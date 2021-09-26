package com.fpinbo.app.analytics

import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class LifeCycleTrackerTest {

    private val tracker: Tracker = mockk(relaxed = true)
    private val sut = LifeCycleTracker(tracker)

    @Test
    fun foreground() {
        sut.onStart(mockk(relaxed = true))

        verify { tracker.appOpen() }
    }
}
