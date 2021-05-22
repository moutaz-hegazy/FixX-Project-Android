package com.example.fixx.test

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class TestService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.i("TAG", "onNewToken: TOKEN >>> "+p0)
    }
}