package com.example.fixx.LoginScreen.Views

import android.app.job.JobScheduler
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import com.example.fixx.services.LogoutService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

@Suppress("DEPRECATION")
class RegistrationActivity : AppCompatActivity(){

    var alpha = 0F

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    var google: FloatingActionButton? = null
    var facebook: FloatingActionButton? = null
    var twitter: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        supportActionBar?.hide()

        tabLayout = findViewById(R.id.myOrders_tablayout)
        viewPager = findViewById(R.id.myOrders_viewPager)

        google = findViewById(R.id.fab_google)
        facebook = findViewById(R.id.fab_facebook)
        twitter = findViewById(R.id.fab_twitter)

        google?.setImageResource(R.drawable.google)
        facebook?.setImageResource(R.drawable.facebook)
        twitter?.setImageResource(R.drawable.twitter)

        google?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.google))
        facebook?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.facebook))
        twitter?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.twitter))

        tabLayout?.newTab()?.setText(R.string.login)?.let {
            tabLayout?.addTab(it)
        }
        tabLayout?.newTab()?.setText(R.string.sign_up)?.let {
            tabLayout?.addTab(it)
        }

        tabLayout?.tabGravity = TabLayout.GRAVITY_FILL
        var viewPager = findViewById<ViewPager>(R.id.myOrders_viewPager)
        var adapter: LoginAdapter = LoginAdapter(supportFragmentManager, context = applicationContext, totalTabs = tabLayout?.tabCount)
        viewPager?.adapter = adapter
        viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        google?.translationY = 300F
        facebook?.translationY = 300F
        twitter?.translationY = 300F
        tabLayout?.translationX = 800F

        google?.alpha = alpha
        facebook?.alpha = alpha
        twitter?.alpha = alpha
        tabLayout?.alpha = alpha

        google?.animate()?.translationY(0F)?.alpha(1F)?.setDuration(1000)?.setStartDelay(600)?.start()
        facebook?.animate()?.translationY(0F)?.alpha(1F)?.setDuration(1000)?.setStartDelay(400)?.start()
        twitter?.animate()?.translationY(0F)?.alpha(1F)?.setDuration(1000)?.setStartDelay(800)?.start()
        tabLayout?.animate()?.translationX(0F)?.alpha(1F)?.setDuration(1000)?.setStartDelay(800)?.start()

        google?.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Google", Toast.LENGTH_SHORT).show()
            FirestoreService.signInWithGoogle(this)
        })

        facebook?.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Facebook", Toast.LENGTH_SHORT).show()
        })

        twitter?.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Twitter", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        FirestoreService.googleSignInRequestResult(requestCode, data, onSuccessHandler = {
            email ->
            FirestoreService.checkIfEmailExists(email){
                exists ->
                if(exists){
                    FirestoreService.fetchUserFromDB{ person ->
                        USER_OBJECT = person
                        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                            FirestoreService.updateDocumentField(Constants.USERS_COLLECTION,"token",token,person!!.uid!!)
                            USER_OBJECT?.token = token
                            startActivity(Intent(this, NavigationBarActivity::class.java))
                            finish()
                        }
                    }
                }
                else{
                    Toast.makeText(this, "I AM HEEEEEEEEEEERE", Toast.LENGTH_SHORT).show()
                    // sign up activity to enter extra data (phone, type, username)
                }
            }
        }, onFailHandler = {
            Toast.makeText(this, "Failed to login", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser == null){
            FirestoreService.loginWithEmailAndPassword(Constants.DEFAULT_EMAIL,Constants.DEFAULT_PASSWORD,
                onSuccessHandler = {}, onFailHandler = {})
        }
    }

    override fun onStop() {
        super.onStop()
        if(FirebaseAuth.getInstance().currentUser?.email == Constants.DEFAULT_EMAIL){
            FirebaseAuth.getInstance().signOut()
        }
    }

}