package com.example.fixx.LoginScreen.Views

import android.app.Application
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.CURRENT_LANGUAGE
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT_OBSERVER
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.*
import com.google.firebase.auth.FacebookAuthProvider.*
import com.google.firebase.messaging.FirebaseMessaging
import java.math.BigDecimal
import java.util.*

@Suppress("DEPRECATION")
class RegistrationActivity : AppCompatActivity(){

    var callbackManager: CallbackManager? = null
    var firebaseAuth: FirebaseAuth? = null
//    var mAppEventsLogger: AppEventsLogger? = null

    var alpha = 0F

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    var google: FloatingActionButton? = null
    var facebook: FloatingActionButton? = null
    var twitter: FloatingActionButton? = null
    var dummy: LoginButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLanguage(CURRENT_LANGUAGE)

        setContentView(R.layout.activity_registration)

        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()

        initializeFacebook(application)

//        FacebookSdk.sdkInitialize(applicationContext)
//        AppEventsLogger.newLogger(applicationContext)
//        AppEventsLogger.activateApp(this)

//        mAppEventsLogger = AppEventsLogger.newLogger(applicationContext)
//        mAppEventsLogger?.logPurchase(BigDecimal.valueOf(4.99), Currency.getInstance("USD"))


        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired


        tabLayout = findViewById(R.id.myOrders_tablayout)
        viewPager = findViewById(R.id.myOrders_viewPager)

        google = findViewById(R.id.fab_google)
        facebook = findViewById(R.id.fab_facebook)
        twitter = findViewById(R.id.fab_twitter)
        dummy = findViewById(R.id.dummy)

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
            FirestoreService.signInWithGoogle(this)
        })

        twitter?.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Twitter", Toast.LENGTH_SHORT).show()
        })

        dummy?.setOnClickListener(View.OnClickListener {
            callbackManager = CallbackManager.Factory.create()
            dummy?.setReadPermissions("email", "public_profile", "")
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult?>{
                    override fun onSuccess(loginResult: LoginResult?) {
                        startActivity(Intent(applicationContext, FacebookActivity::class.java))
//                        var credential: AuthCredential = getCredential(accessToken.token)
//                        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
//                            OnCompleteListener<AuthResult> {
//                                if(it.isSuccessful){
//                                    var fbUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
//                                    startActivity(Intent(applicationContext, FacebookActivity::class.java))
//                                }else{
//                                    Toast.makeText(applicationContext, "Authentication Failed", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                        }
                    }

                    override fun onCancel() {
                        Toast.makeText(applicationContext, "Facebook Login Cancelled", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(exception: FacebookException?) {
                        Toast.makeText(applicationContext, "Facebook Login Failed", Toast.LENGTH_SHORT).show()
                        Log.i("FACEBOOK", "onError: $exception")
                    }
                })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)//Facebook Login...
        FirestoreService.googleSignInRequestResult(requestCode, data, onSuccessHandler = {
            email ->
            FirestoreService.checkIfEmailExists(email){
                exists ->
                if(exists){
                    FirestoreService.fetchUserFromDB(onCompletion = { person ->
                        USER_OBJECT = person
                    },passRegister = {
                        USER_OBJECT_OBSERVER = it
                    })

                    FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                        FirestoreService.updateDocumentField(Constants.USERS_COLLECTION,"token",token,FirestoreService.auth.uid!!)
                        startActivity(Intent(this, NavigationBarActivity::class.java))
                        finish()
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
                onSuccessHandler = {}, onFailHandler = {},passRegister = {})
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

    private fun initializeFacebook(application: Application) {
        FacebookSdk.sdkInitialize(application)
        AppEventsLogger.activateApp(application, application.getString(R.string.facebook_app_id))
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