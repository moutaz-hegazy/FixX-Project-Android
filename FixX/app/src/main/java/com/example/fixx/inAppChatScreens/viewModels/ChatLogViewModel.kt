package com.example.fixx.inAppChatScreens.viewModels

import com.example.fixx.POJOs.ChatMessage
import com.example.fixx.Support.FirestoreService

class ChatLogViewModel (private var channel : String? = null, private val contact : String? = null,
                        private val observer : (msg : ChatMessage)->Unit,
                        private val onCompletion : (msgs : ArrayList<ChatMessage>)->Unit){

    init {
        if(channel != null){
            FirestoreService.fetchChatHistoryForChannel(channel!!, observer, onCompletion)
        }else{
            contact?.let {
                FirestoreService.fetchChatHistoryForInstance(it, observer,
                    onCompletion = {
                        chatMsgs, channelName ->
                        channel = channelName
                        onCompletion(chatMsgs)
                    })
            }
        }
    }

    fun sendMessage(message: ChatMessage){
        channel?.let {
            ch->
            FirestoreService.sendChatMessage(message, ch)
        }
    }
}