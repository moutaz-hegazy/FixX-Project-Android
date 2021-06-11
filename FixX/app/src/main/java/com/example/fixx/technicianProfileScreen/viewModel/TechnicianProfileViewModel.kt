package com.example.fixx.technicianProfileScreen.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fixx.POJOs.Comment
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.RetrofitInstance
import com.example.fixx.showTechnicianScreen.models.JobRequestData
import com.example.fixx.showTechnicianScreen.models.SingleJobRequestPushNotification
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TechnicianProfileViewModel(private val tech : String,private val token: String?,
                                 private val onFailBinding: ()->Unit) : ViewModel() {
    var recyclerListData = MutableLiveData<MutableList<Comment>>()
    var newList : MutableList<Comment> = mutableListOf()

    init {
        FirestoreService.fetchCommentsForTech(tech, onSuccessHandler = {
            recyclerListData.value =
                it.filter { it.commentContent.isNullOrEmpty() != true } as? MutableList<Comment>
            Log.i("TAG", ">>>>>>>>>>>>>>>> :  ${recyclerListData.value} ")
        }, onFailHandler = {
            onFailBinding()
        })
    }

    fun sendNotification(notificationData: JobRequestData) = CoroutineScope(
        Dispatchers.IO).launch {
        token?.let {  token ->
            val notification = SingleJobRequestPushNotification(notificationData, arrayOf(token))
            try {
                val response = RetrofitInstance.api.postSingleJobNotification(notification)
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
}