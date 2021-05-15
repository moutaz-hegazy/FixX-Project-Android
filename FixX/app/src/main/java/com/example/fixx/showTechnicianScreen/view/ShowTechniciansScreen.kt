package com.example.fixx.showTechnicianScreen.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.R
import com.example.fixx.showTechnicianScreen.viewModel.RecyclerActivityViewModel
import com.example.fixx.showTechnicianScreen.viewModel.RecyclerViewModelFactory
import eg.gov.iti.jets.fixawy.POJOs.Technician

class ShowTechniciansScreen : AppCompatActivity() {

    private var viewManager = LinearLayoutManager(this)
    private lateinit var viewModel : RecyclerActivityViewModel
    private lateinit var techRecycler : RecyclerView
    private lateinit var adapter : RecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_technicians_screen)
        supportActionBar?.apply {
            title = "Plumber"
            setBackgroundDrawable(ColorDrawable(Color.BLACK))
        }

        techRecycler = findViewById(R.id.showTechniciansScreen_recyclerView)
        val application = requireNotNull(this).application
        val factory =
            RecyclerViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(RecyclerActivityViewModel::class.java)

        Log.i("TAG", "onCreate ShowTechniciansScreen : ${viewModel.newList}")
        initialiseAdapter()

    }

    private fun initialiseAdapter(){
        techRecycler.layoutManager = viewManager
        observeData()
    }

    fun observeData(){
        var progressBar : ProgressBar = findViewById(R.id.progressbar)

        viewModel.recyclerListData.observe(this, Observer{
            adapter = RecyclerAdapter(viewModel, it as MutableList<Technician> , this)
            adapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE
            techRecycler.adapter= adapter
        })
    }
}