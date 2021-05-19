package com.example.fixx.LoginScreen.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.fixx.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_registration.*

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

        fab_google.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Google", Toast.LENGTH_SHORT).show()
        })

        fab_facebook.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Facebook", Toast.LENGTH_SHORT).show()
        })

        fab_twitter.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Twitter", Toast.LENGTH_SHORT).show()
        })
    }
}