package com.example.fixx.NavigationBar.OrdersScreen.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.NavigationBar.OrdersScreen.viewModels.OrdersViewmodel
import com.example.fixx.POJOs.Job
import com.example.fixx.R
import com.example.fixx.databinding.FragmentCompletedOrdersBinding

class CompletedOrdersFragment : Fragment() {

    var jobs = arrayListOf<Job>()

    lateinit var binding: FragmentCompletedOrdersBinding
    var jobFDetailsAdapter = OrdersAdapter(jobs,Job.JobStatus.Completed)

    val viewmodel : OrdersViewmodel by lazy {
        OrdersViewmodel(Job.JobStatus.Completed, onSuccessBinder = {    receivedJobs->
            binding.completedOrdersProgressBar.visibility = View.INVISIBLE
            jobs.addAll(receivedJobs)
            jobFDetailsAdapter.notifyDataSetChanged()
        },onFaiBinder = {
            binding.completedOrdersProgressBar.visibility = View.INVISIBLE
            Toast.makeText(context, context?.getString(R.string.OrderLoadingFailed), Toast.LENGTH_LONG).show()
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel
        jobFDetailsAdapter.deleteHandler = { position ->
            viewmodel.deleteJob(jobs[position].jobId, onSuccessBinder = {
                jobs.removeAt(position)
                jobFDetailsAdapter.notifyItemRemoved(position)
            }, onFaiBinder = {
                Toast.makeText(
                    context,
                    context?.getString(R.string.JobRemoveFail),
                    Toast.LENGTH_SHORT
                ).show()
            })
        }
    }

    override fun onStart() {
        super.onStart()
        jobs.clear()
        jobFDetailsAdapter.notifyDataSetChanged()
        viewmodel.loadData()

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

        binding.completedRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = jobFDetailsAdapter
        }
    }
}