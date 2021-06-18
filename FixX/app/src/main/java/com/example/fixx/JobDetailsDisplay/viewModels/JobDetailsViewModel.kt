package com.example.fixx.JobDetailsDisplay.viewModels

import android.provider.SyncStateContract
import android.util.Log
import com.example.fixx.POJOs.Comment
import com.example.fixx.POJOs.Extension
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.RetrofitInstance
import com.example.fixx.constants.Constants
import com.example.fixx.techOrderDetailsScreen.models.ReplyNotificationData
import com.example.fixx.techOrderDetailsScreen.models.TechReplyPushNotification
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.gson.Gson
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class JobDetailsViewModel(private val jobId : String) {

    private var userObserver : ListenerRegistration? = null
    fun fetchJobfromDB(onSuccessBinding : (job : Job, List<Extension>)->Unit, onFailBinding : () -> Unit){
        FirestoreService.fetchJobById(jobId,
            onSuccessHandler = {    job ->
                fetchExtensionForJob(onSuccessBinding = {   exts ->
                    onSuccessBinding(job, exts)
                },onFailBinding = {})
        }, onFailHandler = {
            onFailBinding()
        })
    }

    fun fetchExtensionForJob(onSuccessBinding: (exts : List<Extension>) -> Unit,onFailBinding: () -> Unit){
        FirestoreService.fetchExtensionsForJob(jobId,onSuccessBinding,onFailBinding)
    }

    fun getTechnician(techId : String, onSuccessBinding: (tech: Technician) -> Unit, onFailBinding: () -> Unit){
        FirestoreService.fetchUserOnce(techId) {   person ->
            Log.i("TAG", "getTechnician: HERE 2")
            val tech = person as? Technician
            if(tech != null){
                onSuccessBinding(tech)
            }else{
                onFailBinding()
            }
        }
    }

    fun unregisterObserver(){
        userObserver?.let{
            Log.i("TAG", "unregisterObserver: <<<<<<<%% UNREGISTER USER")
            it.remove()
        }
    }

    fun removeSingleBidder(){
        FirestoreService.removeBidders(jobId)
    }

    fun setTechnicianUidWithPrice(uid : String, price : String){
        FirestoreService.selectTechForJob(uid,jobId,price)
    }

    fun sendAcceptNotification(techUid : String, techToken : String, userName : String) = CoroutineScope(Dispatchers.IO)
        .launch {
            val pushNotification = TechReplyPushNotification(ReplyNotificationData(Constants.NOTIFICATION_TYPE_USER_ACCEPT,
                userName, R.string.Job_Accepted,R.string.Job_Accept_Msg,jobId), arrayOf(techToken))
            try {
                val response = RetrofitInstance.api.postTechReplyNotification(pushNotification)
                if(response.isSuccessful) {
                    Log.d("TAG", "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e("TAG", response.errorBody()!!.string())
                }
            } catch(e: Exception) {
                Log.e("TAG", e.toString())
            }
        }

    fun removeJob(onSuccessBinding: () -> Unit, onFailBinding: () -> Unit){
        FirestoreService.removeJob(jobId, onSuccessBinding, onFailBinding)
    }

    fun postRatingAndCommentToTechnician(jobId:String,techId : String, rate : Double, comment : Comment, extraRating:Double,reviews : Int,
                                         onSuccessBinding: () -> Unit, onFailBinding: () -> Unit){
        FirestoreService.addRatingAndComment(techId,rate,extraRating,comment,reviews,onSuccessHandler = {
            FirestoreService.updateDocumentField(Constants.JOBS_COLLECTION,"rateable",false
                ,jobId,onSuccessBinding,onFailBinding)
        },onFailHandler = {
            onFailBinding()
        })
    }
}