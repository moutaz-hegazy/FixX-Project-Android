package com.example.fixx.jobs.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.example.fixx.NavigationBar.OrdersScreen.views.FragmentsAdapter
import com.example.fixx.R
import com.example.fixx.databinding.FragmentJobsBinding
import com.example.project.bottom_navigation_fragments.HomeFragment
import com.google.android.material.tabs.TabLayout

/*
class jobsFragment : Fragment() {

    private lateinit var binding : FragmentJobsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
//
//            fragmentManager?.beginTransaction()?.replace(R.id.jobs_fragment, HomeFragment())?.commit()
//
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentJobsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.myJobsTablayout.apply {
            newTab().setText(R.string.onGoingJobs).let {
                binding.myJobsTablayout.addTab(it)
            }

            newTab().setText(R.string.completedJobs).let {
                binding.myJobsTablayout.addTab(it)
            }

            newTab().setText(R.string.availableJobs).let {
                binding.myJobsTablayout.addTab(it)
            }
            tabGravity = TabLayout.GRAVITY_FILL
        }

        binding.myJobsViewPager.apply {
            adapter = FragmentAdapter(childFragmentManager,context,binding.myJobsTablayout.tabCount)
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.myJobsTablayout))
        }

        binding.myJobsTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.myJobsViewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }









    }
*/
