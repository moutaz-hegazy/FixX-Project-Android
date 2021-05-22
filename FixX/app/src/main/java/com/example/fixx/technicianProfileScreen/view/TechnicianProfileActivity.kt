package com.example.fixx.technicianProfileScreen.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Comment
import com.example.fixx.R
import com.example.fixx.technicianProfileScreen.viewModel.TechnicianProfileViewModel
import com.example.fixx.technicianProfileScreen.viewModel.TechnicianProfileViewModelFactory
import java.lang.IllegalArgumentException

class TechnicianProfileActivity : AppCompatActivity() {

    private var viewManager = LinearLayoutManager(this)
    private lateinit var viewModel : TechnicianProfileViewModel
    private lateinit var techProfileRecycler : RecyclerView
    private lateinit var adapter : TechnicianProfileRecyclerAdapter

    lateinit var image : ImageView
    var techName : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_technician_profile)

        Log.i("TAG", "onCreate: TECH PROFILE::   ${intent.getStringExtra("name")}")
        techName = findViewById(R.id.technician_profile_name_lbl)
        techName?.text = intent.getStringExtra("name")

        supportActionBar?.hide()

        image = findViewById(R.id.technician_profile_img)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image.clipToOutline = true
        }

        techProfileRecycler = findViewById(R.id.technician_profile_recyclerView)
        val application = requireNotNull(this).application
        val factory = TechnicianProfileViewModelFactory()
        try {
            viewModel = ViewModelProvider(this, factory).get(TechnicianProfileViewModel::class.java)
        }catch (error : IllegalArgumentException){
            Toast.makeText(applicationContext,"Couldn't connect to database", Toast.LENGTH_SHORT)
            finish()
        }

        Log.i("TAG", "onCreate ShowTechniciansScreen : ${viewModel.newList}")
        initialiseAdapter()
    }

    private fun initialiseAdapter(){
        //techProfileRecycler.setHasFixedSize(true)
        techProfileRecycler.layoutManager = viewManager
        observeData()
    }

    fun observeData(){

        viewModel.recyclerListData.observe(this, Observer{
            adapter = TechnicianProfileRecyclerAdapter(it as MutableList<Comment> , this)

            adapter.notifyDataSetChanged()
            techProfileRecycler.adapter= adapter
        })
    }
}