package com.example.fixx.jobs.viewModels

import android.util.Log
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.Technician
import com.example.fixx.Support.FirestoreService

class JobsViewModel(val jobStatus : Job.JobStatus, val onSuccessBinder: (List<Job>)->Unit, val onFailBinder:()->Unit) {


    fun loadData(){
        when (jobStatus){
            Job.JobStatus.Completed -> fetchCompletedJobs()
            Job.JobStatus.OnRequest -> fetchAvaiableJobs()
            Job.JobStatus.Accepted-> fetchOngoingJobs()
        }
    }

    private fun fetchCompletedJobs(){
        Log.i("TAG", "fetchCompletedJobs: <<<<<<<<<<<<< Fetching...")
        FirestoreService.fetchMyCompletedWork(onSuccessBinder,onFailBinder)
    }

    private fun fetchOngoingJobs(){
        FirestoreService.fetchMyOngoingWork(onSuccessBinder,onFailBinder)
    }

    private fun fetchAvaiableJobs(){
        val tech = USER_OBJECT as? Technician
        tech?.let {
            FirestoreService.fetchAvailableWork(it.jobTitle!!,it.workLocations!!,onSuccessBinder,onFailBinder)
        }
    }
}