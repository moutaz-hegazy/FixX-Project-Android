package com.example.fixx

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fixx.POJOs.Details
import java.util.regex.Pattern

class SignUpTabFragment: Fragment() {

    //AppUserData...
    private var username: String = ""
    private var email: String = ""
    private var password: String = ""
    private var confirmPassword: String = ""
    private var passedPhoneNumber: String = ""
    private var passedAccountType: String = ""

    var usernameEditText: EditText? = null
    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    var confirmPasswordEditText: EditText? = null
    var signUpButton: Button? = null


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

        if(signUpButton != null)
            Log.i("TAG", "======== FOUND =======")
        else
            Log.i("TAG", "======== NOT FOUND =======")

        signUpButton?.setOnClickListener{
            Log.i("TAG", "======== SIGNUP CLICKED =======")
        }

        val receivedDataBundle = arguments
        if (receivedDataBundle != null) {
            val details = receivedDataBundle.getParcelable<Details>("AppUserData")
            //emailEditText!!.setText(details?.accountType)
            if (details != null) {
                passedPhoneNumber = details.phoneNumber
                passedAccountType = details.accountType
            }
        }

        /*signUpButton?.setOnClickListener{
            username = usernameEditText?.text.toString()
            email = emailEditText?.text.toString()
            password = passwordEditText?.text.toString()
            confirmPassword = confirmPasswordEditText?.text.toString()

            //checkBlankFields()

            Log.i("TAG", "onCreateView: ")

            if(username.isEmpty())
                usernameEditText?.error = "This field is required"
            if(email.isEmpty())
                emailEditText?.error = "This field is required"
            if(password.isEmpty())
                passwordEditText?.error = "This field is required"
            if(confirmPassword.isEmpty())
                confirmPasswordEditText?.error = "This field is required"

            if(validateSignUpForm2(email, password, confirmPassword)){
              startActivity(Intent(context, HomeActivity::class.java))
            }
        }*/

        return root
    }

    //private fun validateUsername(): Boolean{}

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return passwordRegex.matcher(password).matches()
    }

    private fun matchPasswords(password: String, confirmedPassword: String): Boolean{
        return password == confirmedPassword
    }

    private fun validateSignUpForm2(email: String, password: String, confirmedPassword: String):Boolean{

        var flag = false
        if(validateEmail(email) && validatePassword(password) && matchPasswords(password, confirmedPassword))
            flag = true
        else if(!validateEmail(email)) {
            emailEditText?.error = "Invalid!. Enter a valid email."
        }
        else if(!validatePassword(password)) {
            passwordEditText?.error = "Invalid!. Enter a valid password."
        }
        else if(!matchPasswords(password, confirmedPassword)){
            confirmPasswordEditText?.error = "Passwords don't match"
            Toast.makeText(context, "Please, ensure entered passwords match", Toast.LENGTH_SHORT).show()
        }
        return flag
    }

//    private fun checkBlankFields(){
//        when {
//            username.isEmpty() -> usernameEditText?.error = "This field is required"
//            email.isEmpty() -> emailEditText?.error = "This field is required"
//            password.isEmpty() -> passwordEditText?.error = "This field is required"
//            confirmPassword.isEmpty() -> confirmPasswordEditText?.error = "This field is required"
//        }
//    }

/*    private fun createAppUserObject: User()(
            passedPhoneNumber: String,
            passedAccountType: String,
            username: String,
            email: String
    ){
        return User(
            passedPhoneNumber: String,
            passedAccountType: String,
            username: String,
            email: String
        )
    }*/
}