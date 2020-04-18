package com.fpinbo.app.event

import android.content.Intent

sealed class ShareEvent {

    data class Success(
        val intent: Intent
    ) : ShareEvent()

    data class Error(
        val message: String
    ) : ShareEvent()
}