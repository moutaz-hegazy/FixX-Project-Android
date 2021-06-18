package com.example.fixx.LoginScreen.viewmodels

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

class RegisterViewmodel() {
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
}