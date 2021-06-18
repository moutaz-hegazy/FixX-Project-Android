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
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.*
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
    private var db = FirebaseFirestore.getInstance()
    var auth = Firebase.auth
    private var storage = Firebase.storage
    private var storageRef = storage.reference

    val database = Firebase.database
    val contactsRef = database.getReference("Contacts")
    val chatsRef = database.getReference("Chats")

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

//    fun fetchUserFromDB(
//        uid: String? = auth.currentUser?.uid,
//        onCompletion: (user: Person?) -> Unit,
//        passRegister : (reg : ListenerRegistration) -> Unit
//    ) {
//        uid?.let {
//            db.collection("Users").document(it).apply {
//                addSnapshotListener {
//                        value, error ->
//                    val type = value?.data?.get("accountType") as? String
//                    type?.let {
//                        when (it) {
//                            "User" -> onCompletion(value.toObject<User>())
//                            "Technician" -> onCompletion(value.toObject<Technician>())
//                        }
//                    }
//                }.also {
//                    passRegister(it)
//                }
//            }
//        }
//    }

    fun fetchUserOnce(
        uid: String? = auth.currentUser?.uid,
        onCompletion: (user: Person?) -> Unit
    ) {
        uid?.let {
            db.collection("Users").document(it).get().addOnSuccessListener { snapShot ->
                Log.i("TAG", "fetchUserFromDB: FETCH SUCCESS <<<<<<<<<<<")
                val type = snapShot.data?.get("accountType") as? String
                type?.let {
                    when (it) {
                        "User" -> onCompletion(snapShot.toObject<User>())
                        "Technician" -> onCompletion(snapShot.toObject<Technician>())
                    }
                }
            }.addOnFailureListener {
                Log.i("TAG", "fetchUserFromDB: <<<<<<<< FAILED ????")
            }
        }
    }

    fun fetchCommentsForTech(techId : String = auth.uid!!,onSuccessHandler: (comments : ArrayList<Comment>) -> Unit,
                             onFailHandler: () -> Unit){
        val comments = arrayListOf<Comment>()
        db.collection("Users").document(techId).collection("Comments")
            .orderBy("comment.timestamp",Query.Direction.DESCENDING)
            .get().addOnSuccessListener { query ->
                query.forEach { document ->
                    val commentData = document.toObject<CommentData>()
                    comments.add(commentData.comment!!)
                }
                onSuccessHandler(comments)
            }.addOnFailureListener {
                onFailHandler()
            }
    }

    fun addExtensionToJob(jobId : String, ext : Extension, onSuccessHandler: (ext:Extension) -> Unit, onFailHandler: () -> Unit){
        db.collection("Jobs").document(jobId).collection("Extensions").add(ext)
            .addOnSuccessListener {
                it.update("extId" , it.id)
                ext.extId = it.id
                onSuccessHandler(ext)
            }.addOnFailureListener {
                onFailHandler()
            }
    }

    fun updateExtensionPrice(jobId: String,extId:String,price:Int,onSuccessHandler: () -> Unit,onFailHandler: () -> Unit){
        db.collection("Jobs").document(jobId).collection("Extensions")
            .document(extId).update("price" , price).addOnSuccessListener {
                onSuccessHandler()
            }.addOnFailureListener {
                onFailHandler()
            }
    }

    fun removeExtension(jobId: String,extId:String,onSuccessHandler: () -> Unit,onFailHandler: () -> Unit){
        db.collection("Jobs").document(jobId).collection("Extensions")
            .document(extId).delete().addOnSuccessListener {
                onSuccessHandler()
            }.addOnFailureListener {
                onFailHandler()
            }
    }

    fun fetchExtensionsForJob(jobId : String, onSuccessHandler: (exts : List<Extension>) -> Unit, onFailHandler: () -> Unit){
        val exts = mutableListOf<Extension>()
        db.collection("Jobs").document(jobId).collection("Extensions")
            .get().addOnSuccessListener {   query ->
                query.forEach { doc ->
                    val ext = doc.toObject<Extension>()
                    exts.add(ext)
                }
                onSuccessHandler(exts)
            }.addOnFailureListener {
                onFailHandler()
            }
    }

    fun fetchChatUsersTest(onCompletion: (contacts : List<ContactInfo>) -> Unit){
        val contacts = mutableListOf<ContactInfo>()
        contactsRef.child(auth.uid!!).get().addOnSuccessListener {
            if(it.exists()){
                it.children.forEach { contactSnapShot ->
                    val contact = contactSnapShot.getValue(ContactInfo::class.java)
                    contact?.let {  info->
                        contacts.add(info)
                    }
                }
            }
            onCompletion(contacts)
        }
    }

    fun fetchChatHistoryForChannelTest(
        channelName: String,
        observerHandler: (msg: ChatMessage) -> Unit,
        chatRegHandler : (reg : ChildEventListener,ref:DatabaseReference) ->Unit
        ){
        val ref : DatabaseReference
        chatsRef.child(channelName).apply {
            ref = this
            addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.i("TAG", "onChildAdded: >>>>>>>>>> ${snapshot.value}")
                    snapshot.getValue(ChatMessage::class.java)?.let {
                        observerHandler(it)
                    }
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            }).also {
                chatRegHandler(it,ref)
            }
        }
    }

    fun fetchChatHistoryForInstanceTest(
        contact: String,
        observerHandler: (msg: ChatMessage) -> Unit,
        onCompletion: (channelName: String) -> Unit,
        chatRegHandler : (reg : ChildEventListener,ref:DatabaseReference) ->Unit
    ){
        chatsRef.child("${auth.uid}-$contact").get().addOnSuccessListener {
            if(it.exists()){
                onCompletion("${auth.uid}-$contact")
                fetchChatHistoryForChannelTest("${auth.uid}-$contact",observerHandler,chatRegHandler)
            }else{
                chatsRef.child("$contact-${auth.uid}").get().addOnSuccessListener {
                    if(it.exists()) {
                        onCompletion("$contact-${auth.uid}")
                        fetchChatHistoryForChannelTest(
                            "$contact-${auth.uid}",
                            observerHandler,chatRegHandler)
                    }else{
                        createChatChannel("${auth.uid}-$contact",contact,
                            ChatMessage("", auth.uid!!,System.currentTimeMillis()),observerHandler,chatRegHandler)
                        onCompletion("${auth.uid}-$contact")
                    }
                }
            }
        }
    }

    private fun createChatChannel(channelName : String, contact : String, msg : ChatMessage,
                                  observerHandler: (msg: ChatMessage) -> Unit,
                                  chatRegHandler : (reg : ChildEventListener,ref:DatabaseReference) ->Unit){

        chatsRef.child(channelName).apply {
            child(this.push().key!!).setValue(msg).addOnSuccessListener {
                contactsRef.child(auth.uid!!).child(contact)
                    .setValue(ContactInfo(contact,channelName))
            }

            val reg = addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.i("TAG", "onChildAdded: >>>>>>>>>> ${snapshot.value}")
                    snapshot.getValue(ChatMessage::class.java)?.let {
                        observerHandler(it)
                    }
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
            chatRegHandler(reg,this)
        }
    }

    fun addNewChatContatct(contactInfo : ContactInfo){
        contactsRef.child(auth.uid!!).child(contactInfo.uid!!).get().addOnSuccessListener {
            if(!it.exists()){
                it.ref.setValue(contactInfo)
            }
        }
    }

    fun sendChatMessageTest(channel : String, msg : ChatMessage){
        chatsRef.child(channel).child(chatsRef.push().key!!).setValue(msg)
    }

    fun addRatingAndComment(techId:String, rating : Double, extraRating: Double, comment: Comment,reviews : Int,
                            onSuccessHandler: () -> Unit, onFailHandler: () -> Unit) {
        db.collection("Users").document(techId).apply {
            update(mapOf("rating" to rating, "monthlyRating" to extraRating,"reviewCount" to reviews))

            collection("Comments").document(auth.currentUser?.uid!!).get()
                .addOnSuccessListener { snap ->
                    if (snap.exists()) {
                        snap.reference.update("comment", comment).addOnSuccessListener {
                            onSuccessHandler()
                        }.addOnFailureListener {
                            onFailHandler()
                        }
                    } else {
                        snap.reference.set(mapOf("comment" to comment)).addOnSuccessListener {
                            onSuccessHandler()
                        }.addOnFailureListener {
                            onFailHandler()

                        }
                    }
                }
        }
    }

    fun addBidder(jobId : String, bidders:Map<String,String>, onCompletion:()->Unit){
        db.collection("Jobs").document(jobId)
            .update("bidders", bidders).addOnSuccessListener {
                onCompletion()
            }
    }

    fun removeBidders(jobId : String){
        db.collection("Jobs").document(jobId).update("bidders",null)
    }

    fun removeTechnicianFromJob(jobId : String){
        db.collection("Jobs").document(jobId).update(mapOf("techID" to null, "status" to Job.JobStatus.OnRequest,
            "price" to null))
    }

    fun selectTechForJob(techId : String, jobId : String, price : String){
        db.collection("Jobs").document(jobId)
            .update(mapOf("techID" to techId, "price" to price.toInt() , "bidders" to null , "status" to Job.JobStatus.Accepted.rawValue))
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
                    fetchUserOnce{
                        onSuccessHandler(it)
                    }
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

    fun removeLocation(location: String){
        Log.i("TAG", "removeLocation: >>>>>>>>>>>>>> here!!")
        db.collection("Users").document(auth.currentUser?.uid!!)
            .update("locations", FieldValue.arrayRemove(location))
            .addOnSuccessListener {
                Log.i("TAG", "removeLocation: DELETED <<<<<<<<<<<")
            }.addOnFailureListener {
                Log.i("TAG", "removeLocation: DELETE FAILED <<<<<<<<<<<")
            }
    }

    fun removeWorkLocation(location: String, onSuccessHandler: () -> Unit,onFailHandler: () -> Unit){
        db.collection("Users").document(auth.currentUser?.uid!!)
            .update("workLocations", FieldValue.arrayRemove(location))
            .addOnSuccessListener {
                onSuccessHandler()
            }.addOnFailureListener {
                onFailHandler()
            }
    }

    fun removeJob(jobId : String, onSuccessHandler: () -> Unit, onFailHandler: () -> Unit){
        db.collection("Jobs").document(jobId).delete().addOnSuccessListener {
            onSuccessHandler()
        }.addOnFailureListener {
            onFailHandler()
        }
    }

    fun addReplyToComment(userId:String, reply : String, onSuccessHandler: () -> Unit){
        db.collection("Users").document(auth.uid!!).collection("Comments")
            .document(userId).update("comment.reply", reply).addOnSuccessListener {
                onSuccessHandler()
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
                    retrievedJobs.add(job)
                }
                db.collection("Jobs").whereEqualTo("privateRequest", true )
                    .whereEqualTo("privateTechUid",auth.uid)
                    .whereEqualTo("status",Job.JobStatus.OnRequest.rawValue)
                    .get().addOnSuccessListener { result ->
                        Log.i("TAG", "fetchMyOngoingWork: <<<<<<<<<<<<< got Private ")
                        result.forEach {   document ->
                            val job = document.toObject<Job>()
                            retrievedJobs.add(job)
                        }
                        onSuccessHandler(retrievedJobs)
                    }.addOnFailureListener {
                        Log.i("TAG", "fetchMyOngoingWork: >>>>>>>>>>>>>>> Private failed")
                        onSuccessHandler(retrievedJobs)
                    }
            }.addOnFailureListener {
                onFailureHandler()
            }
    }

    fun fetchMyCompletedWork(onSuccessHandler : (jobs : List<Job>)-> Unit, onFailureHandler : ()->Unit){
        val retrievedJobs = ArrayList<Job>()
        Log.i("TAG", "fetchMyCompletedWork: <<<<<<<<<<, Fetch from DB !!")
        db.collection("Jobs").whereEqualTo("techID",auth.currentUser?.uid)
            .whereEqualTo("status",Job.JobStatus.Completed.rawValue)
            .get().addOnSuccessListener {
                    queryResult->
                queryResult.forEach {   document ->
                    val job = document.toObject<Job>()
                    Log.i("TAG", "fetchMyCompletedJobs: >>>> $job")
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
            .whereEqualTo("privateRequest",false)
            .whereIn("areaLocation",workLocations)
            .get().addOnSuccessListener {
                    queryResult->
                queryResult.forEach {   document ->
                    val job = document.toObject<Job>()
                    Log.i("TAG", "AVAILABLE: >>>> $job")
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

    fun updateWorkLocations(loc : String, onSuccessHandler: () -> Unit,onFailHandler: () -> Unit){
        db.collection("Users").document(auth.uid ?: "")
            .update("workLocations", FieldValue.arrayUnion(loc))
            .addOnSuccessListener {
                onSuccessHandler()
            }.addOnFailureListener {
                onFailHandler()
            }
    }

    fun updateDocument(collectionName: String, map: Map<String, Any>, documentId: String,
                       onSuccessHandler: () -> Unit,
                       onFailureHandler: () -> Unit) {
        Log.i("TAG", "updateJob: start updating $map")
        db.collection(collectionName).document(documentId)
            .update(map)
            .addOnSuccessListener { onSuccessHandler()}
            .addOnFailureListener { onFailureHandler() }
    }
}