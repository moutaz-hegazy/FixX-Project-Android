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
        const val REQUEST_CODE = 100
        const val CAMERA_REQUEST = 1888
    }
}