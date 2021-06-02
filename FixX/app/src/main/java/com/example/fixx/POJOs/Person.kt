package com.example.fixx.POJOs

import java.io.Serializable

abstract class Person(
    var phoneNumber: String = "",
    var accountType: String = "",
    var name: String = "",
    var email: String = "",
    var uid : String? = null,
    var token : String? = null,
    var profilePicture: String? = null,
    var locations: MutableList<String>? = null
) : Serializable