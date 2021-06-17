package com.example.fixx.NavigationBar.OrdersScreen.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.NavigationBar.OrdersScreen.viewModels.OrdersViewmodel
import com.example.fixx.POJOs.Job
import com.example.fixx.R
import com.example.fixx.databinding.FragmentOngoingOrdersBinding

class OngoingOrdersFragment : Fragment() {

    var jobs = arrayListOf<Job>()
    val jobsAdapter = OrdersAdapter(jobs,Job.JobStatus.OnRequest)
    val viewmodel : OrdersViewmodel by lazy {
        OrdersViewmodel(Job.JobStatus.OnRequest, onSuccessBinder = {    receivedJobs->
            jobs.addAll(receivedJobs)
            binding.onGoingProgressBar.visibility = View.INVISIBLE
            jobsAdapter.notifyDataSetChanged()
        },onFaiBinder = {
            binding.onGoingProgressBar.visibility = View.INVISIBLE
            Toast.makeText(context, context?.getString(R.string.OrderLoadingFailed),Toast.LENGTH_LONG).show()
        })
    }
    lateinit var binding: FragmentOngoingOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel
        jobsAdapter.deleteHandler = {   position ->
            viewmodel.deleteJob(jobs[position].jobId, onSuccessBinder = {
                jobs.removeAt(position)
                jobsAdapter.notifyDataSetChanged()
            }, onFaiBinder = {
                Toast.makeText(context,context?.getString(R.string.JobRemoveFail),Toast.LENGTH_SHORT).show()
            })
        }
    }

    override fun onStart() {
        super.onStart()
        jobs.clear()
        jobsAdapter.notifyDataSetChanged()
        viewmodel.loadData()

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
        binding.ongoingRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = jobsAdapter
        }
    }
}