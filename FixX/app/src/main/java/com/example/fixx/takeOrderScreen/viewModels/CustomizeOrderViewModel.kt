package com.example.fixx.takeOrderScreen.viewModels

import android.net.Uri
import com.example.fixx.POJOs.Job
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.TaskHandler
import java.util.logging.Handler

class CustomizeOrderViewModel(val job : Job, val onCommpletion : (String)->Unit) {

    private val handler = TaskHandler{
        //onCommpletion("")
    }

    init {
        Thread{
            FirestoreService.saveJobDetails(job){
                id ->
                job.jobId = id

                onCommpletion(id)
            }


            handler.sendEmptyMessage(20)
        }.start()
    }
}