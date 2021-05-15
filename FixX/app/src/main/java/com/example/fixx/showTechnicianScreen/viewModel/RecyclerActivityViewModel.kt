package com.example.fixx.showTechnicianScreen.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fixx.Support.FirestoreService
import eg.gov.iti.jets.fixawy.POJOs.Technician


class RecyclerActivityViewModel : ViewModel() {

    var recyclerListData = MutableLiveData<MutableList<Technician>>()
    var newList : MutableList<Technician> = mutableListOf()

    var userLocations = mutableListOf<String>()

    init {
        var x = FirestoreService.searchForTechnicianByJobAndLocation("job1", "alex"){
            Log.i("TAG", " RECYCLER: $it ")
            newList.addAll(it)
            recyclerListData.value = newList
        }
        Log.i("TAG", "new list: ${newList.count()}")
    }

    fun add(technician: Technician ){
        newList.add(technician)
        recyclerListData.value = newList

    }


}