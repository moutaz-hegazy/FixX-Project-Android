package com.example.fixx.showTechnicianScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class RecyclerViewModelFactory(private val location : String?, private val jobType: String?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecyclerActivityViewModel::class.java)){
            return RecyclerActivityViewModel(location, jobType) as T
        }
        throw IllegalArgumentException("UnknownViewModel")
    }
}