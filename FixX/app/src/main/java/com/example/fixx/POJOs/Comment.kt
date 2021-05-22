package com.example.fixx.POJOs

data class Comment(var username : String = "", var commentContent : String  = "") {
    var profilePicture : String? = null
    var date : String? = null
    var reply : String? = null
}