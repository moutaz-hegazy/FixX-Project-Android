package com.example.fixx.POJOs

class Technician(phoneNumber: String = "", accountType: String = "", name: String = "", email: String = "", uid : String = "")
    : Person(phoneNumber, accountType, name, email, uid) {
    var jobTitle: String? = null
    var workLocations: ArrayList<String>? = null
    var rating: Double? = null
    var monthlyRating: Int? = null
    var jobsCount : Int = 0
    var reviewCount:Int = 0
}