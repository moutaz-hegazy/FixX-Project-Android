package com.example.fixx.takeOrderScreen.viewModels

import android.net.Uri
import android.util.Log
import com.example.fixx.POJOs.Job
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.TaskHandler
import java.util.logging.Handler

class CustomizeOrderViewModel(val job : Job , val imagesUris : MutableList<Uri>,
                              val onSuccessBinding : (job : Job)->Unit,
                              val onFaliureBinding : ()->Unit) {

    init {
        Thread{
            if(!imagesUris.isNullOrEmpty()) {
                FirestoreService.uploadImageToStorage(imagesUris) { listOfImages ->
                    Log.i("TAG", "LISSSSSSSSSSST: ${listOfImages.size} ")
                    if(listOfImages.size == imagesUris.size){
                        job.images = listOfImages
                        FirestoreService.saveJobDetails(job, onSuccessBinding, onFaliureBinding)
                    }
                }
            }else{
                FirestoreService.saveJobDetails(job, onSuccessBinding, onFaliureBinding)
            }

        }.start()
    }
}