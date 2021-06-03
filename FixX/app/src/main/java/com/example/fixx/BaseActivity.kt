//package com.example.fixx
//
//import android.content.Context
//import androidx.appcompat.app.AppCompatActivity
//import com.example.fixx.BaseActivity
//import android.content.SharedPreferences
//import android.preference.PreferenceManager
//import java.util.*
//
//class BaseActivity : AppCompatActivity() {
//    private var mCurrentLocale: Locale? = null
//
//    override fun onStart() {
//        super.onStart()
//        mCurrentLocale = resources.configuration.locale
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//        val locale = getLocale(this)
//        if (locale != mCurrentLocale) {
//            mCurrentLocale = locale
//            recreate()
//        }
//    }
//
//    companion object {
//        fun getLocale(context: Context?): Locale {
//            val sharedPreferences =
//                PreferenceManager.getDefaultSharedPreferences(context)
//            var lang = sharedPreferences.getString("language", "en")
//            when (lang) {
//                "English" -> lang = "en"
//                "Spanish" -> lang = "es"
//            }
//            return Locale(lang)
//        }
//    }
//}