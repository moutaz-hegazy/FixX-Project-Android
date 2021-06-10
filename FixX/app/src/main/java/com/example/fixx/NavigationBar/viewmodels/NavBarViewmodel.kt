package com.example.fixx.NavigationBar.viewmodels

import android.util.Log
import com.example.fixx.POJOs.Person
import com.example.fixx.Support.FirestoreService

class NavBarViewmodel() {
    fun fetchUser(onCompletionBinder : (Person?)->Unit){
        FirestoreService.fetchUserFromDB{
            Log.i("TAG", "fetchUser: <<<<<<<<<<<<<<< Done Fetching !!")
            onCompletionBinder(it)
        }
    }
}