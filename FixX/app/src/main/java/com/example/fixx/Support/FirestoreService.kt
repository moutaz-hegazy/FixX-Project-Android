package com.example.fixx.Support

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.Person
import com.example.fixx.POJOs.Technician
import com.example.fixx.POJOs.User
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

object FirestoreService {
    var db = FirebaseFirestore.getInstance()
    var auth = Firebase.auth
    var storage = Firebase.storage
    var storageRef = storage.reference
    var imagesRef: StorageReference? = null

    lateinit var googleSignInClient: GoogleSignInClient


    fun registerUser(email : String, password : String, onSuccessHandler: () -> Unit, onFailHandler: () -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful){
                    onSuccessHandler()
                }
                else{
                    onFailHandler()
                }
            })
    }

    fun fetchUserFromDB(onCompletion : (user : Person?)->Unit) {
        var userData : Person? = null
        Log.i("wezza", "fetchUserFromDB: here 1")
        auth.currentUser?.uid?.let {
            db.collection("Users").document(it).get().addOnSuccessListener {
                    snapShot ->
                Log.i("wezza", "fetchUserFromDB: here 2")
                val type = snapShot.data?.get("accountType") as? String
                type?.let {
                    when(it){
                        "User" -> onCompletion(snapShot.toObject<User>())
                        "Technician" -> onCompletion(snapShot.toObject<Technician>())
                    }
                }
            }
        }
    }

    fun loginWithEmailAndPassword(email: String, password: String, onSuccessHandler : ()->Unit, onFailHandler : ()->Unit){
        Log.i("TAG", "loginWithEmailAndPassword: Received >>>$email<< >>$password<<")
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult>{ task ->
                if(task.isSuccessful){
//                        Log.i("TAG", "login: successfully"+ auth.uid)
                    onSuccessHandler()
                }
                else{
                    Log.i("TAG", "login: error!!!!")
                    onFailHandler()
                }
            })
    }

    fun saveJobDetails(job: Job, onSuccessHandler: (String) -> Unit) {
        val map = HashMap<String, Any?>()
        job::class.memberProperties.forEach {
            map[it.name] = (it as KProperty1<Any, Any>).get(job)
            Log.i("TAG", "save: inside for each  ${it.name} = ${it.get(job)}")

        }
        db.collection("Jobs").add(map)
            .addOnSuccessListener {
                Log.i("TAG", "DocumentSnapshot successfully written!")
                //job.jobId = it.id
                onSuccessHandler(it.id)
            }
            .addOnFailureListener { e -> Log.i("TAG", "Error writing document", e) }

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
        (context as Activity).startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
    }

    fun googleSignInRequestResult(requestCode : Int, data : Intent?, onSuccessHandler: (email : String) -> Unit, onFailHandler: () -> Unit){

        if (requestCode == Constants.RC_SIGN_IN ) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                Log.i("TAG", "googleSignInRequestResult: before account ")
                val account = task.getResult(ApiException::class.java)!!
                Log.i("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!, onSuccessHandler, onFailHandler)
            } catch (e: ApiException) {
                Log.i("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, onSuccessHandler: (email : String) -> Unit, onFailHandler: () -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    val userEmail = auth?.currentUser?.email
                    //Log.d("TAG", "signInWithCredential:success")
                    onSuccessHandler(userEmail!!)
                } else {
                    //Log.w("TAG", "signInWithCredential:failure", task.exception)
                    onFailHandler()
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

        val docRef = db.collection("Users").whereEqualTo("accountType", "Technician").whereEqualTo("jobTitle", job).whereArrayContains("workLocations",location)
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

    fun uploadImage(filePath : MutableList<Uri>, jobId : String, onSuccessHandler: (MutableList<Uri>) -> Unit) {
        var imagesPathsList = mutableListOf<Uri>()
        if (filePath.isNotEmpty()) {
            for (image in filePath) {
                var path = "Images/" + UUID.randomUUID().toString()
                val imageRef = storageRef!!.child(path)
                val uploadTask = imageRef.putFile(image)
                    .addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener {
                            imagesPathsList?.add(it)
                            Log.i("TAG", "SIZE!!!!!! ${imagesPathsList?.size}")
                            imagesPathsList?.let { it1 -> onSuccessHandler(it1) }
                        }
                        Log.i("TAG", "uploadImage: image uploaded and the url is ${it}")
                        //updateJob("images", path, jobId)

                    }
                    .addOnFailureListener {
                        Log.i("TAG", "uploadImage: failure!!!!!!")
                    }

                Log.i("TAG", "uploadImage: HELLLOOOOOOOOOOOOOOOOO ${uploadTask.snapshot.storage.downloadUrl} ")

            }

        }
    }



    fun uploadJobImage(filePath : MutableList<Uri>, onSuccessHandler: (MutableList<String>) -> Unit) {
        var imagesPathsList = mutableListOf<String>()
        if (filePath.isNotEmpty()) {
            for (image in filePath) {
                var path = "Images/" + UUID.randomUUID().toString()
                val imageRef = storageRef!!.child(path)
                imageRef.putFile(image)
                    .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{
                        return@Continuation imageRef.downloadUrl
                    })
                    .addOnCompleteListener{
                        var uri = it.result
                        Log.i("TAG", "uploadJobImage: ${it.result.toString()}")
                        if (uri != null){
                            imagesPathsList.add(uri.toString())
                            onSuccessHandler(imagesPathsList)
                        }
                    }
            }

        }
    }

     fun updateJob(fieldName : String, element: Any, jobId: String) {
        Log.i("TAG", "updateJob: start updating $element")
        db.collection("Jobs").document(jobId)
            .update(fieldName, element)
            .addOnSuccessListener { Log.i("TAG", "update: DocumentSnapshot updated successfully") }
            .addOnFailureListener { e -> Log.i("TAG", "update: error $e") }
    }

}