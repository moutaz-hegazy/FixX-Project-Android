package com.example.fixx.LoginScreen.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.example.fixx.HomeActivity
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        var appLogo = findViewById<ImageView>(R.id.appLogo)
        appLogo.setImageResource(R.drawable.logo_app)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            val intent = if (checkLogin())
                Intent(this, NavigationBarActivity::class.java)
            else
                Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun checkLogin(): Boolean {
        if(FirestoreService.auth.currentUser != null){
            return true
        }
        return false
    }
}