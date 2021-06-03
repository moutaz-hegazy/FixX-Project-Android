package com.example.fixx.constants
import android.net.Uri
import android.content.res.Resources
import com.example.fixx.R

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
        const val TRANS_CONTACT_UID = "contactUid"
        const val TRANS_JOB_OBJECT = "transJobObject"
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
        const val NOTIFICATION_TYPE_CHAT_MESSAGE = "chatMsg"
        const val NOTIFICATION_TYPE_USER_ACCEPT = "userAccept"
        const val TRANS_RESPONSE_BOOL = "responseBool"
        const val LANGUAGE_SHARED_PREFERENCES = "languageSP"
        const val CURRENT_LANGUAGE = "current_lang"

//        val cities = arrayOf(getString(R.string.City),
//                getString(R.string.Cairo),
//                getString(R.string.Alexandria))
//        val cairoArea = arrayOf(
//            "Area",
//            "Al-Shrouk",
//            "1st Settlement",
//            "Fifth Settlement",
//            "Madenti",
//            "Al-Rehab",
//            "10th Of Ramadan",
//            "Badr City",
//            "Zamalek",
//            "Heliopolis",
//            "Nasser City",
//            "Qobbah",
//            "Maadi",
//            "Mokkatm",
//            "Mohandsen",
//            "Shekh Zayed",
//            "Dokki",
//            "Giza Square",
//            "Haram",
//            "Fissal",
//            "Shobra",
//            "Obour",
//            "Matareya",
//            "6th October",
//            "Helwan",
//            "Ain Shams",
//            "Manyal",
//            "Agouza"
//        )
//        val alexArea = arrayOf(
//            "Area",
//            "Moharam Bek",
//            "Abu Qir",
//            "Montaza",
//            "Al Hadarah",
//            "Al Ibrahimeyah",
//            "Asafra",
//            "Al Azaritah",
//            "Bahari",
//            "Dekhela",
//            "Bokli",
//            "Borg Al Arab",
//            "Al Qabari",
//            "Fleming",
//            "Janklees",
//            "Gleem",
//            "Kafr Abdou",
//            "Louran",
//            "El Mandara",
//            "Miami",
//            "San Stifano",
//            "Sidi Beshr",
//            "Sidi Gaber",
//            "Shatebi",
//            "Sporting",
//            "Victoria",
//            "Smouha",
//            "Stanli",
//            "Wabor El Maya",
//            "El Hanovil",
//            "El Bitash",
//            "Qism Bab Sharqi",
//            //"Qism El-Raml",
//            "Mansheya",
//            "Al Attarin",
//            "First Al Raml",
//            "Mustafa Kamel"
//        )
    }
}