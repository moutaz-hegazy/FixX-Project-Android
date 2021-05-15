package com.example.fixx

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

@Suppress("DEPRECATION")
class LoginAdapter: FragmentPagerAdapter {

    private var context: Context? = null
    var totalTabs = 0

    constructor(fm: FragmentManager, context: Context?, totalTabs: Int?) : super(fm) {
        this.context = context
        if (totalTabs != null) {
            this.totalTabs = totalTabs
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        var loginTabFragment = return when(position){
            0 -> LoginTabFragment()
            1 -> SignUpOnBoardingFragment()
            else -> Fragment()
        }
    }
}