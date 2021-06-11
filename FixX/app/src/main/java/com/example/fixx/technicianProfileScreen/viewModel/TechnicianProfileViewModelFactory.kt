package com.example.fixx.technicianProfileScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fixx.POJOs.Technician
import com.example.fixx.databinding.OngoingJobRecyclerRowBinding
import java.lang.IllegalArgumentException


class TechnicianProfileViewModelFactory(val tech : Technician, val onFailBinding: ()->Unit) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TechnicianProfileViewModel::class.java)){
            return TechnicianProfileViewModel(tech.uid!!, tech.token, onFailBinding) as T
        }
        throw IllegalArgumentException("UnknownViewModel")
    }
}