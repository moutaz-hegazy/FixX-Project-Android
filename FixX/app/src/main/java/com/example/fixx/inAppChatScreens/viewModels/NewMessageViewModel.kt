package com.example.fixx.inAppChatScreens.viewModels

import com.example.fixx.POJOs.Person
import com.example.fixx.Support.FirestoreService

class NewMessageViewModel (val onComplete : (person : Person, chatChannel : String)->Unit){
    init {
        FirestoreService.fetchChatUsers {
            contacts, channels ->
            contacts?.forEach {
                uid ->
                val channel = channels[contacts.indexOf(uid)]
                FirestoreService.fetchUserFromDB(uid){
                    person ->
                    person?.let {
                        checkedPerson ->
                        onComplete(checkedPerson, channel)
                    }
                }
            }
        }
    }
}