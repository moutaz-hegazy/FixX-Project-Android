package eg.gov.iti.jets.fixawy.POJOs

data class User(var phoneNumber: String, var accountType: String, var name: String, var email: String){
    var profilePicture: String? = null
    var locations: List<String>? = null
}