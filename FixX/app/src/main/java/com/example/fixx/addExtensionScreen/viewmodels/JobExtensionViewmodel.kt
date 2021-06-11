package com.example.fixx.addExtensionScreen.viewmodels

import android.net.Uri
import android.util.Log
import com.example.fixx.POJOs.Extension
import com.example.fixx.Support.FirestoreService

class JobExtensionViewmodel (val jobId : String){

    fun uploadExtension(ext: Extension, imagesUris: MutableList<Uri>,
                  onSuccessBinding: (ext : Extension) -> Unit, onFailureBinding: () -> Unit) {
        Thread{
            if(!imagesUris.isNullOrEmpty()) {
                FirestoreService.uploadImageToStorage(imagesUris) { listOfImages ->
                    Log.i("TAG", "LISSSSSSSSSSST: ${listOfImages.size} ")
                    if(listOfImages.size == imagesUris.size){
                        ext.images = listOfImages
                        FirestoreService.addExtensionToJob(jobId,ext, onSuccessBinding, onFailureBinding)
                    }
                }
            }else{
                FirestoreService.addExtensionToJob(jobId,ext, onSuccessBinding, onFailureBinding)
            }

        }.start()
    }
}