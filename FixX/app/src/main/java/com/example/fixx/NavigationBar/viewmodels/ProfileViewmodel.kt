package com.example.fixx.NavigationBar.viewmodels

import android.net.Uri
import android.util.Log
import com.example.fixx.POJOs.Person
import com.example.fixx.POJOs.StringPair
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ProfileViewmodel (val person : Person){
    fun updateProfilePic(path : Uri, onSuccessBinding: (StringPair)->Unit,
                         onFailBinding:()->Unit){
        person.profilePicture?.let {
            FirestoreService.deleteImage(it.first)
        }
        FirestoreService.uploadImageToStorage(mutableListOf(path)){ links->
            if(links.isNotEmpty()){
                FirestoreService.updateDocumentField(Constants.USERS_COLLECTION, "profilePicture", links[0],
                    person.uid ?: "",
                    onSuccessHandler = {
                        onSuccessBinding(links[0])
                    },onFailHandler = {
                        onFailBinding()
                    })
            }
        }
    }

    fun updateName(name : String, onSuccessBinding: () -> Unit, onFailBinding: () -> Unit){
        FirestoreService.updateDocumentField(Constants.USERS_COLLECTION, "name", name,
            person.uid ?: "",onSuccessHandler = {
                onSuccessBinding()
            },onFailHandler = {
                onFailBinding()
            })
    }

    fun updateEmail(email : String,password: String, onSuccessBinding: () -> Unit,onFailBinding: (Boolean) -> Unit){
        FirestoreService.checkIfEmailExists(email){ exists->
            if(exists){
                onFailBinding(true)
            }else{
                val credentials = EmailAuthProvider.getCredential(person.email,password)
                FirestoreService.auth.currentUser?.reauthenticate(credentials)?.addOnCompleteListener {
                    if(it.isSuccessful){
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.updateEmail(email)?.addOnCompleteListener {
                            if(it.isSuccessful){
                                FirestoreService.updateDocumentField(Constants.USERS_COLLECTION, "email",
                                    email, person.uid ?: "",
                                    onSuccessHandler = {
                                        onSuccessBinding()
                                    }, onFailHandler = {
                                        onFailBinding(false)
                                    })
                            }else{
                                onFailBinding(false)
                            }
                        }
                    }
                }
            }
        }
    }

    fun updatePassword(newpassword: String,oldPassword : String, onSuccessBinding: () -> Unit,onFailBinding: () -> Unit){
        val credentials = EmailAuthProvider.getCredential(person.email,oldPassword)
        FirestoreService.auth.currentUser?.reauthenticate(credentials)?.addOnCompleteListener {
            if(it.isSuccessful){
                val user = FirebaseAuth.getInstance().currentUser
                user?.updatePassword(newpassword)?.addOnCompleteListener {
                    if(it.isSuccessful){
                        onSuccessBinding()
                    }else{
                        onFailBinding()
                    }
                }
            }
        }
    }

    fun updatePhoneNumber(phone : String, onSuccessBinding: () -> Unit,onFailBinding: (Boolean) -> Unit){
        FirestoreService.checkIfPhoneExists(phone){ exists->
            if(exists){
                onFailBinding(true)
            }else{
                FirestoreService.updateDocumentField(Constants.USERS_COLLECTION, "phoneNumber",
                    phone, person.uid ?: "",
                    onSuccessHandler = {
                        onSuccessBinding()
                    }, onFailHandler = {
                        onFailBinding(false)
                    })
            }
        }
    }

    fun verifyPassword(password : String,onSuccessBinding: () -> Unit,onFailBinding: () -> Unit){
        FirestoreService.loginWithEmailAndPassword(person.email,password,
            onSuccessHandler = {
                onSuccessBinding()
            },onFailHandler = {
                onFailBinding()
            })
    }
}