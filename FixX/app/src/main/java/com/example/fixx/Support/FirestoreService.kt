package com.example.fixx.Support

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.fixx.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import eg.gov.iti.jets.fixawy.POJOs.Technician
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

object FirestoreService {
    var db = FirebaseFirestore.getInstance()
    var auth = Firebase.auth
    lateinit var googleSignInClient: GoogleSignInClient
    const val RC_SIGN_IN = 9001


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


    private fun configGoogleSignIn(context: Context){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun signInWithGoogle(context: Context) {
        configGoogleSignIn(context)
        val signInIntent = googleSignInClient.signInIntent
        (context as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun googleSignInRequestResult(requestCode : Int, data : Intent?){

        if (requestCode == RC_SIGN_IN ) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                Log.i("TAG", "googleSignInRequestResult: before account ")
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Log.d("TAG", "signInWithCredential:success")
                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.exception)
                    }
                })
    }

    fun checkIfPhoneExists(phone : String, callback : (Boolean) -> Unit) {
        db.collection("Users").whereEqualTo("phoneNumber", phone).get()
                .addOnCompleteListener(OnCompleteListener {
                    if (it.isSuccessful) {
                        val document = it.result
                        if (document?.size()!! > 0) {
                            Log.i("TAG", " document data: ${document}")
                            callback(true)
                        } else {
                            Log.i("TAG", "checkIfPhoneExists: nothing")
                            callback(false)
                        }
                    } else {
                        Log.i("TAG", " get failed: ", it.exception)
                    }
                })
    }

    fun checkIfEmailExists(email : String, callback : (Boolean) -> Unit) {
        db.collection("Users").whereEqualTo("email", email).get()
                .addOnCompleteListener(OnCompleteListener {
                    if (it.isSuccessful) {
                        val document = it.result
                        if (document?.size()!! > 0) {
                            Log.i("TAG", " document data: ${document}")
                            callback(true)
                        } else {
                            Log.i("TAG", "checkIfPhoneExists: nothing")
                            callback(false)
                        }
                    } else {
                        Log.i("TAG", " get failed: ", it.exception)
                    }
                })
    }

    fun searchForTechnicianByJobAndLocation(job: String, location: String, callback: (MutableList<Technician>) -> Unit) {

        var techniciansList = mutableListOf<Technician>()

        val docRef = db.collection("Users").whereEqualTo("accountType", "technician").whereEqualTo("jobTitle", job).whereArrayContains("workLocations",location)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            for (document in documentSnapshot){

                var t = document.toObject<Technician>()
                techniciansList.add(t)
                t.id = document.id

                Log.i("TAG1", "searchForTechnicianByJobAndLocation: TECH:: ${t.id}")
            }
            callback(techniciansList)
            Log.i("TAG1", "searchForTechnicianByJobAndLocation: ALL TECHNICIANS $techniciansList")
        }
    }

}