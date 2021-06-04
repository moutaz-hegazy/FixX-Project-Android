package com.example.fixx.jobs.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fixx.R
import com.example.fixx.databinding.ActivityJobsBinding
import com.google.android.material.tabs.TabLayout

class JobsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJobsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobsBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        supportActionBar?.hide()

      //  binding = ActivityJobsBinding.inflate(inflater, container, false)

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
            adapter = FragmentAdapter(getSupportFragmentManager() ,context,binding.myJobsTablayout.tabCount)
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
