package com.example.fixx.NavigationBar.SettingsScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fixx.R

class SafetyTipsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safety_tips)
        supportActionBar?.hide()
    }
}