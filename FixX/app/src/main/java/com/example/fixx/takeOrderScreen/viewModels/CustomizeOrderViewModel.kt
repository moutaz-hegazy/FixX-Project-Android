package com.example.fixx.takeOrderScreen.viewModels

import android.net.Uri
import android.util.Log
import com.example.fixx.POJOs.Job
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.TaskHandler
import java.util.logging.Handler

class CustomizeOrderViewModel(val job : Job , val imagesUris : MutableList<Uri>, val onCommpletion : ()->Unit) {

    private val handler = TaskHandler{
        onCommpletion()
    }

    init {
        Thread{
            if(!imagesUris.isNullOrEmpty()) {
                FirestoreService.uploadImageToStorage(imagesUris) { listOfImages ->
                    Log.i("TAG", "LISSSSSSSSSSST: ${listOfImages.size} ")
                    if(listOfImages.size == imagesUris.size){
                        job.images = listOfImages
                        FirestoreService.saveJobDetails(job)
                        handler.sendEmptyMessage(20)
                    }
                }
            }else{
                FirestoreService.saveJobDetails(job)
            }

        }.start()
    }
}