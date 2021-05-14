package com.example.fixx.takeOrderScreen.POJOs

import android.graphics.Bitmap

data class Job (val uid: Int, val type : String, val location : String, val status : JobStatus = JobStatus.OnRequest){
    var description : String = ""
    var date : String = ""
    var fromTime : String = ""
    var toTime : String = ""
    var price : Int? = null
    var images : MutableList<Bitmap>? = null
    var techID : Int? = null
    var bidders : MutableList<Int>? = null


    enum class JobStatus {
        OnRequest,
        Accepted,
        OnGuarantee,
        Completed
    }
}