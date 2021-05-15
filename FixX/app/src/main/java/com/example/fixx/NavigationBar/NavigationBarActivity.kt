package com.example.fixx.NavigationBar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.fixx.R
import com.example.project.bottom_navigation_fragments.HomeFragment
import com.example.project.bottom_navigation_fragments.OrdersFragment
import com.example.project.bottom_navigation_fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


 class NavigationBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_bar)
        val bottomnav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val homeFragment = HomeFragment()
        val settingsFragment = SettingsFragment()
        val ordersFragment = OrdersFragment()
        makeCurrentFragment(homeFragment)

        bottomnav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.toolbar_home_item -> makeCurrentFragment(homeFragment)
                R.id.toolbar_settings_item -> makeCurrentFragment(settingsFragment)
                R.id.toolbar_orders_item -> makeCurrentFragment(ordersFragment)
            }
            true
        }



    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
           replace(R.id.toolbar_framelayout,fragment)
            commit()
        }
}