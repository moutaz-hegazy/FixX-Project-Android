package com.example.fixx.constants

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

        const val BASE_URL = "https://fcm.googleapis.com"
        const val SERVER_KEY = "AAAARyIBb3c:APA91bFyojXc4-L2IjYoZT3MCroUFzqSTGcXT1HGCSePyo0YN0VgD0M4KKNr_Wgd_CdI5Jc0G796NXrnlCIepJwG2qZYz2o9uz1gV4Rb3CblhupUlukmTAC0izMOSGy52UdwU9Rts-jH"
        const val CONTENT_TYPE = "application/json"
    }
}