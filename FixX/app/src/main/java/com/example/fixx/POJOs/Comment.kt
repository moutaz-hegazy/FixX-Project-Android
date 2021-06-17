package com.example.fixx.POJOs

data class Comment(var username : String = "",val userId : String? = null, var commentContent : String? = null,
                   var profilePicture : String? = null,
                   var date : String? = null,
                   var reply : String? = null,
                   var timestamp : Long? = null,
                   var rating : Double? = null)

data class CommentData(val comment : Comment? = null)