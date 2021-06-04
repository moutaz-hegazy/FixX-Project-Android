package com.example.fixx.NavigationBar.OrdersScreen.viewModels

import com.example.fixx.POJOs.Job
import com.example.fixx.Support.FirestoreService

class OrdersViewmodel(val jobStatus : Job.JobStatus, val onSuccessBinder: (List<Job>)->Unit, val onFaiBinder:()->Unit) {


    fun loadData(){
        when (jobStatus){
            Job.JobStatus.Accepted,Job.JobStatus.OnRequest -> fetchOngoingJobs()
            Job.JobStatus.Completed -> fetchCompletedJobs()
        }
    }

    private fun fetchOngoingJobs(){
        FirestoreService.fetchMyOngoingOrderedJobs(onSuccessBinder,onFaiBinder)
    }

    private fun fetchCompletedJobs(){
        FirestoreService.fetchMyCompletedOrderedJobs(onSuccessBinder,onFaiBinder)
    }

    fun deleteJob(jobId:String, onSuccessBinder: () -> Unit, onFaiBinder: () -> Unit){
        FirestoreService.removeJob(jobId,onSuccessBinder,onFaiBinder)
    }
}