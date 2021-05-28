package com.example.fixx.POJOs

import android.graphics.Bitmap
import kotlinx.android.parcel.RawValue
import java.io.Serializable

data class Job (val uid: String? = null, val type : String = "", var location : String? = null,
                val status : JobStatus = JobStatus.OnRequest): Serializable{
    var jobId : String = ""
    var description : String = ""
    var date : String = ""
    var completionDate : String = ""
    var fromTime : String? = null
    var toTime : String? = null
    var price : Int? = null
    var techID : String? = null
    var bidders : MutableMap<String,String>? = null
    var images : MutableList<String>? = null
    var isPrivate = false

    enum class JobStatus constructor(var rawValue: String) : Serializable {
        OnRequest("OnRequest"),
        Accepted("Accepted"),
        Completed("Completed")
    }
}