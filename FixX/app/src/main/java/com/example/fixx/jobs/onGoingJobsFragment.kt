package com.example.fixx.jobs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.POJOs.Job
import com.example.fixx.R
import com.example.fixx.databinding.FragmentOnGoingJobsBinding


class onGoingJobsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    var jobs = arrayListOf<Job>()

    lateinit var binding: FragmentOnGoingJobsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val job1 = Job("","Painter","Alexandria,elmandaraQebly,20th st",Job.JobStatus.OnRequest).apply {
            date = "13-june-2021"
        }
        val job2 = Job("","Parquet","Alexandria,elmandaraQebly,20th st",Job.JobStatus.Accepted).apply {
            date = "15-june-2021"
            price = 200
        }
        val job3 = Job("","Electrician","Alexandria,elmandaraQebly,20th st",Job.JobStatus.Accepted).apply {
            date = "10-june-2021"
            price = 350
        }
        jobs.addAll(arrayOf(job1,job2,job3))

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
            adapter = JobsAdapter(jobs,Job.JobStatus.Accepted)
        }
    }



}