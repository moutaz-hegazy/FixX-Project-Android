package com.example.fixx.inAppChatScreens.model

data class ChatPushNotification(
    var data:NotificationData,
//    var to:String
    var registration_ids : Array<String>
)