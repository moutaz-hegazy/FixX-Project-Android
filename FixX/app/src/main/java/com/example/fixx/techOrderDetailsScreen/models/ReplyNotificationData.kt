package com.example.fixx.techOrderDetailsScreen.models

data class ReplyNotificationData (val type : String, val techName : String, val title : Int, val message : Int, val jobId : String,
                                 var techId : String? = null, var price : String? = null)