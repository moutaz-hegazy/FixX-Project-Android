package com.example.fixx.techOrderDetailsScreen.viewModels

import android.util.Log
import com.example.fixx.POJOs.Extension
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.Person
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.RetrofitInstance
import com.example.fixx.constants.Constants
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
        FirestoreService.fetchUserOnce(uid, onSuccessBinding)
    }

    fun sendReplyNotification(notification : TechReplyPushNotification) = CoroutineScope(Dispatchers.IO).launch {
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

    fun fetchJobFromDB(jobId : String, onSuccessBinding: (job: Job, exts:List<Extension>) -> Unit, onFailBinding : () -> Unit){
        FirestoreService.fetchJobById(jobId,
            onSuccessHandler = {    job ->
                FirestoreService.fetchExtensionsForJob(jobId,onSuccessHandler = {   exts ->
                    onSuccessBinding(job,exts)
                },onFailHandler = {})
            },onFailHandler = {
                onFailBinding()
            })
    }

    fun fetchExtensionsForJob(jobId: String,onSuccessBinding: (exts:List<Extension>) -> Unit, onFailBinding: () -> Unit){
        FirestoreService.fetchExtensionsForJob(jobId,onSuccessBinding,onFailBinding)
    }

    fun updateExtensionPrice(jobId: String, extId:String, price : Int, onSuccessBinding: () -> Unit,onFailBinding: () -> Unit){
        FirestoreService.updateExtensionPrice(jobId,extId,price,onSuccessBinding,onFailBinding)
    }

    fun removeExtension(jobId: String,extId: String,onSuccessBinding: () -> Unit,onFailBinding: () -> Unit){
        FirestoreService.removeExtension(jobId,extId,onSuccessBinding,onFailBinding)
    }

    fun addToBidders(jobId : String,bidders: Map<String,String> , onSuccessBinding: () -> Unit){
        FirestoreService.addBidder(jobId,bidders){
            onSuccessBinding()
        }
    }

    fun removeBidders(jobId:String){
        FirestoreService.removeBidders(jobId)
    }

    fun removeSelfFromBidders(jobId: String, restOfBidders : Map<String,String>,
                              onSuccessBinding: () -> Unit,onFailBinding: () -> Unit){
        FirestoreService.updateDocumentField(Constants.JOBS_COLLECTION,"bidders",restOfBidders,
            jobId,onSuccessBinding,onFailBinding)
    }

    fun canceledJob(jobId: String) {
        FirestoreService.removeTechnicianFromJob(jobId)
    }

    fun completeJob(jobId : String,date:String, onSuccessBinding: () -> Unit, onFailBinding: () -> Unit){
        FirestoreService.updateDocument(Constants.JOBS_COLLECTION, mapOf("status" to Job.JobStatus.Completed.rawValue,
            "completionDate" to date, "rateable" to true,"commentable" to true),jobId,onSuccessBinding,onFailBinding)
    }

    fun updateRatingAndJobCount(uid : String,rating : Double,monthlyRaing : Int,
                                jobCount : Int,onSuccessBinding: () -> Unit,onFailBinding: () -> Unit){
        FirestoreService.updateDocument(Constants.USERS_COLLECTION, mapOf("rating" to rating, "jobsCount" to jobCount,
            "monthlyRating" to monthlyRaing),
            uid,onSuccessBinding,onFailBinding)
    }
}