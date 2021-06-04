package com.example.fixx.jobs.viewModels

import com.example.fixx.POJOs.Job
import com.example.fixx.Support.FirestoreService

class JobsViewModel(val jobStatus : Job.JobStatus, val onSuccessBinder: (List<Job>)->Unit, val onFailBinder:()->Unit) {


    fun loadData(){
        when (jobStatus){
            Job.JobStatus.Completed -> fetchCompletedJobs()
        }
    }

    private fun fetchCompletedJobs(){
        FirestoreService.fetchMyCompletedWork(onSuccessBinder,onFailBinder)
    }
}