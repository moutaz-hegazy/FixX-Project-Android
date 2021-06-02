package com.example.fixx.technicianProfileScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fixx.POJOs.Technician
import java.lang.IllegalArgumentException


class TechnicianProfileViewModelFactory(val tech : Technician) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TechnicianProfileViewModel::class.java)){
            return TechnicianProfileViewModel(tech) as T
        }
        throw IllegalArgumentException("UnknownViewModel")
    }
}