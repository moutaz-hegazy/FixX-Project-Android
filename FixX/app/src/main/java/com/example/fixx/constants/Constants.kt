package com.example.fixx.constants
import android.net.Uri
import android.content.res.Resources
import com.example.fixx.R

sealed class Constants {

    companion object{

        const val START_WORK_ADDRESS_ACTIVITY_REQUEST_CODE = 400
        const val cameraPickerRequestCode = 1
        const val galleryPickerRequestCode = 2
        const val serviceName = "serviceName"
        const val CAMERA_PERMISSION_REQUEST_CODE = 200
        const val GALLERY_PERMISSION_REQUEST_CODE = 201
        const val EXTERNAL_STORAGE_REQUEST_CODE = 203
        const val PLACES_AUTOCOMPLETE_REQUEST_CODE = 100
        const val LOCATION_TO_TECH = "locationToTech"
        const val JOB_TYPE_TO_TECH = "jobTypeToTech"
        const val RC_SIGN_IN = 9001
        const val EXTEND_ACTIVIVTY_REQUEST_CODE = 900
        const val START_ADDRESS_ACTIVITY_REQUEST_CODE = 5
        const val START_ADDRESS_MAP_REQUEST_CODE = 6
        const val TRANS_ADDRESS = "address"
        const val TRANS_JOB = "TransJob"
        const val TRANS_IMAGES = "TransImages"
        const val TRANS_USERDATA = "transUser"
        const val TRANS_USER_NAME = "UserName"
        const val TRANS_GOOGLE_BOOL = "GoogleBool"
        const val TRANS_IMAGES_PATHS = "TransImagesPaths"
        const val TRANS_CHAT_CHANNEL = "ChatChannelName"
        const val TRANS_CONTACT_UID = "contactUid"
        const val TRANS_JOB_OBJECT = "transJobObject"
        const val TRANS_EXTENSION = "transExt"
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
        const val NOTIFICATION_TYPE_TECH_REPLY_CANCEL = "techReplyCancel"
        const val NOTIFICATION_TYPE_USER_JOB_REQUEST = "jobRequest"
        const val NOTIFICATION_TYPE_CHAT_MESSAGE = "chatMsg"
        const val NOTIFICATION_TYPE_USER_ACCEPT = "userAccept"
        const val NOTIFICATION_TYPE_JOB_COMPLETED = "jobCompleted"
        const val TRANS_RESPONSE_BOOL = "responseBool"
        const val LANGUAGE_SHARED_PREFERENCES = "languageSP"
        const val CURRENT_LANGUAGE = "current_lang"
        const val TRANS_EDIT_MODE = "editMode"
        const val TRANSIT_FROM_NOTIFICATION = "fromNotification"
        const val TRANS_NOTIFICATION_TYPE = "notificationType"
        const val TRANS_DATA_BUNDLE = "dataBundle"
        const val NOTIFICATION_GROUP = "fixXNotGroup"
        const val CHAT_RECEIVER_FILTER = "chatReceiverFilter"
        const val TECH_ORDER_DETAILS_FILTER = "techOrdersFilter"
        const val USER_JOB_DETAILS_FILTER = "userJobFilter"
        const val CHANNEL_ID = "channelId"

        val cities = arrayOf("City","Cairo", "Alexandria")
        val citiesInArabic = arrayOf("مدينة","القاهرة", "الإسكندرية")

        val cairoArea = arrayOf(
            "Area",
            "Al-Shrouk",
            "1st Settlement",
            "Fifth Settlement",
            "Madenti",
            "Al-Rehab",
            "10th Of Ramadan",
            "Badr City",
            "Zamalek",
            "Heliopolis",
            "Nasser City",
            "Qobbah",
            "Maadi",
            "Mokkatm",
            "Mohandsen",
            "Shekh Zayed",
            "Dokki",
            "Giza Square",
            "Haram",
            "Fissal",
            "Shobra",
            "Obour",
            "Matareya",
            "6th October",
            "Helwan",
            "Ain Shams",
            "Manyal",
            "Agouza"
        )
        val cairoAreaInArabic = arrayOf(
            "منطقة",
            "الشروق",
            "التجمع الأول",
            "التجمع الخامس",
            "مدينتي",
            "الرحاب",
            "العاشر من رمضان",
            "مدينة بدر",
            "الزمالك",
            "هليوبليس",
            "مدينة نصر",
            "القبة",
            "المعادى",
            "المقطم",
            "المهندسين",
            "الشيخ زايد",
            "الدقى",
            "ميدان الجيزة",
            "الهرم",
            "فيصل",
            "شبرا",
            "العبور",
            "المطرية",
            "السادس من أكتوبر",
            "حلوان",
            "عين شمس",
            "المنيل",
            "العجوزة"
        )

        val alexArea = arrayOf(
            "Area",
            "AR Riyadah",
            "Moharam Bek",
            "Abu Qir",
            "Montaza",
            "Al Hadarah",
            "Al Ibrahimeyah",
            "Asafra",
            "Al Azaritah",
            "Bahari",
            "Dekhela",
            "Bokli",
            "Borg Al Arab",
            "Al Qabari",
            "Fleming",
            "Janklees",
            "Gleem",
            "Kafr Abdou",
            "Louran",
            "El Mandara",
            "Miami",
            "San Stifano",
            "Sidi Beshr",
            "Smouha",
            "Sidi Gaber",
            "Shatebi",
            "Sporting",
            "Victoria",
            "Stanli",
            "Wabor El Maya",
            "El Hanovil",
            "El Bitash",
            "Qism Bab Sharqi",
            //"Qism El-Raml",
            "Mansheya",
            "Al Attarin",
            "First Al Raml",
            "Mustafa Kamel",
            "Ezbet Saad",
            "Abis"
        )
        val alexAreaInArabic = arrayOf(
            "منطقة",
            "الرياضة",
            "محرم بك",
            "أبو قير",
            "المنتزة",
            "الحضرة",
            "الإبراهيمية",
            "العصافرة",
            "الأزاريطة",
            "بحرى",
            "الدخيلة",
            "بوكلى",
            "برج العرب",
            "القباري",
            "فليمنج",
            "جناكليس",
            "جليم",
            "كفر عبده",
            "لوران",
            "المندرة",
            "ميامى",
            "سان استفانو",
            "سيدي بشر",
            "سموحة",
            "سيدي جابر",
            "الشاطبي",
            "سبورتنج",
            "فيكتوريا",
            "ستانلي",
            "وابور المياة",
            "الهانوفيل",
            "البطاش",
            "قسم باب شرقي",
            "المنشية",
            "العطارين",
            "أول الرمل",
            "مصطفى كامل",
            "عزبة سعد",
            "أبيس"
        )

        val jobs = arrayOf("Job","Painter","Plumber","Electrician","Carpenter","Tiles Handyman","Parquet","Smith","Decoration Stones","Alumetal",
        "Air Conditioner","Curtains","Glass","Satellite","Gypsum Works","Marble","Pest Control","Wood Painter","Pool\\n maintain")

        val jobInArabic = arrayOf("صنعة","نقاش","سباك","كهربائى","نجار","سيراميك","باركيه","حداد","واجهات حجرية","الوميتال",
            "صيانة تكييف","ستائر","زجاج","صيانة دش","اعمال جبس","رخام","مكافحة حشرات","استرجى","حمامات سباحة")
    }
}