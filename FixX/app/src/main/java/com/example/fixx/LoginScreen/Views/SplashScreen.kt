package com.example.fixx.LoginScreen.Views

import android.app.AlertDialog
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

        val lang = getSharedPreferences(Constants.LANGUAGE_SHARED_PREFERENCES,Context.MODE_PRIVATE)
            .getString(Constants.CURRENT_LANGUAGE,"none")

        if(lang == "none"){
            Log.i("TAG", "onCreate: <<<<<<<<<<<< NULL")
            chooseLanguageDialog()
        }else {
            Log.i("TAG", "onCreate: <<<<<<<<<<<< not Null :'(")
            setLanguage(lang ?: "en")
            checkLogin()
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        var appLogo = findViewById<ImageView>(R.id.appLogo)
        appLogo.setImageResource(R.drawable.logo_app)
    }

    private fun setLanguage(lang : String){
        CURRENT_LANGUAGE = lang
        val locale = Locale(CURRENT_LANGUAGE)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        applicationContext.resources?.updateConfiguration(
            config,
            applicationContext.resources!!.displayMetrics
        )
    }

    private fun saveCurrentLnaguage(lang : String){
        getSharedPreferences(Constants.LANGUAGE_SHARED_PREFERENCES,Context.MODE_PRIVATE)
            .edit().putString(Constants.CURRENT_LANGUAGE,lang).apply()
    }
    private fun checkLogin(){
        if(FirestoreService.auth.currentUser != null
            && FirestoreService.auth.currentUser?.email != "defaultaccount@default.com"){
            Log.i("TAG", "checkLogin: HEERREE <<<<<<<<<<<<<<< ${FirestoreService.auth?.currentUser?.email} ")
            FirestoreService.fetchUserFromDB (onCompletion = {
                    person ->
                NavigationBarActivity.USER_OBJECT = person
                FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
                Intent(applicationContext,NavigationBarActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            },passRegister = {
                NavigationBarActivity.USER_OBJECT_OBSERVER = it
            })
        }else{
            Handler().postDelayed({
                Intent(this, RegistrationActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }, 1500)
        }
    }


    private fun chooseLanguageDialog(){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(getString(R.string.selectLanguageTitle))
        //set message for alert dialog
        builder.setMessage(getString(R.string.SelectLanguageMsg))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("English"){ _, _ ->
            saveCurrentLnaguage("en")
            setLanguage("en")
            checkLogin()
        }
        //performing negative action
        builder.setNegativeButton("العربية"){ _, _ ->
            saveCurrentLnaguage("ar")
            setLanguage("ar")
            checkLogin()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
        alertDialog.window!!.setBackgroundDrawableResource(R.drawable.btn_border)

    }
}