package com.example.fixx.jobs.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
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
        JobsViewModel(Job.JobStatus.Completed, onSuccessBinder = { receivedJobs ->
            Log.i("TAG", "JobReceived >>>>>>>>>> : $receivedJobs ")
            jobs.addAll(receivedJobs)
            binding.completedJobsProgressBar.visibility = View.INVISIBLE
            jobsAdapter.notifyDataSetChanged()
        }, onFailBinder = {
            binding.completedJobsProgressBar.visibility = View.INVISIBLE
            Toast.makeText(
                context,
                context?.getString(R.string.OrderLoadingFailed),
                Toast.LENGTH_LONG
            ).show()
        })
    }

    override fun onStart() {
        super.onStart()
        Log.i("TAG", "onStart: >>>>>>>>> 77")
        jobs.clear()
        jobsAdapter.notifyDataSetChanged()
        viewmodel.loadData()
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
            adapter = jobsAdapter
        }
    }

}
