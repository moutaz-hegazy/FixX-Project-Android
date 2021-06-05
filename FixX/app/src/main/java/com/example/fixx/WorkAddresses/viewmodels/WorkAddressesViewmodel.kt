package com.example.fixx.WorkAddresses.viewmodels

import android.util.Log
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import com.google.firebase.messaging.FirebaseMessaging

class WorkAddressesViewmodel(val uid : String) {

    fun addNewWorkLocation(location : String, onSuccessBinding: ()->Unit, onFailBinding: ()->Unit){
        FirestoreService.updateWorkLocations(location, onSuccessBinding, onFailBinding)
    }

    fun removeWorkLocation(location: String, onSuccessBinding: () -> Unit, onFailBinding: () -> Unit){
        FirestoreService.removeWorkLocation(location, onSuccessBinding, onFailBinding)
    }

    fun subscribeToTopic(topic : String){
        FirebaseMessaging.getInstance()
            .subscribeToTopic("$topic").addOnSuccessListener {
                Log.i("TAG", "checkLogin: SUBSCRIPED <<<<<<<<<<<<<<<")
            }.addOnCompleteListener { task ->
                Log.i("TAG", "checkLogin: COMPLETE <<<<<<<<<<<<<<<")
                var msg = "SUCCESS !!"
                if (!task.isSuccessful) {
                    msg = "FAIL !!"
                }
                Log.d("TAG", msg)
                //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }.addOnCanceledListener {
                Log.i("TAG", "checkLogin: CANCELED <<<<<<<<<<<<<<<")
            }.addOnFailureListener {
                Log.i("TAG", "checkLogin: FALIURE <<<<<<<<<<<<<<< " + it.localizedMessage)
            }
    }

    fun unsubscribeFromTopic(topic : String){
        FirebaseMessaging.getInstance()
            .unsubscribeFromTopic("$topic").addOnSuccessListener {
                Log.i("TAG", "checkLogin: UNSUBSCRIPED <<<<<<<<<<<<<<<")
            }.addOnCompleteListener { task ->
                Log.i("TAG", "checkLogin: COMPLETE <<<<<<<<<<<<<<<")
                var msg = "SUCCESS !!"
                if (!task.isSuccessful) {
                    msg = "FAIL !!"
                }
                Log.d("TAG", msg)
                //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }.addOnCanceledListener {
                Log.i("TAG", "checkLogin: CANCELED <<<<<<<<<<<<<<<")
            }.addOnFailureListener {
                Log.i("TAG", "checkLogin: FALIURE <<<<<<<<<<<<<<< " + it.localizedMessage)
            }
    }

}