package com.example.fixx.NavigationBar.viewmodels

import com.example.fixx.Support.FirestoreService

class SettingsViewmodel {

    fun signoutAccount(){
        FirestoreService.auth.signOut()
    }
}