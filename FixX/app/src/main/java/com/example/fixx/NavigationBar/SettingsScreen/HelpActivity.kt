package com.example.fixx.NavigationBar.SettingsScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.fixx.R

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        supportActionBar?.hide()


        val about = findViewById(R.id.help_aboutus_linearlayout) as LinearLayout
        about.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                val intent = Intent(this@HelpActivity,AboutUsActivity::class.java)
                startActivity(intent)

            }
        })

        val safety = findViewById(R.id.help_safetytips_linearlayout) as LinearLayout
        safety.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                val intent = Intent(this@HelpActivity,SafetyTipsActivity::class.java)
                startActivity(intent)

            }
        })

        val terms = findViewById(R.id.help_terms_linearlayout) as LinearLayout
        terms.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                val intent = Intent(this@HelpActivity,TermsActivity::class.java)
                startActivity(intent)

            }
        })

    }


}