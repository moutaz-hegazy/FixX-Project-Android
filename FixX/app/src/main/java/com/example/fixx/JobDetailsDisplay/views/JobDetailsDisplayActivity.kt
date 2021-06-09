package com.example.fixx.JobDetailsDisplay.views

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.JobDetailsDisplay.viewModels.JobDetailsViewModel
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.POJOs.Comment
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.ActivityJobDetailsDisplayBinding
import com.example.fixx.inAppChatScreens.views.ChatLogActivity
import com.example.fixx.takeOrderScreen.views.CustomizeOrderActivity
import com.example.fixx.techOrderDetailsScreen.views.OrderImagesAdapter
import com.example.fixx.technicianProfileScreen.view.TechnicianProfileActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottom_sheet_pick.view.*
import kotlinx.android.synthetic.main.bottom_sheet_rating.view.*
import java.text.SimpleDateFormat
import java.util.*


class JobDetailsDisplayActivity : AppCompatActivity() {

    lateinit var  binding : ActivityJobDetailsDisplayBinding
    private var jobId : String? = null
    private lateinit var viewmodel : JobDetailsViewModel
    private lateinit var loadedJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        jobId = intent.getStringExtra(Constants.TRANS_JOB)

        val jobObject = intent.getSerializableExtra(Constants.TRANS_JOB_OBJECT) as? Job

