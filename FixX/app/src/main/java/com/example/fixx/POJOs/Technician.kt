package com.example.fixx.POJOs

class Technician(phoneNumber: String = "", accountType: String = "", name: String = "", email: String = "")
    : Person(phoneNumber, accountType, name, email) {
    var id : String? = null
    var profilePicture: String? = null
    var locations: List<String>? = null
    var jobTitle: String? = null
    var workLocations: List<String>? = null
    var rating: Double? = null
    var monthlyRating: Double? = null
}