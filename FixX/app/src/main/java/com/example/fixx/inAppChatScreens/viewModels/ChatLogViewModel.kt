package com.example.fixx.inAppChatScreens.viewModels

import android.util.Log
import com.example.fixx.POJOs.ChatMessage
import com.example.fixx.POJOs.ContactInfo
import com.example.fixx.POJOs.Person
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.RetrofitInstance
import com.example.fixx.inAppChatScreens.model.ChatPushNotification
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ChatLogViewModel (var channel : String? = null, private val contact : String? = null,
                        private val observer : (msg : ChatMessage)->Unit){

    private var chatObserver : ChildEventListener? = null
    private var chatRef : DatabaseReference? = null

    fun fetchChatHistory(){
        if(channel != null){
            FirestoreService.fetchChatHistoryForChannelTest(channel!!,observer,
                chatRegHandler = {  reg,ref ->
                    chatObserver = reg
                    chatRef = ref
            })
        }else{
            contact?.let {
                FirestoreService.fetchChatHistoryForInstanceTest(it,observer,
                    onCompletion = { channelName ->
                        channel = channelName
                        addContactToList(contact)
                },chatRegHandler = {    reg,ref ->
                        chatObserver = reg
                        chatRef = ref
                })
            }
        }
    }

    fun removeObserver(){
        chatObserver?.let {
            chatRef?.removeEventListener(it)
        }
    }

    fun fetchContact(onCompletion : (person : Person?) -> Unit){
        FirestoreService.fetchUserOnce(contact,onCompletion)
    }

    fun sendMessage(message: ChatMessage){
        channel?.let {
            ch->
            FirestoreService.sendChatMessageTest(ch,message)
        }
    }

    fun addContactToList(contact: String){
        FirestoreService.addNewChatContatct(ContactInfo(contact,channel))
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