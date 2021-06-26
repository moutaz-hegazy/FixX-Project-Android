package com.example.fixx.LoginScreen.Views

import android.app.Application
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.CURRENT_LANGUAGE
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.*
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

@Suppress("DEPRECATION")
class RegistrationActivity : AppCompatActivity(){


    var firebaseAuth: FirebaseAuth? = null
//    var mAppEventsLogger: AppEventsLogger? = null

    var alpha = 0F

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    var google: Button? = null
    var progressBar: ProgressBar? = null

    var googleTxt: TextView? = null
    var googleImg: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLanguage(CURRENT_LANGUAGE)

        setContentView(R.layout.activity_registration)

        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()

        tabLayout = findViewById(R.id.myOrders_tablayout)
        viewPager = findViewById(R.id.myOrders_viewPager)

        google = findViewById(R.id.register_google_btn)
        progressBar = findViewById(R.id.google_progressBar)
        googleImg = findViewById(R.id.google_img)
        googleTxt = findViewById(R.id.google_txt)


        tabLayout?.newTab()?.setText(R.string.login)?.let {
            tabLayout?.addTab(it)
        }
        tabLayout?.newTab()?.setText(R.string.sign_up)?.let {
            tabLayout?.addTab(it)
        }

        tabLayout?.tabGravity = TabLayout.GRAVITY_FILL
        var viewPager = findViewById<ViewPager>(R.id.myOrders_viewPager)
        var adapter = LoginAdapter(supportFragmentManager, context = applicationContext,
            fragments = listOf(LoginTabFragment(),SignUpOnBoardingFragment()))
        viewPager?.adapter = adapter
        viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        tabLayout?.alpha = alpha

        tabLayout?.animate()?.translationX(0F)?.alpha(1F)?.setDuration(1000)?.setStartDelay(800)?.start()

        google?.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Google", Toast.LENGTH_SHORT).show()
            FirestoreService.signInWithGoogle(this)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        FirestoreService.googleSignInRequestResult(requestCode, data, onSuccessHandler = {
            email ->
            FirestoreService.checkIfEmailExists(email){
                exists ->
                if(exists){

                    google?.visibility = View.INVISIBLE
                    googleTxt?.visibility = View.INVISIBLE
                    googleImg?.visibility = View.INVISIBLE
                    progressBar?.visibility = View.VISIBLE

                    FirestoreService.fetchUserOnce{ person->
                        USER_OBJECT = person
                        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                            FirestoreService.updateDocumentField(Constants.USERS_COLLECTION,"token",
                                token,FirestoreService.auth.uid!!)
                            USER_OBJECT?.token = token
                            startActivity(Intent(this, NavigationBarActivity::class.java))
                            finish()
                        }
                    }
                }
                else{
                    google?.visibility = View.INVISIBLE
                    googleTxt?.visibility = View.INVISIBLE
                    googleImg?.visibility = View.INVISIBLE
                    progressBar?.visibility = View.VISIBLE
                    // sign up activity to enter extra data (phone, type, username)
                    Intent(this,SignUpWithGoogleActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
            }
        }, onFailHandler = {
            Toast.makeText(this, "Failed to login", Toast.LENGTH_SHORT).show()
            google?.visibility = View.VISIBLE
            googleTxt?.visibility = View.VISIBLE
            googleImg?.visibility = View.VISIBLE
            progressBar?.visibility = View.INVISIBLE
        })
    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser == null){
            FirestoreService.loginWithEmailAndPassword(Constants.DEFAULT_EMAIL,Constants.DEFAULT_PASSWORD,
                onSuccessHandler = {}, onFailHandler = {})
        }
    }

    override fun onBackPressed() {
        if(tabLayout?.selectedTabPosition == 0){
            super.onBackPressed()
        }else{
            if(supportFragmentManager.fragments.last() is SignUpOnBoardingFragment){
                super.onBackPressed()
            }else{
                supportFragmentManager.beginTransaction().remove(supportFragmentManager.fragments.last()).commit()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if(FirebaseAuth.getInstance().currentUser?.email == Constants.DEFAULT_EMAIL){
            FirebaseAuth.getInstance().signOut()
        }
    }

    private fun setLanguage(lang : String){
        CURRENT_LANGUAGE = lang
        val locale = Locale(CURRENT_LANGUAGE)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        this.resources?.updateConfiguration(
            config,
            this.resources!!.displayMetrics
        )
    }
}