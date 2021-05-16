package com.example.fixx.showTechnicianScreen.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fixx.POJOs.Technician
import com.example.fixx.Support.FirestoreService



class RecyclerActivityViewModel(private val location : String?, private val jobType : String?) : ViewModel() {

    var recyclerListData = MutableLiveData<MutableList<Technician>>()
    var newList : MutableList<Technician> = mutableListOf()


    var userLocations = mutableListOf<String>()

    init {
        location?.let{
            jobType?.let {
                var x = FirestoreService.searchForTechnicianByJobAndLocation(jobType, location){
                    Log.i("TAG", " RECYCLER: $it ")
                    newList.addAll(it)
                    recyclerListData.value = newList
                }
                Log.i("TAG", "new list: ${newList.count()}")
            }
        }
    }

    fun add(technician: Technician ){
        newList.add(technician)
        recyclerListData.value = newList

    }
}