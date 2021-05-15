package com.example.fixx.showTechnicianScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class RecyclerViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecyclerActivityViewModel::class.java)){
            return RecyclerActivityViewModel() as T
        }
        throw IllegalArgumentException("UnknownViewModel")
    }
}