package com.example.fixx.Support

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.fixx.POJOs.*
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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.lang.reflect.Field

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


    fun registerUser(
        email: String,
        password: String,
        onSuccessHandler: () -> Unit,
        onFailHandler: () -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    onSuccessHandler()
                } else {
                    onFailHandler()
                }
            })
    }

    fun fetchUserFromDB(
        uid: String? = auth.currentUser?.uid,
        onCompletion: (user: Person?) -> Unit
    ) {
        var userData: Person? = null
        uid?.let {
            db.collection("Users").document(it).get().addOnSuccessListener { snapShot ->
                val type = snapShot.data?.get("accountType") as? String
                type?.let {
                    when (it) {
                        "User" -> onCompletion(snapShot.toObject<User>())
                        "Technician" -> onCompletion(snapShot.toObject<Technician>())
                    }
                }
            }
        }
    }

    fun fetchChatUsers(onCompletion: (chatUsers: List<String>?, chatChannels: ArrayList<String>) -> Unit) {
        var contactedUsersIds = ArrayList<String>()
        var chatChannels = ArrayList<String>()
        auth.currentUser?.uid?.let { uid ->
            db.collection("Chats").whereArrayContains("users", uid)
                .get().addOnSuccessListener { snapShot ->
                    snapShot.forEach { document ->
                        (document.data["users"] as? List<String>)?.find { it != uid }?.let {
                            contactedUsersIds.add(it)
                            chatChannels.add(document.reference.id)
                        }
                    }
                    onCompletion(contactedUsersIds, chatChannels)
                }
        }
    }
    fun fetchChatHistoryForChannel(
        channelName: String,
        observerHandler: (msg: ChatMessage) -> Unit,
        onCompletion: (chatMsgs: ArrayList<ChatMessage>) -> Unit
    ) {
        val msgs = ArrayList<ChatMessage>()
        var display = false
        db.collection("Chats").document(channelName)
            .collection("Messages")
            .orderBy("timestamp", Query.Direction.ASCENDING).apply {
                addSnapshotListener { value, error ->
                    error?.let {
                        Log.i("TAG", "fetchChatHistory: Error " + it.localizedMessage)
                    }
                    val msgs = value?.toObjects<ChatMessage>()
                    if (!msgs.isNullOrEmpty() && display) {
                        msgs[msgs.size -1].let {
                            Log.i("TAG", "fetchChatHistory: " + it)
                            observerHandler(it)
                        }
                    }
                    display = true
                }

                get().addOnSuccessListener { snapShot ->
                    snapShot.forEach { msgsSnapShot ->
                        Log.i("wezza", "fetchChatHistory: HERE 1 >> ")
                        val msg = msgsSnapShot.toObject<ChatMessage>()
                        msgs.add(msg)
                    }
                    Log.i("wezza", "fetchChatHistory: HERE 4 >> " + msgs.size)
                    onCompletion(msgs)
                }
            }
    }

    fun addBidder(uid : String, jobId : String, price: String, onCompletion:()->Unit){
        db.collection("Jobs").document(jobId)
            .update("bidders", mapOf(uid to price)).addOnSuccessListener {
                onCompletion()
            }
    }

    fun removeBidders(jobId : String){
        db.collection("Jobs").document(jobId).update("bidders",null)
    }

    fun selectTechForJob(techId : String, jobId : String, price : String){
        db.collection("Jobs").document(jobId)
            .update(mapOf("techID" to techId, "price" to price.toInt() , "bidders" to null , "status" to Job.JobStatus.Accepted.rawValue))
    }

    fun fetchChatHistoryForInstance(
        contact: String,
        observerHandler: (msg: ChatMessage) -> Unit,
        onCompletion: (chatMsgs: ArrayList<ChatMessage>, channelName: String) -> Unit
    ) {
        auth.currentUser?.uid?.let { uid ->
            db.collection("Chats").document("$uid-$contact").get().addOnSuccessListener {
                if (it.exists()) {
                    loadMessagesWithObserver(it, "$uid-$contact",observerHandler,onCompletion)
                } else {
                    db.collection("Chats").document("$contact-$uid").get()
                        .addOnSuccessListener { it2 ->
                            if (it2.exists()) {
                                loadMessagesWithObserver(it2,"$contact-$uid",observerHandler,onCompletion)
                            } else {
                                // create new document in Chats
                                db.collection("Chats")
                                    .document("$uid-$contact").apply {
                                        set(mapOf<String, List<String>>("users" to listOf(uid, contact)))
                                            .addOnSuccessListener {
                                                this.collection("Messages").addSnapshotListener { value, error ->
                                                    error?.let {
                                                        Log.i(
                                                            "TAG",
                                                            "fetchChatHistory: Error " + it.localizedMessage
                                                        )
                                                    }
                                                    val msgs = value?.toObjects<ChatMessage>()
                                                    if (!msgs.isNullOrEmpty()) {
                                                        msgs.last().let {
                                                            Log.i("TAG", "fetchChatHistory: " + it)
                                                            observerHandler(it)
                                                        }
                                                    }
                                                }
                                                onCompletion(arrayListOf(),"$uid-$contact")
                                            }
                                    }
                            }
                        }
                }
            }
        }
    }

    private fun loadMessagesWithObserver(document : DocumentSnapshot,channelName: String ,observerHandler: (msg: ChatMessage) -> Unit,
                                         onCompletion: (chatMsgs: ArrayList<ChatMessage>, channelName: String) -> Unit){
        val msgs = ArrayList<ChatMessage>()
        var display = false
        document.reference
            .collection("Messages")
            .orderBy("timestamp", Query.Direction.ASCENDING).apply {
                addSnapshotListener { value, error ->
                    error?.let {
                        Log.i("TAG", "fetchChatHistory: Error " + it.localizedMessage)
                    }
                    val msgs = value?.toObjects<ChatMessage>()
                    if (!msgs.isNullOrEmpty() && display) {
                        msgs?.last()?.let {
                            Log.i("TAG", "fetchChatHistory: " + it)
                            observerHandler(it)
                        }
                    }
                    display = true
                }
                get().addOnSuccessListener { snapShot ->
                    snapShot.forEach { msgsSnapShot ->
                        Log.i("wezza", "fetchChatHistory: HERE 1 >> ")
                        val msg = msgsSnapShot.toObject<ChatMessage>()
                        msgs.add(msg)
                    }
                    Log.i("wezza", "fetchChatHistory: HERE 4 >> " + msgs.size)
                    onCompletion(msgs, channelName)
                }
            }
    }

    fun sendChatMessage(msg: ChatMessage, toChannel: String) {
        val map = HashMap<String, Any?>()
        msg::class.memberProperties.forEach {
            map[it.name] = (it as KProperty1<Any, Any>).get(msg)
            Log.i("TAG", "save: inside for each  ${it.name} = ${it.get(msg)}")
        }
        db.collection("Chats").document(toChannel).collection("Messages").add(map)
            .addOnSuccessListener { Log.i("TAG", "Message successfully written!") }
            .addOnFailureListener { e -> Log.i("TAG", "Error writing document", e) }
    }

    fun fetchJobById(jobId : String, onSuccessHandler: (job : Job) -> Unit, onFailHandler: () -> Unit){
        db.collection("Jobs").document(jobId).get()
            .addOnSuccessListener { snapShot ->
            val job = snapShot.toObject<Job>()
                job?.let {
                    onSuccessHandler(it)
                }
        }.addOnFailureListener {
            onFailHandler()
        }
    }

    fun loginWithEmailAndPassword(
        email: String,
        password: String,
        onSuccessHandler: (person : Person?) -> Unit,
        onFailHandler: () -> Unit
    ) {
        Log.i("TAG", "loginWithEmailAndPassword: Received >>>$email<< >>$password<<")
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful && email != Constants.DEFAULT_EMAIL) {
                    fetchUserFromDB(auth.currentUser?.uid,onSuccessHandler)
                } else {
                    Log.i("TAG", "login: error!!!!")
                    onFailHandler()
                }
            })
    }


    fun saveJobDetails(job: Job, onSuccessHandler: (jobs: Job) -> Unit, onFailHandler: () -> Unit) {
        val map = HashMap<String, Any?>()
        job::class.memberProperties.forEach {
            map[it.name] = (it as KProperty1<Any, Any>).get(job)
            Log.i("TAG", "save: inside for each  ${it.name} = ${it.get(job)}")

        }
        db.collection("Jobs").add(map)
            .addOnSuccessListener {
                it.update("jobId",it.id)
                job.jobId = it.id
                onSuccessHandler(job)
                Log.i("TAG", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.i("TAG", "Error writing document", e); onFailHandler() }
    }

    

    fun saveUserData(user: Any, onSuccessHandler: (person: Person?) -> Unit, onFailHandler: () -> Unit) {
        (user as? Person)?.let {    person ->
            person.uid = FirebaseAuth.getInstance().uid.toString()
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener {  token ->
                    person.token = token

                    val map = HashMap<String, Any?>()

                    user::class.memberProperties.forEach {
                        map[it.name] = (it as KProperty1<Any, Any>).get(user)
                        Log.i("TAG", "save: inside for each  ${it.name} = ${it.get(user)}")
                    }

                    db.collection("Users").document(FirebaseAuth.getInstance().uid.toString())
                        .set(map)
                        .addOnSuccessListener { onSuccessHandler(person) }
                        .addOnFailureListener { e -> Log.i("TAG", "Error writing document", e); onFailHandler() }
                }
        }
    }

    private fun configGoogleSignIn(context: Context) {
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

    fun googleSignInRequestResult(
        requestCode: Int,
        data: Intent?,
        onSuccessHandler: (email: String) -> Unit,
        onFailHandler: () -> Unit
    ) {

        if (requestCode == Constants.RC_SIGN_IN) {
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

    private fun firebaseAuthWithGoogle(
        idToken: String,
        onSuccessHandler: (email: String) -> Unit,
        onFailHandler: () -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    val userEmail = auth.currentUser?.email
                    //Log.d("TAG", "signInWithCredential:success")
                    onSuccessHandler(userEmail!!)
                } else {
                    //Log.w("TAG", "signInWithCredential:failure", task.exception)
                    onFailHandler()
                }
            })
    }

    fun fetchMyOngoingOrderedJobs(onSuccessHandler : (jobs : List<Job>)-> Unit, onFailureHandler : ()->Unit){
        val retrievedJobs = ArrayList<Job>()
        db.collection("Jobs").whereEqualTo("uid",auth.currentUser?.uid)
            .whereIn("status", arrayListOf(Job.JobStatus.OnRequest.rawValue, Job.JobStatus.Accepted.rawValue))
            .get().addOnSuccessListener {
                queryResult->
                queryResult.forEach {   document ->
                    val job = document.toObject<Job>()
                    Log.i("TAG", "fetchMyOngoingOrderedJobs: >>>> $job")
                    retrievedJobs.add(job)
                }
                onSuccessHandler(retrievedJobs)
            }.addOnFailureListener {
                onFailureHandler()
            }
    }

    fun fetchMyCompletedOrderedJobs(onSuccessHandler : (jobs : List<Job>)-> Unit, onFailureHandler : ()->Unit){
        val retrievedJobs = ArrayList<Job>()
        db.collection("Jobs").whereEqualTo("uid",auth.currentUser?.uid)
            .whereEqualTo("status",Job.JobStatus.Completed.rawValue)
            .get().addOnSuccessListener {
                    queryResult->
                queryResult.forEach {   document ->
                    val job = document.toObject<Job>()
                    Log.i("TAG", "fetchMyOngoingOrderedJobs: >>>> $job")
                    retrievedJobs.add(job)
                }
                onSuccessHandler(retrievedJobs)
            }.addOnFailureListener {
                onFailureHandler()
            }
    }

    fun fetchMyOngoingWork(onSuccessHandler : (jobs : List<Job>)-> Unit, onFailureHandler : ()->Unit){
        val retrievedJobs = ArrayList<Job>()
        db.collection("Jobs").whereEqualTo("techID",auth.currentUser?.uid)
            .whereEqualTo("status",Job.JobStatus.Accepted.rawValue)
            .get().addOnSuccessListener {
                    queryResult->
                queryResult.forEach {   document ->
                    val job = document.toObject<Job>()
                    Log.i("TAG", "fetchMyOngoingOrderedJobs: >>>> $job")
                    retrievedJobs.add(job)
                }
                onSuccessHandler(retrievedJobs)
            }.addOnFailureListener {
                onFailureHandler()
            }
    }

    fun fetchMyCompletedWork(onSuccessHandler : (jobs : List<Job>)-> Unit, onFailureHandler : ()->Unit){
        val retrievedJobs = ArrayList<Job>()
        db.collection("Jobs").whereEqualTo("techID",auth.currentUser?.uid)
            .whereEqualTo("status",Job.JobStatus.Completed.rawValue)
            .get().addOnSuccessListener {
                    queryResult->
                queryResult.forEach {   document ->
                    val job = document.toObject<Job>()
                    Log.i("TAG", "fetchMyOngoingOrderedJobs: >>>> $job")
                    retrievedJobs.add(job)
                }
                onSuccessHandler(retrievedJobs)
            }.addOnFailureListener {
                onFailureHandler()
            }
    }

    fun fetchAvailableWork(jobTitle : String, workLocations : ArrayList<String>,
                           onSuccessHandler : (jobs : List<Job>)-> Unit, onFailureHandler : ()->Unit){
        val retrievedJobs = ArrayList<Job>()
        db.collection("Jobs").whereEqualTo("status",Job.JobStatus.OnRequest.rawValue)
            .whereEqualTo("type",jobTitle)
            .whereIn("location",workLocations)
            .get().addOnSuccessListener {
                    queryResult->
                queryResult.forEach {   document ->
                    val job = document.toObject<Job>()
                    Log.i("TAG", "fetchMyOngoingOrderedJobs: >>>> $job")
                    retrievedJobs.add(job)
                }
                onSuccessHandler(retrievedJobs)
            }.addOnFailureListener {
                onFailureHandler()
            }
    }

    fun checkIfPhoneExists(phone: String, callback: (Boolean) -> Unit) {
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

    fun checkIfEmailExists(email: String, callback: (Boolean) -> Unit) {
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

    fun searchForTechnicianByJobAndLocation(
        job: String,
        location: String,
        callback: (MutableList<Technician>) -> Unit
    ) {

        var techniciansList = mutableListOf<Technician>()

        val docRef = db.collection("Users").whereEqualTo("accountType", "Technician")
            .whereEqualTo("jobTitle", job).whereArrayContains("workLocations",
                location.substringAfter("%").substringBefore("/"))
        docRef.get().addOnSuccessListener { documentSnapshot ->
            for (document in documentSnapshot) {

                var t = document.toObject<Technician>()
                if(t.uid != auth.uid){
                    techniciansList.add(t)
                }
            }
            callback(techniciansList)
            Log.i("TAG1", "searchForTechnicianByJobAndLocation: ALL TECHNICIANS $techniciansList")
        }
    }

    fun uploadImageToStorage(
        filePath: MutableList<Uri>,
        onSuccessHandler: (MutableList<StringPair>) -> Unit
    ) {
        var imagesPathsList = mutableListOf<StringPair>()
        if (filePath.isNotEmpty()) {
            for (image in filePath) {
                var path = "Images/" + UUID.randomUUID().toString()
                val imageRef = storageRef.child(path)
                imageRef.putFile(image)
                    .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                        return@Continuation imageRef.downloadUrl
                    })
                    .addOnCompleteListener {
                        var uri = it.result
                        Log.i("TAG", "uploadJobImage: ${it.result.toString()}")
                        if (uri != null) {
                            imagesPathsList.add(StringPair(path,uri.toString()))
                            onSuccessHandler(imagesPathsList)
                        }
                    }
            }

        }
    }

    fun deleteImage(path : String){
        storageRef.child(path).delete()
    }

    fun updateDocumentField(
        collectionName: String,
        fieldName: String,
        element: Any,
        documentId: String,
        onSuccessHandler: () -> Unit = {},
        onFailHandler: () -> Unit = {}
    ) {
        Log.i("TAG", "updateJob: start updating $element")
        db.collection(collectionName).document(documentId)
            .update(fieldName, element)
            .addOnSuccessListener { onSuccessHandler() }
            .addOnFailureListener { e -> Log.i("TAG", "update: error $e"); onFailHandler() }
    }

    fun updateUserLocations(loc : String, onSuccessHandler: () -> Unit,onFailHandler: () -> Unit){
        db.collection("Users").document(auth.uid ?: "")
            .update("locations", FieldValue.arrayUnion(loc))
            .addOnSuccessListener {
                onSuccessHandler()
            }.addOnFailureListener {
                onFailHandler()
            }
    }

    fun updateDocument(collectionName: String, map: Map<String, Any>, documentId: String) {
        Log.i("TAG", "updateJob: start updating $map")
        db.collection(collectionName).document(documentId)
            .update(map)
            .addOnSuccessListener { Log.i("TAG", "update: DocumentSnapshot updated successfully") }
            .addOnFailureListener { e -> Log.i("TAG", "update: error $e") }
    }
}