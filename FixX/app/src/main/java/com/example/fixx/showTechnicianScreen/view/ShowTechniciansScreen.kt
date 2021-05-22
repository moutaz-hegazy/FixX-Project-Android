package com.example.fixx.showTechnicianScreen.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.showTechnicianScreen.viewModel.RecyclerActivityViewModel
import com.example.fixx.showTechnicianScreen.viewModel.RecyclerViewModelFactory
import com.example.fixx.takeOrderScreen.viewModels.CustomizeOrderViewModel
import com.example.fixx.technicianProfileScreen.view.TechnicianProfileActivity

import java.lang.IllegalArgumentException

class ShowTechniciansScreen : AppCompatActivity() {

    private var viewManager = LinearLayoutManager(this)
    private lateinit var viewModel : RecyclerActivityViewModel
    private lateinit var techRecycler : RecyclerView
    private lateinit var adapter : RecyclerAdapter

    private var serviceName : Int? = null
    private var job : Job? = null
    private var parsedJobLocation : String? = null
    val imagesUris = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_technicians_screen)

        serviceName = intent.getIntExtra(Constants.serviceName,-1)
        job = intent.getSerializableExtra(Constants.TRANS_JOB) as? Job
        //val images = intent.getParcelableArrayExtra(Constants.TRANS_IMAGES)
        //val imagesPaths = intent.getParcelableArrayExtra(Constants.TRANS_IMAGES_PATHS)
        val imagesPaths = intent.getStringArrayExtra(Constants.TRANS_IMAGES_PATHS)

        Log.i("TAG2", "onCreate: ${imagesPaths?.size} ")
        Log.i("TAG2", "onCreate: ${job?.location} ")
        /*parsedJobLocation = job?.location?.substringBefore(":")
        job?.location = parsedJobLocation
        Log.i("TAG2", "onCreate: $parsedJobLocation ")*/


        // get list of Uri instead of the received strings to upload
        imagesPaths?.forEach { image ->
            imagesUris.add(Uri.parse(image))
        }


        supportActionBar?.apply {
            title = getString(serviceName!!)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF6200EE")))
        }

        techRecycler = findViewById(R.id.showTechniciansScreen_recyclerView)
        val application = requireNotNull(this).application
        val factory = RecyclerViewModelFactory(job?.location,job?.type)
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
            adapter = RecyclerAdapter(it as MutableList<Technician> , this)

            adapter.bookTechnician = {
                job?.let {
                    CustomizeOrderViewModel(it, imagesUris) {
                        Toast.makeText(this, "Job Uploaded.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            adapter.showTechProfileHandler ={
                var toProfile = Intent(this, TechnicianProfileActivity::class.java)
                toProfile.putExtra("name", viewModel.newList.get(it).name)
                startActivity(toProfile)
            }

            adapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE
            techRecycler.adapter= adapter
        })
    }
}