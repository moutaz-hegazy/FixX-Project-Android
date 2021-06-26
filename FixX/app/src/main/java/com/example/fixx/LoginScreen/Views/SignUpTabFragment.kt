package com.example.fixx.LoginScreen.Views

import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.fixx.POJOs.Details
import com.example.fixx.HomeActivity
import com.example.fixx.LoginScreen.viewmodels.RegisterViewmodel
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.POJOs.Technician
import com.example.fixx.POJOs.User
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login_tab.*
import java.util.regex.Pattern

class SignUpTabFragment: Fragment() {

    //AppUserData...
    private var username: String = ""
    private var email: String = ""
    private var password: String = ""
    private var confirmedPassword: String = ""
    private var passedPhoneNumber: String = ""
    private var passedAccountType: String = ""

    var usernameEditText: EditText? = null
    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    var confirmPasswordEditText: EditText? = null
    var signUpButton: Button? = null
    var progressBar: ProgressBar? = null

    var dummyButton: Button? = null

    private val viewmodel : RegisterViewmodel by lazy {
        RegisterViewmodel()
    }


    private val passwordRegex =
            Pattern.compile(
                    "^" +
                            //"(?=.*[0-9])" +         //at least 1 digit
                            //"(?=.*[a-z])" +         //at least 1 lower case letter
                            //"(?=.*[A-Z])" +         //at least 1 upper case letter
                            //"(?=.*[a-zA-Z])" +      //any letter
                            //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                            "(?=\\S+$)" +           //no white spaces
                            ".{6,}" +               //at least 6 characters
                            "$"
            )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = layoutInflater.inflate(R.layout.fragment_sign_up_tab, container, false)

        usernameEditText = root.findViewById(R.id.username)
        emailEditText = root.findViewById(R.id.email)
        passwordEditText = root.findViewById(R.id.pass)
        confirmPasswordEditText = root.findViewById(R.id.confirmPass)
        signUpButton = root.findViewById(R.id.signup_button)
        progressBar = root.findViewById(R.id.signup_progressBar)

        //dummyButton = root.findViewById(R.id.dummy_button)

        val receivedDataBundle = arguments
        if (receivedDataBundle != null) {
            val details = receivedDataBundle.getSerializable(Constants.TRANS_USERDATA) as? Details
            if (details != null) {
                passedPhoneNumber = details.phoneNumber
                passedAccountType = details.accountType
            }
        }

