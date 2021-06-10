package com.example.fixx.takeOrderScreen.viewModels

import android.net.Uri
import android.provider.SyncStateContract
import android.util.Log
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.StringPair
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.RetrofitInstance
import com.example.fixx.Support.TaskHandler
import com.example.fixx.constants.Constants
import com.example.fixx.inAppChatScreens.model.ChatPushNotification
import com.example.fixx.showTechnicianScreen.models.MultiJobRequestPushNotification
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
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
            val jobChanges = mutableMapOf("location" to job.location!!,"areaLocation" to job.areaLocation!!,
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


    fun sendNotification(notification: MultiJobRequestPushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postAreaJobNotification(notification)
            if(response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch(e: Exception) {
            Log.e("TAG", e.toString())
        }
    }
}
