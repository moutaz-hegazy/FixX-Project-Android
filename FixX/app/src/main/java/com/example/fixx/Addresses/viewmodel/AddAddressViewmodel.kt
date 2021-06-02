package com.example.fixx.Addresses.viewmodel

import android.util.Log
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants

class AddAddressViewmodel(private val address : String, onSuccessBinding:()->Unit, onFailBinding : ()->Unit) {
    init {

        if(USER_OBJECT?.locations == null){
            USER_OBJECT?.locations = mutableListOf(address)
        }else{
            USER_OBJECT?.locations?.add(address)
        }
        Log.i("TAG", ">>>>>>>>>> ${USER_OBJECT?.locations} ")
        FirestoreService.updateUserLocations(address,
            onSuccessHandler = {
                onSuccessBinding()
            },onFailHandler = {
                onFailBinding()
            })
    }
}