        usernameEditText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                usernameEditText?.error = null
            }
        })

        emailEditText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailEditText?.error = null
            }
        })

        passwordEditText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordEditText?.error = null
            }
        })

        confirmPasswordEditText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                confirmPasswordEditText?.error = null
            }
        })

        signUpButton?.setOnClickListener(View.OnClickListener {
            username = usernameEditText?.text.toString()
            email = emailEditText?.text.toString()
            password = passwordEditText?.text.toString()
            confirmedPassword = confirmPasswordEditText?.text.toString()

            if(usernameEditText?.text.isNullOrEmpty())
                usernameEditText?.error = "This field is required"
            else if(email.isEmpty())
                emailEditText?.error = "This field is required"
            else if(password.isEmpty())
                passwordEditText?.error = "This field is required"
            else if(confirmedPassword.isEmpty())
                confirmPasswordEditText?.error = "This field is required"
            else if(validateSignUpForm2(email, password, confirmedPassword)){
                FirestoreService.checkIfEmailExists(email){
                    exists ->
                    if(exists){
                        emailEditText?.text?.clear()
                        passwordEditText?.text?.clear()
                        confirmPasswordEditText?.text?.clear()
                        Toast.makeText(context, "this email already exists.", Toast.LENGTH_SHORT).show()
                    }else{
                        signUpButton?.visibility = View.INVISIBLE
                        progressBar?.visibility = View.VISIBLE

                        FirebaseAuth.getInstance().signOut()
                        FirestoreService.registerUser(email,password, onSuccessHandler = {
                            if (passedAccountType == "User"){
                                var user: User = createUserObject()
                                FirestoreService.saveUserData(user,
                                    onSuccessHandler = {
                                        person ->
                                        USER_OBJECT = person
                                        startActivity(Intent(context, NavigationBarActivity::class.java))
                                        activity?.finish()
                                    },onFailHandler = {
                                        Toast.makeText(context,"Register failed",Toast.LENGTH_SHORT)
                                    })
                            }
                            else if (passedAccountType == "Technician") {
                                val technician: Technician = createTechnicianObject()
                                FirestoreService.saveUserData(technician,
                                    onSuccessHandler = {
                                            person ->
                                        (person as? Technician)?.apply {
                                            workLocations?.forEach {
                                                viewmodel.subscribeToTopic(this.jobTitle+getWorkTopic(it))
                                            }
                                        }
                                        USER_OBJECT = person
                                        startActivity(Intent(context, NavigationBarActivity::class.java))
                                        activity?.finish()
                                    },onFailHandler = {
                                        Toast.makeText(context,"Register failed",Toast.LENGTH_SHORT)

                                        signUpButton?.visibility = View.VISIBLE
                                        progressBar?.visibility = View.INVISIBLE
                                    })
                            }
                        }, onFailHandler = {
                            Toast.makeText(context, "Register fail.",Toast.LENGTH_SHORT).show()
                            signUpButton?.visibility = View.VISIBLE
                            progressBar?.visibility = View.INVISIBLE
                        })
                    }
                }
            }
        })
        return root
    }

    private fun validateEmail(email: String): Boolean {
        var flag = false
        if(email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            flag = true
        else if(email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            emailEditText?.error = "Invalid Email!"
        return flag
    }

    private fun validatePassword(password: String): Boolean {
        var flag = false
        if(password.isNotEmpty() && passwordRegex.matcher(password).matches())
            flag = true
        else if(password.isNotEmpty() && !passwordRegex.matcher(password).matches())
            passwordEditText?.error = "Invalid Password!"
        return flag
    }

    private fun matchPasswords(password: String, confirmedPassword: String): Boolean{
        if(password != confirmedPassword)
            confirmPasswordEditText?.error = "Passwords don't match"
        return password == confirmedPassword
    }

    private fun validateSignUpForm2(email: String, password: String, confirmedPassword: String):Boolean{
        var flag = false
        if(usernameEditText?.text.isNullOrEmpty())
            usernameEditText?.error = "This field is required"
        else if(email.isNotEmpty() && !validateEmail(email))
            emailEditText?.error = "Invalid!. Enter a valid email."
        else if(password.isNotEmpty() && !validatePassword(password))
            passwordEditText?.error = "Invalid!. Enter a valid password."
        else if(!matchPasswords(password, confirmedPassword))
            confirmPasswordEditText?.error = "Passwords don't match"
        else if(validateEmail(email) && validatePassword(password) && matchPasswords(password, confirmedPassword))
            flag = true
        return flag
    }

    private fun createUserObject(): User {
        return User(passedPhoneNumber, passedAccountType, username, email)
    }

    private fun createTechnicianObject() = Technician(passedPhoneNumber, passedAccountType, username, email).apply {
        jobTitle = arguments?.getString(Constants.TRANS_JOB)
        workLocations = arrayListOf()
        arguments?.getStringArrayList(Constants.TRANS_ADDRESS)?.forEach {
            workLocations?.add("${getCityEnglishName(it.substringBefore(","))},${getAreaEnglishName(it.substringAfter(","),it.substringBefore(","))}")
        }
    }

    private fun getWorkTopic(location: String) : String{
        val city = location.substringBefore(",")
        val area = location.substringAfter(",")

        return "%"+getCityEnglishName(city).replace(" ","_", true)+"."+
                getAreaEnglishName(area,city).replace(" ","_",true).
                replace("-","_",true)
    }

    private fun getCityEnglishName(city: String): String {
        var myCity = city
        for (iterator in Constants.citiesInArabic.indices) {
            if (city.equals(Constants.citiesInArabic[iterator])) {
                myCity = Constants.cities[iterator]
            }
        }
        return myCity
    }

    private fun getAreaEnglishName(area: String, city: String): String {
        var myArea = area

        if (city.equals(getString(R.string.Cairo))) {
            for (iterator in Constants.cairoAreaInArabic.indices) {
                if (area.equals(Constants.cairoAreaInArabic[iterator])) {
                    myArea = Constants.cairoArea[iterator]
                }
            }
        } else if (city.equals(getString(R.string.Alexandria))) {
            for (iterator in Constants.alexAreaInArabic.indices) {
                if (area.equals(Constants.alexAreaInArabic[iterator])) {
                    myArea = Constants.alexArea[iterator]
                }
            }
        }

        return myArea
    }
}