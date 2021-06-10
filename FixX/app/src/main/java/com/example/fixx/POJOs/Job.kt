package com.example.fixx.POJOs

import android.graphics.Bitmap
import android.location.Location
import kotlinx.android.parcel.RawValue
import java.io.Serializable

data class Job (val uid: String? = null, val type : String = "",
                val status : JobStatus = JobStatus.OnRequest,
                var jobId : String = "",
                var description : String = "",
                var date : String = "",
                var completionDate : String = "",
                var fromTime : String? = null,
                var toTime : String? = null,
                var price : Int? = null,
                var techID : String? = null,
                var bidders : MutableMap<String,String>? = null,
                var images : MutableList<StringPair>? = null,
                var privateRequest : Boolean = false): Serializable{
    var location : String? = null
    set(value) {
        areaLocation = value?.substringAfter("%")?.substringBefore("/")
        field = value
    }
    var areaLocation : String? = null
    var rateable : Boolean = false
    var privateTechUid : String? = null

    constructor(uid: String? = null, type : String = "",
                location: String? = null,
                status : JobStatus = JobStatus.OnRequest,
                jobId : String = "",
                description : String = "",
                date : String = "",
                completionDate : String = "",
                fromTime : String? = null,
                toTime : String? = null,
                price : Int? = null,
                techID : String? = null,
                bidders : MutableMap<String,String>? = null,
                images : MutableList<StringPair>? = null,
                privateRequest : Boolean = false):this(uid,type,status,jobId,description,date,
                        completionDate,fromTime,toTime,price,techID,bidders,images,privateRequest){
                    this.location = location
                }

    enum class JobStatus constructor(var rawValue: String) : Serializable {
        OnRequest("OnRequest"),
        Accepted("Accepted"),
        Completed("Completed")
    }
}