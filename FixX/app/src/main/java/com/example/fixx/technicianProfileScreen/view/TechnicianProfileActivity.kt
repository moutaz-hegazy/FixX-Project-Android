package com.example.fixx.technicianProfileScreen.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.POJOs.Comment
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.showTechnicianScreen.models.JobRequestData
import com.example.fixx.takeOrderScreen.viewModels.CustomizeOrderViewModel
import com.example.fixx.technicianProfileScreen.viewModel.TechnicianProfileViewModel
import com.example.fixx.technicianProfileScreen.viewModel.TechnicianProfileViewModelFactory
import com.squareup.picasso.Picasso
import java.lang.IllegalArgumentException

class TechnicianProfileActivity : AppCompatActivity() {

    private var viewManager = LinearLayoutManager(this)
    private lateinit var viewModel : TechnicianProfileViewModel
    private lateinit var techProfileRecycler : RecyclerView
    private lateinit var adapter : TechnicianProfileRecyclerAdapter
    private val customizeOrderViewmodel : CustomizeOrderViewModel by lazy {
        CustomizeOrderViewModel()
    }

    lateinit var image : ImageView
    lateinit var imageLbl : TextView
    var techName : TextView? = null
    var bookBtn : Button? = null
    var ratingBar : RatingBar? = null

    var technicianData : Technician? = null
    var imagesPaths : Array<String>? = null
    var job : Job? = null
    val imagesUris = mutableListOf<Uri>()
    private var editMode = false

    private lateinit var jobsCountLbl : TextView
    private lateinit var reviewsCountLbl : TextView

    private var jobUploaded = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_technician_profile)

        supportActionBar?.hide()

        technicianData = intent.getSerializableExtra(Constants.TRANS_USERDATA) as Technician
        job = intent.getSerializableExtra(Constants.TRANS_JOB) as? Job
        imagesPaths = intent.getStringArrayExtra(Constants.TRANS_IMAGES_PATHS) as? Array<String>
        val showOnly = intent.getBooleanExtra(Constants.TRANS_RESPONSE_BOOL,false)
        editMode = intent.getBooleanExtra(Constants.TRANS_EDIT_MODE,false)

        techName = findViewById(R.id.technician_profile_name_lbl)
        image = findViewById(R.id.technician_profile_img)
        imageLbl = findViewById(R.id.technician_profile_img_lbl)
        bookBtn = findViewById(R.id.technician_profile_book_btn)
        ratingBar = findViewById(R.id.technician_profile_ratingBar)
        jobsCountLbl = findViewById(R.id.technician_profile_number_of_jobs_lbl)
        reviewsCountLbl = findViewById(R.id.technician_profile_number_of_reviews_lbl)

        if(!showOnly){
            bookBtn?.visibility = View.VISIBLE
        }

        if (technicianData?.profilePicture != null){
            image.visibility = View.VISIBLE
            Picasso.get().load(technicianData?.profilePicture?.second).into(image)
        }
        else{
            imageLbl.visibility = View.VISIBLE
            imageLbl.text = technicianData?.name?.first()?.toUpperCase().toString()
        }
        techName?.text = technicianData?.name

        jobsCountLbl.text = technicianData?.jobsCount.toString()
        reviewsCountLbl.text = technicianData?.reviewCount.toString()

        bookBtn?.setOnClickListener {
            imagesPaths?.forEach { image ->
                imagesUris.add(Uri.parse(image))
            }

            job?.let { job ->
                job.privateRequest = true
                job.privateTechUid = technicianData?.uid
                if(editMode){
                    customizeOrderViewmodel.updateJob(job, imagesUris,
                        onSuccessBinding = {
                            Toast.makeText(this, R.string.JobUpdated, Toast.LENGTH_SHORT).show()
                            viewModel.sendNotification(
                                JobRequestData( Constants.NOTIFICATION_TYPE_USER_JOB_REQUEST,
                                    NavigationBarActivity.USER_OBJECT?.name ?: "",
                                    R.string.JobRequestTitle,R.string.SingleJobRequest, it.jobId
                                ))
                        }, onFailureBinding = {
                            Toast.makeText(this, R.string.JobUpdateFailed, Toast.LENGTH_SHORT).show()
                        })
                }else{
                    customizeOrderViewmodel.uploadJob(job, imagesUris,
                        onSuccessBinding = {
                            Toast.makeText(this, R.string.JobUploaded, Toast.LENGTH_SHORT).show()
                            viewModel.sendNotification(
                                JobRequestData( Constants.NOTIFICATION_TYPE_USER_JOB_REQUEST,
                                    NavigationBarActivity.USER_OBJECT?.name ?: "",
                                    R.string.JobRequestTitle,R.string.SingleJobRequest, it.jobId
                                ))
                        }, onFailureBinding = {
                            Toast.makeText(this, R.string.JobUploadFailed, Toast.LENGTH_SHORT).show()
                        })
                }
                jobUploaded = true
            }
            bookBtn?.isClickable = false
            bookBtn?.text = "Booked"
            bookBtn?.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_done_24,0)
            bookBtn?.setBackgroundColor(Color.GRAY)
        }

        ratingBar?.rating = technicianData?.rating?.toFloat() ?: 0F

        techProfileRecycler = findViewById(R.id.technician_profile_recyclerView)

        technicianData?.let {   tech ->
            val factory = TechnicianProfileViewModelFactory(tech){
                Toast.makeText(this,R.string.CommentLoadFail, Toast.LENGTH_SHORT).show()
            }
            try {
                viewModel = ViewModelProvider(this, factory).get(TechnicianProfileViewModel::class.java)
            }catch (error : IllegalArgumentException){
                Toast.makeText(applicationContext,"Couldn't connect to database", Toast.LENGTH_SHORT)
                finish()
            }
        }

        Log.i("TAG", "onCreate ShowTechniciansScreen : ${viewModel.newList}")
        initialiseAdapter()
    }

    private fun initialiseAdapter(){
        techProfileRecycler.layoutManager = viewManager
        observeData()
    }

    override fun onBackPressed() {
        if(jobUploaded){
            Intent().apply {
                putExtra(Constants.TECH_LIST_BOOLEAN, true)
            }.also {
                setResult(Activity.RESULT_OK, it)
                finish()
            }
        }else{
          super.onBackPressed()
        }
    }

    fun observeData(){

        viewModel.recyclerListData.observe(this, Observer{list ->
            technicianData?.let {
                adapter = TechnicianProfileRecyclerAdapter(it, list as MutableList<Comment> , this)
            }

            adapter.notifyDataSetChanged()
            techProfileRecycler.adapter= adapter
        })
    }
}