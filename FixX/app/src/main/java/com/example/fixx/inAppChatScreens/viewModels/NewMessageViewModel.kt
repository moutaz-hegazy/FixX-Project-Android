package com.example.fixx.inAppChatScreens.viewModels

import com.example.fixx.POJOs.Person
import com.example.fixx.Support.FirestoreService

class NewMessageViewModel (val onComplete : (person : Person)->Unit){
    init {
        FirestoreService.fetchChatUsers {
            contacts ->
            contacts?.forEach {
                uid ->
                FirestoreService.fetchUserFromDB(uid){
                    person ->
                    person?.let {
                        checkedPerson ->
                        onComplete(checkedPerson)
                    }
                }
            }
        }
    }
}