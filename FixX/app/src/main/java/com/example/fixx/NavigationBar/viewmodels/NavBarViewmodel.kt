package com.example.fixx.NavigationBar.viewmodels

import android.util.Log
import com.example.fixx.POJOs.Person
import com.example.fixx.Support.FirestoreService
import com.google.firebase.firestore.ListenerRegistration

class NavBarViewmodel() {
    fun fetchUser(onCompletionBinder : (Person?)->Unit){
        FirestoreService.fetchUserOnce {
            onCompletionBinder(it)
        }
    }
}