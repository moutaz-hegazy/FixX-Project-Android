package com.example.fixx.NavigationBar

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fixx.NavigationBar.OrdersScreen.views.OrdersFragment
import com.example.fixx.POJOs.Person
import com.example.fixx.R
import com.example.project.bottom_navigation_fragments.HomeFragment
import com.example.project.bottom_navigation_fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ContextUtils.getActivity


class NavigationBarActivity : AppCompatActivity() {

     companion object{
         var USER_OBJECT : Person? = null
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    override fun onBackPressed() {
        val homeFragment = HomeFragment()
        val bottomnav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

      //  if(bottomnav.selectedItemId eq)

      //  val currentFragment = defaultNavigator.currentDestination




//        val f: Fragment =
//            getActivity(this).getFragmentManager().findFragmentById(R.id.home_fragment)
//

        if(bottomnav.selectedItemId == R.id.toolbar_home_item)
        {
            super.onBackPressed()
        }
        makeCurrentFragment(homeFragment)
       // bottomnav.menu.getItem(1).setChecked(true)
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
           replace(R.id.toolbar_framelayout,fragment)
            commit()
        }


}

