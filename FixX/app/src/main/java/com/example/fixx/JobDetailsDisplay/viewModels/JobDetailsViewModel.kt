package com.example.fixx.JobDetailsDisplay.viewModels

import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.Technician
import com.example.fixx.Support.FirestoreService

class JobDetailsViewModel(private val jobId : String, private val onSuccessBinding : (job : Job)->Unit,
                          private val onFailBinding : () -> Unit) {
    init {
        FirestoreService.fetchJobById(jobId, onSuccessBinding, onFailBinding)
    }

    fun getTechnician(techId : String, onSuccessBinding: (tech: Technician) -> Unit, onFailBinding: () -> Unit){
        FirestoreService.fetchUserFromDB(techId){   person ->
            val tech = person as? Technician
            if(tech != null){
                onSuccessBinding(tech)
            }else{
                onFailBinding()
            }
        }
    }
}