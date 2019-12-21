package com.fpinbo.app.push

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FPInBoFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("MessagingService", "token: $p0")
    }
}