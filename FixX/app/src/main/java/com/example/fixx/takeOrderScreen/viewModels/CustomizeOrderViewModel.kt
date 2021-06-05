package com.example.fixx.takeOrderScreen.viewModels

import android.net.Uri
import android.provider.SyncStateContract
import android.util.Log
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.StringPair
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.TaskHandler
import com.example.fixx.constants.Constants
import java.util.logging.Handler

class CustomizeOrderViewModel() {

    fun uploadJob(job: Job, imagesUris: MutableList<Uri>, onSuccessBinding: (job: Job) -> Unit, onFailureBinding: () -> Unit) {
        Thread{
            if(!imagesUris.isNullOrEmpty()) {
                FirestoreService.uploadImageToStorage(imagesUris) { listOfImages ->
                    Log.i("TAG", "LISSSSSSSSSSST: ${listOfImages.size} ")
                    if(listOfImages.size == imagesUris.size){
                        job.images = listOfImages
                        FirestoreService.saveJobDetails(job, onSuccessBinding, onFailureBinding)
                    }
                }
            }else{
                FirestoreService.saveJobDetails(job, onSuccessBinding, onFailureBinding)
            }

        }.start()
    }

    fun updateJob(job: Job, newImagesUris: MutableList<Uri>, onSuccessBinding: (job:Job) -> Unit, onFailureBinding: () -> Unit){
        Thread{
            val jobChanges = mutableMapOf("location" to job.location!!,
                "date" to job.date, "fromTime" to (job.fromTime ?: ""), "toTime" to (job.toTime ?: ""),
                "images" to (job.images?: listOf<StringPair>()),
                "description" to job.description,"bidders" to mapOf<String,String>(),"privateRequest" to job.privateRequest)
            if(!newImagesUris.isNullOrEmpty()) {
                FirestoreService.uploadImageToStorage(newImagesUris) { listOfImages ->
                    Log.i("TAG", "LISSSSSSSSSSST: ${listOfImages.size} ")
                    if(listOfImages.size == newImagesUris.size){
                        if(job.images.isNullOrEmpty()) {
                            job.images = listOfImages
                        }else{
                            job.images?.addAll(listOfImages)
                        }
                        jobChanges["images"] = job.images ?: listOfImages
                        FirestoreService.updateDocument(Constants.JOBS_COLLECTION, jobChanges, job.jobId,
                            onSuccessHandler = {
                                onSuccessBinding(job)
                            },onFailureHandler = {
                                onFailureBinding()
                            })
                    }
                }
            }else{
                FirestoreService.updateDocument(Constants.JOBS_COLLECTION, jobChanges, job.jobId,
                    onSuccessHandler = {
                        onSuccessBinding(job)
                    },onFailureHandler = {
                        onFailureBinding()
                    })
            }
        }.start()
    }

    fun removeImage(imageId : String?){
        imageId?.let {
            FirestoreService.deleteImage(it)
        }
    }
}
