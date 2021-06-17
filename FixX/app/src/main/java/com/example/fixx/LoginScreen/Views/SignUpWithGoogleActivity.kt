package com.example.fixx.LoginScreen.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.databinding.ActivitySignUpWithGoogleBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpWithGoogleActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpWithGoogleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpWithGoogleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = SignUpOnBoardingFragment()
        fragment.fromGoogle = true
        var adapter = LoginAdapter(supportFragmentManager, context = applicationContext,
            fragments = listOf(fragment))
        binding.googleSignUpViewPager.adapter = adapter
    }

    override fun onBackPressed() {
        if(supportFragmentManager.fragments.last() is SignUpOnBoardingFragment){
            super.onBackPressed()
            FirebaseAuth.getInstance().signOut()
        }else{
            supportFragmentManager.beginTransaction().remove(supportFragmentManager.fragments.last()).commit()
        }
    }
}