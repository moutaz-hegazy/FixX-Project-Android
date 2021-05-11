package eg.gov.iti.jets.fixawy.POJOs

data class Technician(var phoneNumber: String, var accountType: String, var name: String, var email: String){
    var profilePicture: String? = null
    var locations: List<String>? = null
    var jobTitle: String? = null
    var workLocations: List<String>? = null
    var rating: Double? = null
    var monthlyRating: Double? = null
}