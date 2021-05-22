package com.example.fixx.Support

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.fixx.POJOs.*
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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

object FirestoreService {
    var db = FirebaseFirestore.getInstance()
    var auth = Firebase.auth
    lateinit var googleSignInClient: GoogleSignInClient
    const val RC_SIGN_IN = 9001

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

    fun fetchUserFromDB(uid : String? = auth.currentUser?.uid ,onCompletion : (user : Person?)->Unit) {
        var userData : Person? = null
        uid?.let {
            db.collection("Users").document(it).get().addOnSuccessListener {
                snapShot ->
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

    fun fetchChatUsers(onCompletion: (chatUsers : List<String>?) -> Unit){
        var contactedUsersIds  =  ArrayList<String>()
        auth.currentUser?.uid?.let {
            uid ->
            db.collection("Chats").whereArrayContains("users","7uru4sSpz8brZ30aH52JCLaBU733")
                .get().addOnSuccessListener {
                snapShot ->
                snapShot.forEach {
                    document ->
                    (document.data["users"] as? List<String>)?.find { it != "7uru4sSpz8brZ30aH52JCLaBU733" }?.let {
                        contactedUsersIds.add(it)
                    }
                }
                    onCompletion(contactedUsersIds)
            }
        }
    }

    fun fetchChatHistory(contact : String ,observerHandler: (msg : ChatMessage)-> Unit
                         , onCompletion: (chatUsers: ArrayList<ChatMessage> , channelName : String) -> Unit){
        val msgs = ArrayList<ChatMessage>()
        var display = false
        auth.currentUser?.uid?.let {
            uid ->
            db.collection("Chats").document("$uid-$contact").get().addOnSuccessListener {
                if(it.exists()){
                    it.reference
                        .collection("Messages")
                        .orderBy("timestamp",Query.Direction.ASCENDING)
                        .get()
                        .addOnSuccessListener {
                            snapShot ->
                            snapShot.forEach {
                                msgsSnapShot ->
                                Log.i("wezza", "fetchChatHistory: HERE 1 >> ")
                                val msg = msgsSnapShot.toObject<ChatMessage>()
                                msgs.add(msg)
                            }
                            Log.i("wezza", "fetchChatHistory: HERE 4 >> " + msgs.size)
                            onCompletion(msgs , "$uid-$contact")
                        }

                    it.reference.collection("Messages")
                        .orderBy("timestamp",Query.Direction.ASCENDING)
                        .addSnapshotListener { value, error ->
                        error?.let{
                            Log.i("TAG", "fetchChatHistory: Error " + it.localizedMessage)
                        }
                        val msgs = value?.toObjects<ChatMessage>()
                        if(!msgs.isNullOrEmpty() && display){
                            msgs?.last()?.let {
                                Log.i("TAG", "fetchChatHistory: "+ it)
                                observerHandler(it)
                            }
                        }
                            display = true
                    }
                }else{
                    db.collection("Chats").document("$contact-$uid").get().addOnSuccessListener {
                        it2->
                        if(it2.exists()){
                            it2.reference
                                .collection("Messages")
                                .get()
                                .addOnSuccessListener {
                                        snapShot ->
                                    snapShot.forEach {
                                            msgsSnapShot ->
                                        (msgsSnapShot.data["msg"] as? ChatMessage)?.let{
                                                msg ->
                                            msgs.add(msg)
                                        }
                                    }
                                    onCompletion(msgs, "$contact-$uid")
                                }
                            it.reference.collection("Messages")
                                .orderBy("timestamp",Query.Direction.ASCENDING)
                                .addSnapshotListener { value, error ->
                                    error?.let{
                                        Log.i("TAG", "fetchChatHistory: Error " + it.localizedMessage)
                                    }
                                    val msgs = value?.toObjects<ChatMessage>()
                                    if(!msgs.isNullOrEmpty() && display){
                                        msgs?.last()?.let {
                                            Log.i("TAG", "fetchChatHistory: "+ it)
                                            observerHandler(it)
                                        }
                                    }
                                    display = true
                                }
                        }else{
                            // create new document in Chats
                            db.collection("Chats")
                                .document("$uid-$contact")
                                .set(mapOf<String,List<String>>("users" to listOf(uid,contact)))
                                .addOnSuccessListener {
                                    Log.i("TAG", "fetchChatHistory: Chat Channel added")
                                    onCompletion(msgs, "$uid-$contact")
                                }
                        }
                    }
                }
            }
        }
    }

    fun sendChatMessage(msg : ChatMessage , toChannel : String){
        val map = HashMap<String, Any?>()
        msg::class.memberProperties.forEach {
            map[it.name] = (it as KProperty1<Any, Any>).get(msg)
            Log.i("TAG", "save: inside for each  ${it.name} = ${it.get(msg)}")
        }
        db.collection("Chats").document(toChannel).collection("Messages").add(map)
            .addOnSuccessListener { Log.i("TAG", "Message successfully written!") }
            .addOnFailureListener { e -> Log.i("TAG", "Error writing document", e) }
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

    fun saveJobDetails(job: Job) {
        val map = HashMap<String, Any?>()
        job::class.memberProperties.forEach {
            map[it.name] = (it as KProperty1<Any, Any>).get(job)
            Log.i("TAG", "save: inside for each  ${it.name} = ${it.get(job)}")

        }
        db.collection("Jobs").add(map)
            .addOnSuccessListener { Log.i("TAG", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.i("TAG", "Error writing document", e) }
    }

    fun saveUserData(user: Any) {
        (user as? Person)?.let {
            it.uid = FirebaseAuth.getInstance().uid.toString()
        }
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
            }
            callback(techniciansList)
            Log.i("TAG1", "searchForTechnicianByJobAndLocation: ALL TECHNICIANS $techniciansList")
        }
    }

}