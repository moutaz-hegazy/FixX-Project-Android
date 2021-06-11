package com.example.fixx.inAppChatScreens.viewModels

import android.util.Log
import com.example.fixx.POJOs.Person
import com.example.fixx.Support.FirestoreService

class NewMessageViewModel (val onComplete : (person : Person, chatChannel : String)->Unit){
    init {
        FirestoreService.fetchChatUsersTest {
            it.forEach {  info ->
                FirestoreService.fetchUserOnce(info.uid){
                        person ->
                    person?.let {
                            checkedPerson ->
                        onComplete(checkedPerson, info.channel ?: "")
                    }
                }
            }
        }
    }
}