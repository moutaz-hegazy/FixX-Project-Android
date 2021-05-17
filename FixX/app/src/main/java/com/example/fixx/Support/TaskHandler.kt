package com.example.fixx.Support


import android.os.Handler
import android.os.Message

class TaskHandler(val onCompletion: (Message) -> Unit ): Handler() {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        onCompletion(msg)
    }
}