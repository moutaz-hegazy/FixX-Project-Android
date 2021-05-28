package com.example.fixx.constants

import android.net.Uri

sealed class Constants {
    companion object{
        const val cameraPickerRequestCode = 1
        const val galleryPickerRequestCode = 2
        const val serviceName = "serviceName"
        const val CAMERA_PERMISSION_REQUEST_CODE = 200
        const val GALLERY_PERMISSION_REQUEST_CODE = 201
        const val LOCATION_TO_TECH = "locationToTech"
        const val JOB_TYPE_TO_TECH = "jobTypeToTech"
        const val RC_SIGN_IN = 9001
        const val START_ADDRESS_ACTIVITY_REQUEST_CODE = 5
        const val START_ADDRESS_MAP_REQUEST_CODE = 6
        const val TRANS_ADDRESS = "address"
        const val TRANS_JOB = "TransJob"
        const val TRANS_IMAGES = "TransImages"
        const val TRANS_USERDATA = "transUser"
        const val TRANS_IMAGES_PATHS = "TransImagesPaths"
        const val TRANS_CHAT_CHANNEL = "ChatChannelName"
        const val CHAT_TOPIC = "chats"
        const val USERS_COLLECTION = "Users"
        const val JOBS_COLLECTION = "Jobs"
        const val DEFAULT_EMAIL = "defaultaccount@default.com"
        const val DEFAULT_PASSWORD = "default123"
        const val BASE_URL = "https://fcm.googleapis.com"
        const val SERVER_KEY = "AAAARyIBb3c:APA91bFyojXc4-L2IjYoZT3MCroUFzqSTGcXT1HGCSePyo0YN0VgD0M4KKNr_Wgd_CdI5Jc0G796NXrnlCIepJwG2qZYz2o9uz1gV4Rb3CblhupUlukmTAC0izMOSGy52UdwU9Rts-jH"
        const val CONTENT_TYPE = "application/json"
        const val TECH_LIST_REQUEST_CODE = 300
        const val TECH_LIST_BOOLEAN = "BooleanResult"
        const val TECH_DETAILS_REQUESTCODE = 301
        const val NOTIFICATION_TYPE_TECH_REPLY_CONFIRM = "techReplyConfirm"
        const val NOTIFICATION_TYPE_TECH_REPLY_DENY = "techReplyDeny"
        const val NOTIFICATION_TYPE_USER_JOB_REQUEST = "jobRequest"
        const val TRANS_RESPONSE_BOOL = "responseBool"
    }
}