package com.example.fixx.inAppChatScreens.viewModels

import android.util.Log
import com.example.fixx.POJOs.ChatMessage
import com.example.fixx.POJOs.Person
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.RetrofitInstance
import com.example.fixx.inAppChatScreens.model.ChatPushNotification
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ChatLogViewModel (var channel : String? = null, private val contact : String? = null,
                        private val observer : (msg : ChatMessage)->Unit,
                        private val onCompletion : (msgs : ArrayList<ChatMessage>)->Unit){

    fun fetchChatHistory(){
        if(channel != null){
            Log.i("TAG", ">>>> : with channel ")
            //FirestoreService.fetchChatHistoryForChannel(channel!!, observer, onCompletion)
            FirestoreService.fetchChatHistoryForChannelTest(channel!!,observer,onCompletion)
        }else{
            contact?.let {
//                FirestoreService.fetchChatHistoryForInstance(it, observer,
//                    onCompletion = {
//                            chatMsgs, channelName ->
//                        channel = channelName
//                        onCompletion(chatMsgs)
//                    })
                FirestoreService.fetchChatHistoryForInstanceTest(it,observer,
                    onCompletion = {
                        chatMsgs, channelName ->
                        channel = channelName
                        onCompletion(chatMsgs)
                })
            }
        }
    }

    fun fetchContact(onCompletion : (person : Person?) -> Unit){
        FirestoreService.fetchUserFromDB(contact,onCompletion)
    }

    fun sendMessage(message: ChatMessage){
        channel?.let {
            ch->
//            FirestoreService.sendChatMessage(message, ch)
            FirestoreService.sendChatMessageTest(ch,message)
        }
    }

    fun sendNotification(notification: ChatPushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
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