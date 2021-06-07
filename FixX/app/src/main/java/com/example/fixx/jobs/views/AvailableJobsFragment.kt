package com.example.fixx.jobs.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.POJOs.Job
import com.example.fixx.R
import com.example.fixx.databinding.FragmentAvailableJobsBinding
import com.example.fixx.jobs.viewModels.JobsViewModel


class AvailableJobsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    var jobs = arrayListOf<Job>()
    lateinit var binding: FragmentAvailableJobsBinding
    val viewmodel: JobsViewModel by lazy {
        JobsViewModel(Job.JobStatus.OnRequest, onSuccessBinder = {
            jobs.addAll(it)
            binding.availableJobsProgressBar.visibility = View.INVISIBLE
            jobsAdapter.notifyDataSetChanged()
        },onFailBinder = {
            binding.availableJobsProgressBar.visibility = View.INVISIBLE
            Toast.makeText(context, R.string.JobsLoadingFailed, Toast.LENGTH_SHORT).show()
        })
    }
    val jobsAdapter = JobsAdapter(jobs, Job.JobStatus.OnRequest)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAvailableJobsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.availableJobsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = jobsAdapter
        }
    }


}