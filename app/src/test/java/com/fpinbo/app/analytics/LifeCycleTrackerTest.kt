package com.fpinbo.app.analytics

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class LifeCycleTrackerTest {

    private val tracker: Tracker = mock()
    private val sut = LifeCycleTracker(tracker)

    @Test
    fun foreground() {
        sut.onStart(mock())

        verify(tracker).appOpen()
    }
}