package com.example.fixx.LoginScreen.Views

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.CURRENT_LANGUAGE
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.FirebaseService
import com.example.fixx.constants.Constants
import java.util.*

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        CURRENT_LANGUAGE = getSharedPreferences(Constants.LANGUAGE_SHARED_PREFERENCES,Context.MODE_PRIVATE)
            .getString(Constants.CURRENT_LANGUAGE,"en")  ?: "en"// your language
        val locale = Locale(CURRENT_LANGUAGE)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        applicationContext.resources?.updateConfiguration(
            config,
            applicationContext.resources!!.displayMetrics
        )

        var appLogo = findViewById<ImageView>(R.id.appLogo)
        appLogo.setImageResource(R.drawable.logo_app)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        checkLogin()
    }

    private fun checkLogin(){
        if(FirestoreService.auth.currentUser != null
            && FirestoreService.auth.currentUser?.email != "defaultaccount@default.com"){
            Log.i("TAG", "checkLogin: HEERREE <<<<<<<<<<<<<<< ${FirestoreService.auth?.currentUser?.email} ")
            FirestoreService.fetchUserFromDB {
                    person ->
                NavigationBarActivity.USER_OBJECT = person
                FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
//                FirebaseMessaging.getInstance().token.addOnSuccessListener {
//                    FirebaseService.token = it
//                }

//                FirebaseMessaging.getInstance()
//                    .subscribeToTopic("${Constants.CHAT_TOPIC}_${person?.uid}").addOnSuccessListener {
//                        Log.i("TAG", "checkLogin: SUBSCRIPED <<<<<<<<<<<<<<<")
//                    }.addOnCompleteListener { task ->
//                        Log.i("TAG", "checkLogin: COMPLETE <<<<<<<<<<<<<<<")
//                        var msg = "SUCCESS !!"
//                        if (!task.isSuccessful) {
//                            msg = "FAIL !!"
//                        }
//                        Log.d("TAG", msg)
//                        //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//                    }.addOnCanceledListener {
//                        Log.i("TAG", "checkLogin: CANCELED <<<<<<<<<<<<<<<")
//                    }.addOnFailureListener {
//                        Log.i("TAG", "checkLogin: FALIURE <<<<<<<<<<<<<<< "+ it.localizedMessage)
//                    }
                Intent(applicationContext,NavigationBarActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }else{
            Handler().postDelayed({
                Intent(this, RegistrationActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }, 1500)
        }
    }
}