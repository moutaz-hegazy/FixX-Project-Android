package com.example.fixx

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.regex.Pattern

class LoginTabFragment: Fragment() {

    var alpha = 0
    var email: String = ""
    var password: String = ""

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
        val root = layoutInflater.inflate(R.layout.fragment_login_tab, container, false)

        var emailEditText = root.findViewById<EditText>(R.id.email)
        var passwordEditText = root.findViewById<EditText>(R.id.pass)
        var forgotPassword = root.findViewById<TextView>(R.id.forgotPass)
        var loginButton = root.findViewById<Button>(R.id.login_button)

        forgotPassword.setOnClickListener(View.OnClickListener {
            Toast.makeText(context, "Oops, forgot my password!!!", Toast.LENGTH_SHORT).show()
        })

        loginButton.setOnClickListener(View.OnClickListener {
            email = emailEditText.text.toString()
            password = passwordEditText.text.toString()

            if(email.isEmpty())
                emailEditText.error = "This field is required"

            if(password.isEmpty())
                passwordEditText.error = "This field is required"

            if(!validateEmail(email)) {
                emailEditText?.error = "Invalid Email!. Enter a valid email."
            }

            if(!validatePassword(password)) {
                passwordEditText?.error = "Invalid Password!. Enter a valid password."
            }

            //firebase authenticate login then move to home
            //checkLogin(email, password)
        })

        emailEditText.translationX = 800F
        passwordEditText.translationX = 800F
        forgotPassword.translationX = 800F
        loginButton.translationX = 800F

        emailEditText.alpha = alpha.toFloat()
        passwordEditText.alpha = alpha.toFloat()
        forgotPassword.alpha = alpha.toFloat()
        loginButton.alpha = alpha.toFloat()

        emailEditText?.animate()?.translationX(0F)?.alpha(1F)?.setDuration(1000)?.setStartDelay(400)?.start()
        passwordEditText?.animate()?.translationX(0F)?.alpha(1F)?.setDuration(1000)?.setStartDelay(600)?.start()
        forgotPassword?.animate()?.translationX(0F)?.alpha(1F)?.setDuration(1000)?.setStartDelay(600)?.start()
        loginButton?.animate()?.translationX(0F)?.alpha(1F)?.setDuration(1000)?.setStartDelay(800)?.start()

        return root
    }

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return passwordRegex.matcher(password).matches()
    }
}