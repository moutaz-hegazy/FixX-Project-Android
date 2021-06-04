package com.example.fixx.WorkAddresses.viewmodels

import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants

class WorkAddressesViewmodel(val uid : String) {

    fun addNewWorkLocation(location : String, onSuccessBinding: ()->Unit, onFailBinding: ()->Unit){
        FirestoreService.updateWorkLocations(location, onSuccessBinding, onFailBinding)
    }

    fun removeWorkLocation(location: String, onSuccessBinding: () -> Unit, onFailBinding: () -> Unit){
        FirestoreService.removeWorkLocation(location, onSuccessBinding, onFailBinding)
    }

}