        if(jobObject != null){
            viewmodel = JobDetailsViewModel(jobObject.jobId)
            displayJobOnScreen(jobObject)
        }else{
            jobId?.let {
                viewmodel = JobDetailsViewModel(it)
                viewmodel.fetchJobfromDB(onSuccessBinding = { job ->
                    displayJobOnScreen(job)
                }, onFailBinding = {

                })
            }
        }
    }


    private fun displayJobOnScreen(job : Job){
        loadedJob = job
        binding.jobDetailsJobImage.setImageResource(getImageResourse(job.type) ?: 0)
        binding.jobDetailsDateLbl.text = getDateFor(job.status, job)
        val locName = job.location?.substringBefore("%")
        if(!locName.isNullOrEmpty()){
            binding?.jobDetailsLocationLbl?.text = locName
        }else{
            binding?.jobDetailsLocationLbl?.text = job.location?.substringAfter("%")
        }
        binding.jobDetailsFromTimeLbl.text = job.fromTime
        binding.jobDetailsToTimeLbl.text = job.toTime
        binding.jobDetailsStatusLbl.text = job.status.rawValue
        binding.jobDisplayMenuBtn.setOnClickListener {
            showPopupMenu(it, job.status)
        }
        job.price?.let {
            binding.jobDetailsFinalPriceTitleLbl.visibility = View.VISIBLE
            binding.jobDetailsFinalPriceLbl.visibility = View.VISIBLE
            binding.jobDetailsFinalPriceLbl.text = "$it ${getString(R.string.LE)}"
        }
        if(!job.images.isNullOrEmpty()){
            binding.jobDetailsImagesTitleLbl.visibility = View.VISIBLE
            binding.jobDetailsImagesRecycler.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(applicationContext).apply {
                    orientation = RecyclerView.HORIZONTAL
                }
                adapter = OrderImagesAdapter(job.images!!.map { it.second })
            }
        }
        if(!job.description.isNullOrEmpty()){
            binding.jobDetailsDescTitleLbl.visibility = View.VISIBLE
            binding.jobDetailsDescriptionLbl.text = job.description
            binding.jobDetailsDescriptionLbl.visibility = View.VISIBLE
        }

        displayTechnicianData(job)
    }

    private fun displayTechnicianData(job : Job){
        if (job.techID != null) {
            loadSingleTechnician(job.techID!!){tech ->
                if(job.status == Job.JobStatus.Completed){
                    binding.jobDetailsTechRateBtn.apply {
                        if(job.rateable){
                            binding.jobDetailsTechRateBtn.setOnClickListener {
                                showBottomSheetDialog(tech,job.jobId)
                            }
                        }
                        visibility = View.VISIBLE
                    }
                }
            }
        } else if(!job.bidders.isNullOrEmpty()) {
            loadBidders(job)
        }
    }

    private fun loadBidders(job : Job){
        job.bidders?.let { map ->
            Log.i("TAG", "onCreate: Here 44 <<<<<<<<"+ job.privateRequest + job)
            if (job.privateRequest) {
                Log.i("TAG", "onCreate: Here 22 <<<<<<<<")
                map.keys.first().let { techUid ->
                    binding.jobDetailsTechLayout.visibility = View.VISIBLE
                    loadSingleTechnician(techUid) { tech->
                        loadPrivateTechnician(tech, map)
                    }
                }
            } else {
                Log.i("TAG", "onCreate: Here 33 <<<<<<<<")
                job.bidders?.let { bidders ->
                    loadAllBidders(bidders)
                }
            }
        }
    }

    private fun loadPrivateTechnician(tech : Technician , map : Map<String,String>){
        binding.bidderItemConfirmPriceTitleLbl.visibility = View.VISIBLE
        binding.bidderItemConfirmPriceLbl.text = "${map[tech.uid]} ${getString(R.string.LE)}"
        binding.bidderItemConfirmPriceLbl.visibility = View.VISIBLE
        binding.jobDetailsTechAcceptBtn.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                // Accept price.
                acceptTechnician(tech,map[tech.uid]!!)
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

    private fun loadAllBidders(bidders : Map<String,String>){
        binding.jobDetailsBiddersTitleLbl.visibility = View.VISIBLE
        binding.jobDetailsBiddersRecycler.apply {
            val techs = mutableListOf<Technician>()
            visibility = View.VISIBLE
            adapter = JobDetailsBiddersAdapter(techs, bidders){ tech,price ->
                acceptTechnician(tech,price)
                visibility = View.GONE
                binding.jobDetailsBiddersTitleLbl.visibility = View.GONE
                loadAcceptedTech(tech,price)
            }
            layoutManager = LinearLayoutManager(this@JobDetailsDisplayActivity)
            Log.i("TAG", "displayJobOnScreen: <<<<<<<<<<<"+bidders)
            bidders.forEach {
                viewmodel.getTechnician(it.key, onSuccessBinding = { tech ->
                    Log.i("TAG", "displayJobOnScreen: HERE 1")
                    techs.add(tech)
                    adapter?.notifyDataSetChanged()
                }, onFailBinding = {

                })
            }
        }
    }

    private fun loadAcceptedTech(tech:Technician, price: String? = null){
        if (tech.profilePicture != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.bidderItemTechImageView.clipToOutline = true
            }
            binding.bidderItemTechImageView.visibility = View.VISIBLE
            Picasso.get().load(tech.profilePicture?.second)
                .into(binding.bidderItemTechImageView)
        } else {
            binding.bidderItemTechImageLbl.visibility = View.VISIBLE
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

        binding.jobDetailsTechAcceptBtn.visibility = View.INVISIBLE
        binding.jobDetailsTechCancelBtn.visibility = View.INVISIBLE
        binding.bidderItemConfirmPriceLbl.visibility = View.INVISIBLE
        binding.bidderItemConfirmPriceTitleLbl.visibility = View.INVISIBLE

        binding.jobDetailsTechLayout.visibility = View.VISIBLE

        price?.let {
            binding.jobDetailsFinalPriceLbl.text = it
            binding.jobDetailsFinalPriceLbl.visibility = View.VISIBLE
            binding.jobDetailsFinalPriceTitleLbl.visibility = View.VISIBLE
        }
    }

    private fun loadSingleTechnician(uid : String, onLoadCompleted : ((Technician) -> Unit)? = null){
        viewmodel.getTechnician(uid, onSuccessBinding = { tech ->
            loadAcceptedTech(tech)
            onLoadCompleted?.let {
                it(tech)
            }
        }, onFailBinding = {

        })
    }

    private fun acceptTechnician(tech:Technician,price : String){
        viewmodel.setTechnicianUidWithPrice(tech.uid!!,
            price)
        viewmodel.sendAcceptNotification(tech.uid!!,tech.token!!, USER_OBJECT!!.name)
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


    private fun showPopupMenu(view : View,type: Job.JobStatus){
        val popupMenu = PopupMenu(this,view)
        popupMenu.apply {
            menuInflater.inflate(
                when(type){
                    Job.JobStatus.Completed ->R.menu.image_long_press_menu
                    Job.JobStatus.OnRequest ->R.menu.on_request_menu
                    Job.JobStatus.Accepted  ->R.menu.accepted_job_menu
                }
                , menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.image_menu_delete,
                    R.id.accepted_job_menu_delete,
                    R.id.onRequest_menu_delete  -> {
                        viewmodel.removeJob(onSuccessBinding = {
                            Toast.makeText(this@JobDetailsDisplayActivity, R.string.JobRemoved, Toast.LENGTH_SHORT).show()
                            finish()
                        },onFailBinding = {
                            Toast.makeText(this@JobDetailsDisplayActivity, R.string.JobRemoveFail, Toast.LENGTH_SHORT).show()
                        })
                        true
                    }

                    R.id.onRequest_menu_edit ->{
                        Intent(this@JobDetailsDisplayActivity, CustomizeOrderActivity::class.java).apply{
                            putExtra(Constants.TRANS_JOB_OBJECT, loadedJob)
                        }.also {
                            startActivity(it)
                        }
                        true
                    }

                    R.id.accepted_job_menu_extend ->{
                        // extend activity.
                        true
                    }

                    else -> false
                }
            }
            show()
        }
    }

    private fun showBottomSheetDialog(tech : Technician,jobId:String){
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet_rating, null)
        val dialog = BottomSheetDialog(this)
        bottomSheet.rootView.bootom_sheet_submit_btn.setOnClickListener {
            val rating = bottomSheet.rootView.bootom_sheet_ratingBar.rating
            if(rating == 0.0f){
                Toast.makeText(this,R.string.GiveRating, Toast.LENGTH_SHORT).show()
            }else{
                val comment = bottomSheet.rootView.bootom_sheet_comment_txt.text.toString()
                val commentValue = if(comment.isNullOrEmpty())null; else comment    //<<<<<<<<<<<< take care land mine
                val commentObj = Comment(USER_OBJECT!!.name,commentValue, USER_OBJECT?.profilePicture?.second
                    ,SimpleDateFormat("dd-MMM-YYYY").format(Calendar.getInstance().time),null
                    , System.currentTimeMillis(), rating.toDouble())

                val finalRating = ((rating.toDouble()-4)/2) + (tech.rating ?: 2.5)
                viewmodel.postRatingAndCommentToTechnician(jobId,tech.uid!!,finalRating,commentObj,0.0
                    ,onSuccessBinding = {
                        dialog.dismiss()
                    },onFailBinding = {
                        Toast.makeText(this, R.string.CommentFail,Toast.LENGTH_SHORT).show()
                    })
            }
        }
        dialog.setContentView(bottomSheet.rootView)
        dialog.show()
    }
}