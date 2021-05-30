package com.example.fixx.NavigationBar.OrdersScreen.views

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentsAdapter (private val fm : FragmentManager, private val context : Context, private val taps : Int)
    : FragmentPagerAdapter(fm,taps) {

    override fun getCount(): Int {
        return taps
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> OngoingOrdersFragment()
            1 -> CompletedOrdersFragment()
            else -> Fragment()
        }
    }
}