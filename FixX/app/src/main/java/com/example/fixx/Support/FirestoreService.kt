package com.example.fixx.Support

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

object FirestoreService {
    var db = FirebaseFirestore.getInstance()
    var auth = Firebase.auth


    fun registerUser(email : String, password : String){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful){
                        val user = auth.currentUser
                        Log.i("TAG", "register: successfully"+ user.uid)
                    }
                    else{
                        Log.i("TAG", "register: error!!!!!!")
                    }
                })
    }


    fun loginWithEmailAndPassword(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener<AuthResult>{ task ->
                    if(task.isSuccessful){
                        Log.i("TAG", "login: successfully"+ auth.uid)
                    }
                    else{
                        Log.i("TAG", "login: error!!!!")
                    }
                })
    }


    fun saveUserData(user: Any) {

        val map = HashMap<String, Any?>()

        user::class.memberProperties.forEach {
            map[it.name] = (it as KProperty1<Any, Any>).get(user)
            Log.i("TAG", "save: inside for each  ${it.name} = ${it.get(user)}")
        }

        db.collection("Users").document(FirebaseAuth.getInstance().uid.toString())
                .set(map)
                .addOnSuccessListener { Log.i("TAG", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.i("TAG", "Error writing document", e) }
    }

}