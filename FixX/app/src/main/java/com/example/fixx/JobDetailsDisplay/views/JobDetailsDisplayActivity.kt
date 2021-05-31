package com.example.fixx.JobDetailsDisplay.views

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.JobDetailsDisplay.viewModels.JobDetailsViewModel
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.ActivityJobDetailsDisplayBinding
import com.example.fixx.inAppChatScreens.views.ChatLogActivity
import com.example.fixx.techOrderDetailsScreen.views.OrderImagesAdapter
import com.example.fixx.technicianProfileScreen.view.TechnicianProfileActivity
import com.squareup.picasso.Picasso


class JobDetailsDisplayActivity : AppCompatActivity() {

    lateinit var  binding : ActivityJobDetailsDisplayBinding
    private var jobId : String? = null
    private lateinit var viewmodel : JobDetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobId = intent.getStringExtra(Constants.TRANS_JOB)

        jobId?.let {
            viewmodel= JobDetailsViewModel(it, onSuccessBinding = { job ->
                binding.jobDetailsJobImage.setImageResource(getImageResourse(job.type) ?: 0)
                binding.jobDetailsDateLbl.text = getDateFor(job.status, job)
                binding.jobDetailsLocationLbl.text = job.location
                binding.jobDetailsFromTimeLbl.text = job.fromTime
                binding.jobDetailsToTimeLbl.text = job.toTime
                binding.jobDetailsStatusLbl.text = job.status.rawValue
                job.price?.let {
                    binding.jobDetailsFinalPriceTitleLbl.visibility = View.VISIBLE
                    binding.jobDetailsFinalPriceLbl.visibility = View.VISIBLE
                    binding.jobDetailsFinalPriceLbl.text = "$it ${getString(R.string.LE)}"
                }
                job.images?.let { images ->
                    binding.jobDetailsImagesTitleLbl.visibility = View.VISIBLE
                    binding.jobDetailsImagesRecycler.apply {
                        visibility = View.VISIBLE
                        layoutManager = LinearLayoutManager(applicationContext).apply {
                            orientation = RecyclerView.HORIZONTAL
                        }
                        adapter = OrderImagesAdapter(images)
                    }
                }
                if(!job.description.isNullOrEmpty()){
                    binding.jobDetailsDescTitleLbl.visibility = View.VISIBLE
                    binding.jobDetailsDescriptionLbl.text = job.description
                    binding.jobDetailsDescriptionLbl.visibility = View.VISIBLE
                }

                if (job.techID != null) {
                    Log.i("TAG", "onCreate: Here 11 <<<<<<<<")
                    loadSingleTechnician(job.techID!!)
                } else {
                    job.bidders?.let { map ->
                        Log.i("TAG", "onCreate: Here 44 <<<<<<<<"+ job.privateRequest + job)
                        if (job.privateRequest) {
                            Log.i("TAG", "onCreate: Here 22 <<<<<<<<")
                            map.keys.first().let { techUid ->
                                binding.jobDetailsTechLayout.visibility = View.VISIBLE
                                loadSingleTechnician(techUid) { tech->
                                    binding.bidderItemConfirmPriceTitleLbl.visibility = View.VISIBLE
                                    binding.bidderItemConfirmPriceLbl.text = "${map[techUid]} ${getString(R.string.LE)}"
                                    binding.bidderItemConfirmPriceLbl.visibility = View.VISIBLE
                                    binding.jobDetailsTechAcceptBtn.apply {
                                        visibility = View.VISIBLE
                                        setOnClickListener {
                                            // Accept price.
                                            viewmodel.setTechnicianUidWithPrice(techUid,
                                                map[techUid] ?: "")
                                            viewmodel.sendAcceptNotification(techUid,tech.token!!, USER_OBJECT!!.name)
                                            this.visibility = View.INVISIBLE
                                            binding.jobDetailsTechCancelBtn.visibility = View.INVISIBLE
                                        }
                                    }
                                    binding.jobDetailsTechCancelBtn.apply {
                                        visibility = View.VISIBLE
                                        setOnClickListener {
                                            viewmodel.removeSingleBidder()
                                            binding.jobDetailsTechLayout.visibility = View.INVISIBLE
                                            // show dialog Edit or delete job.

                                        }
                                    }
                                }
                            }
                        } else {
                            Log.i("TAG", "onCreate: Here 33 <<<<<<<<")
                            job.bidders?.let { bidders ->
                                binding.jobDetailsBiddersRecycler.apply {
                                    visibility = View.VISIBLE
                                    adapter = JobDetailsBiddersAdapter(
                                        bidders.keys.toList(),
                                        bidders
                                    ) { uid, onSuccess, onFail ->
                                        viewmodel.getTechnician(uid, onSuccess, onFail)
                                    }
                                }
                            }
                        }
                    }
                }

            }, onFailBinding = {

            })
        }
    }

    private fun loadSingleTechnician(uid : String, onLoadCompleted : ((Technician) -> Unit)? = null){
        viewmodel.getTechnician(uid, onSuccessBinding = { tech ->
            if (tech.profilePicture != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.bidderItemTechImageView.clipToOutline = true
                }
                binding.bidderItemTechImageView.visibility = View.VISIBLE
                Picasso.get().load(tech.profilePicture)
                    .into(binding.bidderItemTechImageView)
            } else {
                binding.bidderItemTechImageView.visibility = View.VISIBLE
                binding.bidderItemTechImageLbl.text =
                    tech.name.first().toUpperCase().toString()
            }
            binding.bidderItemTechNameLbl.text = tech.name
            binding.bidderItemTechRating.rating = tech.rating?.toFloat() ?: 0f
            binding.jobDetailsTechLayout.setOnClickListener {
                Intent(this, TechnicianProfileActivity::class.java).apply {
                    putExtra(Constants.TRANS_RESPONSE_BOOL, true)
                    putExtra(Constants.TRANS_USERDATA,tech)
                }.also {
                    startActivity(it)
                }
            }
            binding.bidderItemTechChatBtn.setOnClickListener {
                Intent(this, ChatLogActivity::class.java).apply {
                    putExtra(Constants.TRANS_USERDATA, tech)
                }.also {
                    startActivity(it)
                }
            }
            binding.bidderItemTechCallBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${tech.phoneNumber}")
                startActivity(intent)
            }

            onLoadCompleted?.let {
                it(tech)
            }
        }, onFailBinding = {

        })
    }

    private fun getDateFor(status: Job.JobStatus, job: Job) : String{
        return when(status){
            Job.JobStatus.Completed -> job.completionDate
            else -> job.date
        }
    }

    private fun getImageResourse(type: String) : Int?{
        return when(type){
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