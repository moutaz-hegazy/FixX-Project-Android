package com.example.fixx.NavigationBar.SettingsScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fixx.R

class TermsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        supportActionBar?.hide()
    }
}