package com.example.fixx
//
//import android.app.Application
//import android.content.res.Configuration
//import android.content.res.Resources
//import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.getLocale
//import java.util.*
//
//class App : Application() {
//
//    override fun onCreate() {
//        super.onCreate()
//        setLocale()
//    }
//
//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        setLocale()
//    }
//
//    private fun setLocale() {
//        val resources: Resources = resources
//        val configuration: Configuration = resources.configuration
//        val locale: Locale = getLocale(this)
//        if (configuration.locale != locale) {
//            configuration.setLocale(locale)
//            resources.updateConfiguration(configuration, null)
//        }
//    }
//}