package com.example.fixx.NavigationBar.OrdersScreen.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.JobDetailsDisplay.JobDetailsDisplayActivity
import com.example.fixx.POJOs.Job
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.FragmentCompletedOrdersBinding

class CompletedOrdersFragment : Fragment() {

    var jobs = arrayListOf<Job>()

    lateinit var binding: FragmentCompletedOrdersBinding
    var jobFDetailsAdapter = OrdersAdapter(jobs,Job.JobStatus.Completed)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompletedOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.completedRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = jobFDetailsAdapter
        }

        jobFDetailsAdapter.showJobDetailsHandler = {
            var intent = Intent(context, JobDetailsDisplayActivity::class.java)
            intent.putExtra(Constants.TRANS_JOB,)
        }
    }
}