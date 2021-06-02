package com.example.fixx.techOrderDetailsScreen.models

data class ReplyNotificationData (val type : String, val user : String, val title : Int, val message : Int, val jobId : String,
                                  var price : String? = null)