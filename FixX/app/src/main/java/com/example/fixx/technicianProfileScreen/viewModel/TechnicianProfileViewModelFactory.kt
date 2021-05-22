package com.example.fixx.technicianProfileScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException


class TechnicianProfileViewModelFactory : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TechnicianProfileViewModel::class.java)){
            return TechnicianProfileViewModel() as T
        }
        throw IllegalArgumentException("UnknownViewModel")
    }
}