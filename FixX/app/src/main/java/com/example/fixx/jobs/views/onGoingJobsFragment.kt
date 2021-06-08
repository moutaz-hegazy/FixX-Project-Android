package com.example.fixx.jobs.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.POJOs.Job
import com.example.fixx.R
import com.example.fixx.databinding.FragmentOnGoingJobsBinding
import com.example.fixx.jobs.viewModels.JobsViewModel


class onGoingJobsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    var jobs = arrayListOf<Job>()

    lateinit var binding: FragmentOnGoingJobsBinding
    val viewmodel: JobsViewModel by lazy {
        JobsViewModel(Job.JobStatus.Accepted, onSuccessBinder = {
            jobs.addAll(it)
            binding.onGoingJobsProgressBar.visibility = View.INVISIBLE
            jobsAdapter.notifyDataSetChanged()
        },onFailBinder = {
            binding.onGoingJobsProgressBar.visibility = View.INVISIBLE
            Toast.makeText(context, R.string.JobsLoadingFailed,Toast.LENGTH_SHORT).show()
        })
    }
    var jobsAdapter: JobsAdapter = JobsAdapter(jobs,Job.JobStatus.Accepted)

    override fun onStart() {
        super.onStart()
        Log.i("TAG", "onStart: TEST <<<<<<<< 11")
        jobs.clear()
        jobsAdapter.notifyDataSetChanged()
        viewmodel.loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnGoingJobsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.ongoingJobsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = jobsAdapter
        }
    }
}