package com.example.fixx.POJOs

import android.graphics.Bitmap
import kotlinx.android.parcel.RawValue
import java.io.Serializable

data class Job (val uid: String?, val type : String, val location : String?, val status : JobStatus = JobStatus.OnRequest): Serializable{
    var jobId : String = ""
    var description : String = ""
    var date : String = ""
    var completionDate : String = ""
    var fromTime : String = ""
    var toTime : String = ""
    var price : Int? = null
    var techID : Int? = null
    var bidders : MutableList<Int>? = null

    enum class JobStatus constructor(var rawValue: String) : Serializable {
        OnRequest("OnRequest"),
        Accepted("Accepted"),
        OnGuarantee("OnGuarantee"),
        Completed("Completed")
    }
}