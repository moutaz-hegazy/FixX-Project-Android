package com.example.fixx.jobs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.NavigationBar.OrdersScreen.viewModels.OrdersViewmodel
import com.example.fixx.NavigationBar.OrdersScreen.views.OrdersAdapter
import com.example.fixx.POJOs.Job
import com.example.fixx.R
import com.example.fixx.databinding.FragmentCompletedJobsBinding
import com.example.fixx.jobs.viewModels.JobsViewModel


class CompletedJobsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    var jobs = arrayListOf<Job>()
    lateinit var binding: FragmentCompletedJobsBinding
    val jobsAdapter = JobsAdapter(jobs,Job.JobStatus.Completed)
    val viewmodel : JobsViewModel by lazy {
        JobsViewModel(Job.JobStatus.Completed, onSuccessBinder = {    receivedJobs->
            jobs.addAll(receivedJobs)
            binding.completedJobsProgressBar.visibility = View.INVISIBLE
            jobsAdapter.notifyDataSetChanged()
        },onFailBinder = {
            binding.completedJobsProgressBar.visibility = View.INVISIBLE
            Toast.makeText(context, context?.getString(R.string.OrderLoadingFailed), Toast.LENGTH_LONG).show()
        })
    }

    override fun onStart() {
        super.onStart()
        Log.i("TAG", "onStart: in completed.............")
        jobs.clear()
        jobsAdapter.notifyDataSetChanged()
        viewmodel.loadData()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel
        /*val job1 = Job("","Painter","Alexandria,elmandaraQebly,20th st", Job.JobStatus.Completed).apply {
            date = "13-june-2021"
        }
        val job2 = Job("","Parquet","Alexandria,elmandaraQebly,20th st", Job.JobStatus.Completed).apply {
            date = "15-june-2021"
            price = 200
        }
        val job3 = Job("","Electrician","Alexandria,elmandaraQebly,20th st",
            Job.JobStatus.Completed).apply {
            date = "10-june-2021"
            price = 350
        }
        jobs.addAll(arrayOf(job1,job2,job3))*/

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompletedJobsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.completedJobsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = OrdersAdapter(jobs, Job.JobStatus.Completed)
        }
    }

}
