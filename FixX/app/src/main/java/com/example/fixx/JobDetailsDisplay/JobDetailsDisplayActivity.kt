package com.example.fixx.JobDetailsDisplay

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.constants.Constants
import kotlinx.android.synthetic.main.activity_job_details_display.*
import kotlinx.android.synthetic.main.bidder_recycler_item.*


class JobDetailsDisplayActivity : AppCompatActivity() {

    private var jobType: ImageView? = null
    private var jobLocation: TextView? = null
    private var fromTime: TextView? = null
    private var toTime: TextView? = null
    private var date: TextView? = null
    private var completionDate: TextView? = null
    private var jobStatus: TextView? = null
    private var jobPrice: TextView? = null
    private var jobDesc: TextView? = null
    private var biddersRecyclerView: RecyclerView? = null
    private var biddersList: ArrayList<Technician>? = null
    private lateinit var biddersAdapter: BiddersAdapter


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details_display)
        supportActionBar?.hide()

        val jobObject: Job = intent.getSerializableExtra(Constants.TRANS_JOB) as Job

        jobType = findViewById(R.id.jobType)
        jobLocation = findViewById(R.id.jobLocation)
        fromTime = findViewById(R.id.fromTime)
        toTime = findViewById(R.id.toTime)
        date = findViewById(R.id.date)
        completionDate = findViewById(R.id.completionDate)
        jobStatus = findViewById(R.id.jobStatus)
        jobPrice = findViewById(R.id.jobPrice)
        jobDesc = findViewById(R.id.jobDesc)
        biddersRecyclerView = findViewById(R.id.biddersRecyclerView)

        biddersRecyclerView?.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        biddersAdapter = BiddersAdapter(biddersList as ArrayList<Technician>)
        biddersRecyclerView?.adapter = biddersAdapter

        jobType?.setImageResource(mapJobTypeToImage(jobObject.type)?: 0)
        jobLocation?.text = jobObject.location
        fromTime?.text = jobObject.fromTime
        toTime?.text = jobObject.toTime
        jobStatus?.text = jobObject.status.toString()
        jobPrice?.text = jobObject.price.toString()
        date?.text = jobObject.date
        completionDate?.text = jobObject.completionDate



        //As per job description...
        if(jobObject.description != null){
            jobDesc?.text = jobObject.description
            jobDesc?.movementMethod = ScrollingMovementMethod()
        }else{
            //jobDesc?.text = "No Available Order Description"
            descriptionHeading.visibility = View.GONE
            descriptionLinearLayout.visibility = View.GONE
            jobDesc?.visibility = View.GONE
        }



        //As per job attached images...
        if(jobObject.images != null){
            repeat(jobObject.images!!.size) {
                var imgString: String? = null
                var imgContainer: ImageView? = null
                val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                imgContainer?.layoutParams = params
                params.gravity = Gravity.CENTER
                imgContainer?.setImageResource(it)
                imagesScrollView.addView(imgContainer)
                jobObject.images
            }
        }else{
            imagesHeading.visibility = View.GONE
            imagesContainer.visibility = View.GONE
            imagesScrollView?.visibility = View.GONE
        }



        //As per job bidders...
        if(jobObject.bidders != null){
            jobObject.bidders!!.forEachIndexed { index, i ->
//                bidderAvatar.setImageResource(jobObject.bidders!![index].)
                bidderName.text = jobObject.techID.toString()
//                bidderRating.text =
//                bidderBid.text =
            }
        }else{
            biddersHeading.visibility = View.GONE
            biddersRecyclerView?.visibility = View.GONE
        }
    }

//    fun getTechnicianFromJobTechID(techID: String): Technician?{
//
//    }

    fun mapJobTypeToImage(jobType: String): Int? {
        return when(jobType){
            "Painter" -> R.drawable.painter
            "Plumber" -> R.drawable.plumber
            "Electrician" -> R.drawable.electrician
            "Carpenter" -> R.drawable.carpenter
            "Tiles_Handyman" -> R.drawable.tileshandyman
            "Parquet" -> R.drawable.parquet
            "Smith" -> R.drawable.smith
            "Decoration_Stones" -> R.drawable.masondecorationstones
            "Alumetal" -> R.drawable.alumetal
            "Air_Conditioner" -> R.drawable.airconditioner
            "Curtains" -> R.drawable.curtains
            "Glass" -> R.drawable.glass
            "Satellite" -> R.drawable.satellite
            "Gypsum_Works" -> R.drawable.gypsumworks
            "Marble" -> R.drawable.marbleandgranite
            "Pest_Control" -> R.drawable.pestcontrol
            "Wood_Painter" -> R.drawable.woodpainter
            "Swimming_pool" -> R.drawable.swimmingpool
            else -> null
        }
    }
}