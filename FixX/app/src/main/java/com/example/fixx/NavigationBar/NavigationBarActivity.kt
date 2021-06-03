package com.example.fixx.NavigationBar

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fixx.NavigationBar.OrdersScreen.views.OrdersFragment
import com.example.fixx.POJOs.Person
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.project.bottom_navigation_fragments.HomeFragment
import com.example.project.bottom_navigation_fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class NavigationBarActivity : AppCompatActivity() {

     companion object{
         var USER_OBJECT : Person? = null
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_bar)

        supportActionBar?.hide()

        val languageToLoad = getSharedPreferences(Constants.LANGUAGE_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .getString(Constants.CURRENT_LANGUAGE,"en")  ?: "en"// your language
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        this.getResources()?.updateConfiguration(
            config,
            this.getResources()!!.getDisplayMetrics()
        )


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
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.fragments[0] as? HomeFragment
        if(currentFragment != null){
            super.onBackPressed()
        }else{
            makeCurrentFragment(HomeFragment())
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
           replace(R.id.toolbar_framelayout,fragment)
            commit()
    }
}

