package com.example.fixx.technicianProfileScreen.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fixx.POJOs.Comment
import com.example.fixx.POJOs.Technician
import com.example.fixx.Support.RetrofitInstance
import com.example.fixx.showTechnicianScreen.models.JobRequestData
import com.example.fixx.showTechnicianScreen.models.SingleJobRequestPushNotification
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TechnicianProfileViewModel(val tech : Technician) : ViewModel() {
    var recyclerListData = MutableLiveData<MutableList<Comment>>()
    var newList : MutableList<Comment> = mutableListOf()

    init {
        newList = mutableListOf(
            Comment("noha", "aaaaaaaaaaaaaaaaaaaaaaaaaaa"),
                Comment("noha tany", "bbbbbbbbbbbbb"),
                Comment("yomna", "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"),
                Comment("noha tany2", "wwwwwwwwwwwwwwwwwwwwwwwwwwwww"),
                Comment("noha tany3", "bbbbbbbbbbbbb"),
                Comment("noha tany3", "bbbbbbbbbbbbb"),
                Comment("noha tany3", "bbbbbbbbbbbbb")
        )
        recyclerListData.value = newList
    }

    fun sendNotification(notificationData: JobRequestData) = CoroutineScope(
        Dispatchers.IO).launch {
        tech.token?.let {  token ->
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