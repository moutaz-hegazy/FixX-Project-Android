package com.example.fixx.NavigationBar.viewmodels

import com.example.fixx.POJOs.Person
import com.example.fixx.Support.FirestoreService

class NavBarViewmodel() {
    fun fetchUser(onCompletionBinder : (Person?)->Unit){
        FirestoreService.fetchUserFromDB(null,onCompletionBinder)
    }
}