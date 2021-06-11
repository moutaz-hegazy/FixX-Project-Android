package com.example.fixx.LoginScreen.Views

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fixx.HomeActivity
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT_OBSERVER
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import com.google.firebase.messaging.FirebaseMessaging
import java.util.regex.Pattern
import kotlin.reflect.jvm.internal.impl.load.java.Constant

class LoginTabFragment: Fragment() {

    var alpha = 0
    private var email: String = ""
    private var password: String = ""
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var forgotPassword: TextView? = null
    private var loginButton: Button? = null

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

        emailEditText = root.findViewById<EditText>(R.id.email)
        passwordEditText = root.findViewById<EditText>(R.id.pass)
        forgotPassword = root.findViewById<TextView>(R.id.forgotPass)
        loginButton = root.findViewById<Button>(R.id.login_button)

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

        forgotPassword?.setOnClickListener(View.OnClickListener {
            Toast.makeText(context, "Oops, forgot my password!!!", Toast.LENGTH_SHORT).show()
        })

        loginButton?.setOnClickListener(View.OnClickListener {
            email = emailEditText?.text.toString()
            password = passwordEditText?.text.toString()
//            validateLoginForm(email, password)

            if (email.isEmpty())
                emailEditText?.error = "This field is required"
            if (password.isEmpty())
                passwordEditText?.error = "This field is required"
            else {
                FirestoreService.loginWithEmailAndPassword(email, password, onSuccessHandler = {
                    person ->
                    USER_OBJECT = person
                    val home = Intent(context, NavigationBarActivity::class.java)
                    startActivity(home)
                    activity?.finish()
                },
                    onFailHandler = {
                        Toast.makeText(context, "Failed to log in", Toast.LENGTH_SHORT).show()
                },passRegister = {
                    USER_OBJECT_OBSERVER = it
                })

                FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                    FirestoreService.updateDocumentField(Constants.USERS_COLLECTION,"token",token,FirestoreService.auth.uid!!)
                }
            }
        })

        emailEditText?.translationX = 800F
        passwordEditText?.translationX = 800F
        forgotPassword?.translationX = 800F
        loginButton?.translationX = 800F

        emailEditText?.alpha = alpha.toFloat()
        passwordEditText?.alpha = alpha.toFloat()
        forgotPassword?.alpha = alpha.toFloat()
        loginButton?.alpha = alpha.toFloat()

        emailEditText?.animate()?.translationX(0F)?.alpha(1F)?.setDuration(1000)?.setStartDelay(400)?.start()
        passwordEditText?.animate()?.translationX(0F)?.alpha(1F)?.setDuration(1000)?.setStartDelay(600)?.start()
        forgotPassword?.animate()?.translationX(0F)?.alpha(1F)?.setDuration(1000)?.setStartDelay(600)?.start()
        loginButton?.animate()?.translationX(0F)?.alpha(1F)?.setDuration(1000)?.setStartDelay(800)?.start()

        return root
    }

    private fun validateEmail(email: String): Boolean {
        var flag = false
        if(email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            flag = true
        else if(email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText?.error = "Enter a proper email format"
        }
        else if(email.isEmpty())
            emailEditText?.error = "This field is required"
        return flag
    }

    private fun validatePassword(password: String): Boolean {
        var flag = false
        if(password.isNotEmpty() && passwordRegex.matcher(password).matches())
            flag = true
        else if(password.isNotEmpty() && !passwordRegex.matcher(password).matches()){
            passwordEditText?.error = "Enter a minimum 6-character password"
        }
        else if(password.isEmpty())
            passwordEditText?.error = "This field is required"
        return flag
    }

    private fun validateLoginForm(email: String, password: String){
        if(validateEmail(email) && validatePassword(password))
            startActivity(Intent(context, HomeActivity::class.java))
    }
}