package com.example.fixx.takeOrderScreen.viewModels

import com.example.fixx.POJOs.Job
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.TaskHandler
import java.util.logging.Handler

class CustomizeOrderViewModel(val job : Job , val onCommpletion : ()->Unit) {

    private val handler = TaskHandler{
        onCommpletion()
    }

    init {
        Thread{
            FirestoreService.saveJobDetails(job)
            handler.sendEmptyMessage(20)
        }.start()
    }
}