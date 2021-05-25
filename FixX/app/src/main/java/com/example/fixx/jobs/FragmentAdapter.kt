package com.example.fixx.jobs

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.fixx.NavigationBar.OrdersScreen.views.AvailableOrdersFragment
import com.example.fixx.NavigationBar.OrdersScreen.views.CompletedOrdersFragment
import com.example.fixx.NavigationBar.OrdersScreen.views.OnGuaranteeOrdersFragment

class FragmentAdapter (private val fm : FragmentManager, private val context : Context, private val taps : Int)
    : FragmentPagerAdapter(fm,taps) {

    override fun getCount(): Int {
        return taps
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> onGoingJobsFragment()
            1 -> CompletedJobsFragment()
            2 -> AvailableJobsFragment()
            else -> Fragment()
        }
    }
}