package com.example.fixx.JobDetailsDisplay

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Bidder
import com.example.fixx.POJOs.Job
import com.example.fixx.R


class JobDetailsDisplayActivity : AppCompatActivity() {

/*    private val biddersList = ArrayList<Bidder>()
    private lateinit var biddersAdapter: BiddersAdapter
    private var biddersRecyclerView = findViewById<RecyclerView>(R.id.biddersRecyclerView)*/

    private var dummyJob: Job? = null
    private var jobType: ImageView? = null
    private var jobLocation: TextView? = null
    private var fromTime: TextView? = null
    private var toTime: TextView? = null
    private var date: TextView? = null
    private var jobStatus: TextView? = null
    private var jobPrice: TextView? = null
    private var jobDesc: TextView? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details_display)
        supportActionBar?.hide()

        jobType = findViewById(R.id.jobType)
        jobLocation = findViewById(R.id.jobLocation)
        fromTime = findViewById(R.id.fromTime)
        toTime = findViewById(R.id.toTime)
        date = findViewById(R.id.date)
        jobStatus = findViewById(R.id.jobStatus)
        jobPrice = findViewById(R.id.jobPrice)
        jobDesc = findViewById(R.id.jobDesc)

        val biddersRecyclerView = findViewById<RecyclerView>(R.id.biddersRecyclerView)
        biddersRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        val biddersList = ArrayList<Bidder>()

        biddersList.add(Bidder(R.drawable.colored_avatar_user, "Bidder1","5/10", 1000))
        biddersList.add(Bidder(R.drawable.colored_avatar_user, "Bidder2","4.5/10", 2000))
        biddersList.add(Bidder(R.drawable.colored_avatar_user, "Bidder3","5/10", 3000))
        biddersList.add(Bidder(R.drawable.colored_avatar_user, "Bidder4","4.5/10", 4000))

        val adapter = BiddersAdapter(biddersList)

        biddersRecyclerView.adapter = adapter

//        imagesRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//        imagesAdapter = MoviesAdapter(movieList)
//        val mLayoutManager = LinearLayoutManager(applicationContext)
//        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
//        recyclerView.layoutManager = mLayoutManager
//        recyclerView.itemAnimator = DefaultItemAnimator()
//        recyclerView.adapter = moviesAdapter
//        prepareMovieData()

//        if (receivedBundle != null){
//
//        }

        //dummy job object created...
        dummyJob = Job("", "carpentry", "Alexandria", Job.JobStatus.OnRequest)
        dummyJob!!.description = "Job Description Job DescriptionJob DescriptionJob DescriptionJob DescriptionJob DescriptionJob DescriptionJob DescriptionJob DescriptionJob DescriptionJob DescriptionJob DescriptionJob DescriptionJob DescriptionJob DescriptionJob DescriptionJob Description"
        dummyJob!!.date = "Job date"
        dummyJob!!.fromTime = "Start"
        dummyJob!!.toTime = "finish"
        dummyJob!!.price = 1000
        //images : MutableList<Bitmap>? = null
        //bidders : MutableList<Int>? = null

        jobType?.setImageResource(R.drawable.carpenter)
        jobLocation?.text = dummyJob?.location
        jobStatus?.text = dummyJob?.status.toString()
        jobDesc?.text = dummyJob?.description
        jobPrice?.text = dummyJob?.price.toString()
        jobDesc?.movementMethod = ScrollingMovementMethod()
    }
}