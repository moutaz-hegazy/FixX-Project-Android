package com.example.fixx.showTechnicianScreen.view

import android.app.Activity
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
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.showTechnicianScreen.models.JobRequestData
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

    private val customizeViewmodel : CustomizeOrderViewModel by lazy {
        CustomizeOrderViewModel()
    }

    private var serviceName : Int? = null
    private var job : Job? = null
    private var editMode = false
    val imagesUris = mutableListOf<Uri>()
    var imagesPaths = arrayOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_technicians_screen)

        serviceName = intent.getIntExtra(Constants.serviceName,-1)
        job = intent.getSerializableExtra(Constants.TRANS_JOB) as? Job
        imagesPaths = intent.getStringArrayExtra(Constants.TRANS_IMAGES_PATHS) as Array<String>
        editMode = intent.getBooleanExtra(Constants.TRANS_EDIT_MODE,false)

        Log.i("TAG2", "onCreate: ${imagesPaths?.size} ")
        Log.i("TAG2", "onCreate: ${job?.location} ")

        // get list of Uri instead of the received strings to upload
        imagesPaths?.forEach { image ->
            imagesUris.add(Uri.parse(image))
        }


        supportActionBar?.apply {
            title = getString(serviceName!!)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF6200EE")))
        }

        techRecycler = findViewById(R.id.showTechniciansScreen_recyclerView)
        val factory = RecyclerViewModelFactory(job?.areaLocation,job?.type)
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

        viewModel.recyclerListData.observe(this, Observer {
            adapter = RecyclerAdapter(it as MutableList<Technician>, this)

            adapter.bookTechnician = { position ->
                job?.let { job ->
                    job.privateRequest = true
                    job.privateTechUid = adapter.arrayList[position].uid
                    if(editMode){
                        customizeViewmodel.updateJob(job, imagesUris,
                            onSuccessBinding = {
                                Toast.makeText(this, "Job Uploaded.", Toast.LENGTH_SHORT).show()
                                viewModel.sendNotification(
                                    JobRequestData( Constants.NOTIFICATION_TYPE_USER_JOB_REQUEST,
                                        USER_OBJECT?.name ?: "",
                                        R.string.JobRequestTitle,R.string.SingleJobRequest, it.jobId
                                    ), position)
                            }, onFailureBinding = {
                                Toast.makeText(this, "Job Upload Failed.", Toast.LENGTH_SHORT).show()
                            })
                    }else{
                        customizeViewmodel.uploadJob(job, imagesUris,
                            onSuccessBinding = {
                                Toast.makeText(this, "Job Uploaded.", Toast.LENGTH_SHORT).show()
                                viewModel.sendNotification(
                                    JobRequestData( Constants.NOTIFICATION_TYPE_USER_JOB_REQUEST,
                                        USER_OBJECT?.name ?: "",
                                        R.string.JobRequestTitle,R.string.SingleJobRequest, it.jobId
                                    ), position)
                            }, onFailureBinding = {
                                Toast.makeText(this, "Job Upload Failed.", Toast.LENGTH_SHORT).show()
                            })
                    }
                    val returnIntent = Intent()
                    returnIntent.putExtra(Constants.TECH_LIST_BOOLEAN, true)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }

            adapter.showTechProfileHandler = {
                var toProfile = Intent(this, TechnicianProfileActivity::class.java)
                toProfile.putExtra("name", viewModel.newList[it].name)
                toProfile.putExtra(Constants.TRANS_USERDATA, viewModel.newList[it])
                toProfile.putExtra(Constants.TRANS_JOB, job)
                toProfile.putExtra(Constants.TRANS_IMAGES_PATHS, imagesPaths)
                toProfile.putExtra(Constants.TRANS_EDIT_MODE,editMode)
                toProfile.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                startActivityForResult(toProfile, Constants.TECH_DETAILS_REQUESTCODE)
            }

            adapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE
            techRecycler.adapter = adapter
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == Constants.TECH_DETAILS_REQUESTCODE){
            data?.getBooleanExtra(Constants.TECH_LIST_BOOLEAN , false)?.let {
                if(it){
                    val returnIntent = Intent()
                    returnIntent.putExtra(Constants.TECH_LIST_BOOLEAN,true)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }
        }
    }
}