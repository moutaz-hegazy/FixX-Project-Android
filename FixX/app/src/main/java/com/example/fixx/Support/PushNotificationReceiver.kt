package com.example.fixx.Support

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

class PushNotificationReceiver()
    : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("TAG", "onReceive: IAM ALIVE!!!!!!!!!!!!!")
    }
}