package com.example.fixx.showTechnicianScreen.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fixx.POJOs.Technician
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.RetrofitInstance
import com.example.fixx.inAppChatScreens.model.ChatPushNotification
import com.example.fixx.showTechnicianScreen.models.JobRequestData
import com.example.fixx.showTechnicianScreen.models.SingleJobRequestPushNotification
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class RecyclerActivityViewModel(private val location : String?, private val jobType : String?) : ViewModel() {

    var recyclerListData = MutableLiveData<MutableList<Technician>>()
    var newList : MutableList<Technician> = mutableListOf()


    init {
        location?.let{
            jobType?.let {
                var x = FirestoreService.searchForTechnicianByJobAndLocation(jobType, location){
                    Log.i("TAG", " RECYCLER: $it ")
                    newList.addAll(it)
                    recyclerListData.value = newList
                }
                Log.i("TAG", "new list: ${newList.count()}")
            }
        }
    }

    fun add(technician: Technician ){
        newList.add(technician)
        recyclerListData.value = newList

    }

    fun sendNotification(notificationData: JobRequestData, position : Int) = CoroutineScope(Dispatchers.IO).launch {
        Log.i("TAG", "sendNotification: HERE 1 <<<<")
        newList[position].token?.let {  token ->
            Log.i("TAG", "sendNotification: HERE 2 <<<< $token")
            val notification = SingleJobRequestPushNotification(notificationData, arrayOf(token))
            try {
                Log.i("TAG", "sendNotification: HERE 3 <<<<")
                val response = RetrofitInstance.api.postSingleJobNotification(notification)
                Log.i("TAG", "sendNotification: HERE 4 <<<<")
                if(response.isSuccessful) {
                    Log.d("TAG", "Response: ${Gson().toJson(response)}")
                    Log.i("TAG", "sendNotification: HERE 5 <<<<")
                } else {
                    Log.e("TAG", response.errorBody()!!.string())
                    Log.i("TAG", "sendNotification: HERE 6 <<<<")
                }
            } catch(e: Exception) {
                Log.e("TAG", e.toString())
            }
        }
    }
}