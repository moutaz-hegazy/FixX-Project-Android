package com.example.fixx.POJOs


class User(phoneNumber: String = "", accountType: String = "", name: String ="", email: String = "")
    : Person(phoneNumber,accountType,name,email){
    var uid : String? = null
    var profilePicture: String? = null
    var locations: List<String>? = null
}