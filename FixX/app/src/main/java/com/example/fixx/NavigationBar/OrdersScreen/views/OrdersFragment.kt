package com.example.fixx.NavigationBar.OrdersScreen.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fixx.R
import com.example.fixx.databinding.FragmentOrdersBinding
import com.google.android.material.tabs.TabLayout


class OrdersFragment : Fragment() {

    private lateinit var binding : FragmentOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.myOrdersTablayout.apply {
            newTab().setText(R.string.onGoingOrders).let {
                binding.myOrdersTablayout.addTab(it)
            }
            newTab().setText(R.string.completedOrders).let {
                binding.myOrdersTablayout.addTab(it)
            }
            tabGravity = TabLayout.GRAVITY_FILL
        }

        binding.myOrdersViewPager.apply {
            adapter = FragmentsAdapter(childFragmentManager,context,binding.myOrdersTablayout.tabCount)
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.myOrdersTablayout))
        }

        binding.myOrdersTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.myOrdersViewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

}