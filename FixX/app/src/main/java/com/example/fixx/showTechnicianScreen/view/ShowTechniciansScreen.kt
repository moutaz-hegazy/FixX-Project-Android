package com.example.fixx.showTechnicianScreen.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.showTechnicianScreen.viewModel.RecyclerActivityViewModel
import com.example.fixx.showTechnicianScreen.viewModel.RecyclerViewModelFactory

import java.lang.IllegalArgumentException

class ShowTechniciansScreen : AppCompatActivity() {

    private var viewManager = LinearLayoutManager(this)
    private lateinit var viewModel : RecyclerActivityViewModel
    private lateinit var techRecycler : RecyclerView
    private lateinit var adapter : RecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_technicians_screen)

        val location = intent.getStringExtra(Constants.LOCATION_TO_TECH)
        val jobType = intent.getStringExtra(Constants.JOB_TYPE_TO_TECH)
        val serviceName = intent.getIntExtra(Constants.serviceName,-1)
        supportActionBar?.apply {
            title = getString(serviceName)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF6200EE")))
        }

        techRecycler = findViewById(R.id.showTechniciansScreen_recyclerView)
        val application = requireNotNull(this).application
        val factory = RecyclerViewModelFactory(location,jobType)
        try {
            viewModel = ViewModelProvider(this, factory).get(RecyclerActivityViewModel::class.java)
        }catch (error : IllegalArgumentException){
            Toast.makeText(applicationContext,"Couldn't connect to database",Toast.LENGTH_SHORT)
            finish()
        }

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