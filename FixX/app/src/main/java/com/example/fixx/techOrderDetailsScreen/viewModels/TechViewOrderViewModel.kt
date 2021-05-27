package com.example.fixx.techOrderDetailsScreen.viewModels

import android.util.Log
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.Person
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.RetrofitInstance
import com.example.fixx.techOrderDetailsScreen.models.ReplyNotificationData
import com.example.fixx.techOrderDetailsScreen.models.TechReplyPushNotification
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class TechViewOrderViewModel() {
    fun fetchUserFromDB(uid: String?, onSuccessBinding: (user: Person?) -> Unit) {
        FirestoreService.fetchUserFromDB(uid, onSuccessBinding)
    }

    fun sendDenyNotification(notification : TechReplyPushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postTechReplyNotification(notification)
            if (response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch (e: Exception) {
            Log.e("TAG", e.toString())
        }
    }

    fun fetchJobFromDB(jobId : String, onSuccessBinding: (job: Job) -> Unit, onFailBinding : () -> Unit){
        FirestoreService.fetchJobById(jobId,onSuccessBinding,onFailBinding)
    }
}