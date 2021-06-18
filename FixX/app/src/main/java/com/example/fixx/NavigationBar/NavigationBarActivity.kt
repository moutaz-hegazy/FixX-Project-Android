package com.example.fixx.NavigationBar

import android.app.usage.ConfigurationStats
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.fixx.JobDetailsDisplay.views.JobDetailsDisplayActivity
import com.example.fixx.NavigationBar.OrdersScreen.views.OrdersFragment
import com.example.fixx.NavigationBar.viewmodels.NavBarViewmodel
import com.example.fixx.POJOs.Person
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.Support.PushNotificationReceiver
import com.example.fixx.constants.Constants
import com.example.fixx.inAppChatScreens.views.ChatLogActivity
import com.example.fixx.techOrderDetailsScreen.views.TechViewOrderScreen
import com.example.project.bottom_navigation_fragments.HomeFragment
import com.example.project.bottom_navigation_fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ListenerRegistration
import java.util.*


class NavigationBarActivity : AppCompatActivity() {

    companion object{
        var USER_OBJECT : Person? = null
        var CURRENT_LANGUAGE = "en"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.i("TAG", "onCreate: >>>>>>>>>> TOKEN >>>>>> ${USER_OBJECT?.token}")
        val languageToLoad = getSharedPreferences(Constants.LANGUAGE_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .getString(Constants.CURRENT_LANGUAGE,"en")  ?: "en"// your language
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        this.resources?.updateConfiguration(
            config,
            this.resources!!.displayMetrics
        )

        if (intent.getBooleanExtra(Constants.TRANSIT_FROM_NOTIFICATION, false)) {
            NavBarViewmodel().fetchUser(onCompletionBinder = { person ->
                USER_OBJECT = person
                when (intent.getStringExtra(Constants.TRANS_NOTIFICATION_TYPE)) {
                    Constants.NOTIFICATION_TYPE_JOB_COMPLETED ->
                        navigateToActivity(JobDetailsDisplayActivity::class.java,intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
                    Constants.NOTIFICATION_TYPE_TECH_REPLY_CANCEL ->
                        navigateToActivity(JobDetailsDisplayActivity::class.java,intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
                    Constants.NOTIFICATION_TYPE_TECH_REPLY_DENY ->
                        navigateToActivity(JobDetailsDisplayActivity::class.java,intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
                    Constants.NOTIFICATION_TYPE_TECH_REPLY_CONFIRM ->
                        navigateToActivity(JobDetailsDisplayActivity::class.java,intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
                    Constants.NOTIFICATION_TYPE_USER_JOB_REQUEST ->
                        navigateToActivity(TechViewOrderScreen::class.java,intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
                    Constants.NOTIFICATION_TYPE_USER_ACCEPT ->
                        navigateToActivity(TechViewOrderScreen::class.java,intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
                    Constants.NOTIFICATION_TYPE_CHAT_MESSAGE ->
                        navigateToActivity(ChatLogActivity::class.java,intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
                    else -> {}
                }
            })
        }

        setContentView(R.layout.activity_navigation_bar)
        supportActionBar?.hide()

        val bottomnav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val homeFragment = HomeFragment()
        val settingsFragment = SettingsFragment()
        val ordersFragment = OrdersFragment()
        makeCurrentFragment(homeFragment)

        bottomnav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.toolbar_home_item ->{
                    makeCurrentFragment(homeFragment)

                }
                R.id.toolbar_settings_item ->{
                    makeCurrentFragment(settingsFragment)
                }
                R.id.toolbar_orders_item -> makeCurrentFragment(ordersFragment)
            }

            true
        }

    }

    private fun navigateToActivity(activity : Class<*>, bundle : Bundle?){
        Intent(this,activity).apply {
            putExtra(Constants.TRANS_DATA_BUNDLE,bundle)
        }.also {
            startActivity(it)
        }
    }


    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.fragments[0] as? HomeFragment
        if(currentFragment != null){
            super.onBackPressed()
        }else{
            makeCurrentFragment(HomeFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG", "onDestroy: Destroyed <<<<<<<<<<<<<<<")
    }

    private fun makeCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
           replace(R.id.toolbar_framelayout,fragment)
            commit()
    }
}

