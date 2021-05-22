package com.example.fixx.NavigationBar.OrdersScreen.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Job
import com.example.fixx.R
import com.example.fixx.databinding.FragmentOngoingOrdersBinding

class OngoingOrdersFragment : Fragment() {

    var jobs = arrayListOf<Job>()

    lateinit var binding: FragmentOngoingOrdersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOngoingOrdersBinding.inflate(inflater, container, false)
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

        binding.ongoingRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = OrdersAdapter(jobs,Job.JobStatus.OnRequest)
        }
    }
}