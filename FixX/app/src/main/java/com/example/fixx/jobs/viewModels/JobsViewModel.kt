package com.example.fixx.jobs.viewModels

import android.util.Log
import com.example.fixx.POJOs.Job
import com.example.fixx.Support.FirestoreService

class JobsViewModel(val jobStatus : Job.JobStatus, val onSuccessBinder: (List<Job>)->Unit, val onFailBinder:()->Unit) {


    fun loadData(){
        when (jobStatus){
            Job.JobStatus.Completed -> fetchCompletedJobs()
        }
    }

    private fun fetchCompletedJobs(){
        Log.i("TAG", "fetchCompletedJobs: <<<<<<<<<<<<< Fetching...")
        FirestoreService.fetchMyCompletedWork(onSuccessBinder,onFailBinder)
    }

    fun deleteJob(jobId:String, onSuccessBinder: () -> Unit, onFaiBinder: () -> Unit){
        FirestoreService.removeJob(jobId,onSuccessBinder,onFaiBinder)
    }
}