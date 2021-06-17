package com.example.fixx.LoginScreen.Views

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.fixx.LoginScreen.Views.LoginTabFragment
import com.example.fixx.LoginScreen.Views.SignUpOnBoardingFragment

@Suppress("DEPRECATION")
class LoginAdapter: FragmentPagerAdapter {

    private var context: Context? = null
    private var fragments = listOf<Fragment>()

    constructor(fm: FragmentManager, context: Context?, fragments : List<Fragment>) : super(fm) {
        this.context = context
        this.fragments = fragments
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
}