package com.example.fixx.technicianProfileScreen.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fixx.POJOs.Comment

class TechnicianProfileViewModel : ViewModel() {
    var recyclerListData = MutableLiveData<MutableList<Comment>>()
    var newList : MutableList<Comment> = mutableListOf()

    init {
        newList = mutableListOf(
            Comment("noha", "aaaaaaaaaaaaaaaaaaaaaaaaaaa"),
                Comment("noha tany", "bbbbbbbbbbbbb"),
                Comment("yomna", "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"),
                Comment("noha tany2", "wwwwwwwwwwwwwwwwwwwwwwwwwwwww"),
                Comment("noha tany3", "bbbbbbbbbbbbb"),
                Comment("noha tany3", "bbbbbbbbbbbbb"),
                Comment("noha tany3", "bbbbbbbbbbbbb")
        )
        recyclerListData.value = newList
    }
}