package com.example.fixx.techOrderDetailsScreen.views

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.POJOs.Job
import com.example.fixx.POJOs.Person
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.ActivityTechViewOrderScreenBinding
import com.example.fixx.inAppChatScreens.views.ChatLogActivity
import com.example.fixx.techOrderDetailsScreen.models.ReplyNotificationData
import com.example.fixx.techOrderDetailsScreen.models.TechReplyPushNotification
import com.example.fixx.techOrderDetailsScreen.viewModels.TechViewOrderViewModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.reflect.jvm.internal.impl.renderer.ClassifierNamePolicy

class TechViewOrderScreen : AppCompatActivity() {

    val binding by lazy {
        ActivityTechViewOrderScreenBinding.inflate(layoutInflater)
    }

    val viewModel by lazy {
        TechViewOrderViewModel()
    }
    var jobId : String? = null

    var job : Job? = null

    var contact : Person? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar.apply {
            title = getString(R.string.JobDetails)
        }

        intent.apply {
            val bundle = getBundleExtra(Constants.TRANS_DATA_BUNDLE)
            jobId = bundle?.getString(Constants.TRANS_JOB)
            Log.i("TAG", "onCreate: >>>>>>>>>>>>>>>. $bundle<<<<<<<<<<,,")
            job = getSerializableExtra(Constants.TRANS_JOB_OBJECT) as? Job
        }

        if(job != null){
            displayJobDetails(job!!)
        }else{
            jobId?.let {    jobID ->
                viewModel.fetchJobFromDB(jobID, onSuccessBinding = {    returnedJob->
                    displayJobDetails(returnedJob)
                }, onFailBinding = {

                    finish()
                })
            }
        }
    }


    private fun displayJobDetails(passedJob : Job){
        this.job = passedJob
        configurebottomLayout()
        fetchAndDisplayUserData()
        configureDenyButton()

        binding.techViewOrderAddressLbl.text = job?.location?.substringAfter("%")
        binding.techViewOrderDateLbl.text = job?.date
        binding.techViewOrderFromTimeLbl.text = job?.fromTime
        binding.techViewOrderToTimeLbl.text = job?.toTime
        if(job?.description.isNullOrEmpty()){
            binding.techViewOrderDescTitleLbl.text = getString(R.string.No_Description)
            binding.techViewOrderDescTitleLbl.setTextColor(Color.GRAY)
        }else{
            binding.techViewOrderDescriptionLbl.apply {
                visibility = View.VISIBLE
                text = job?.description
            }
        }

        if(job?.images?.isNullOrEmpty() == false) {
            binding.techViewOrderImagesLbl.visibility = View.VISIBLE
            binding.techViewOrderImagesRecycler.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(applicationContext).apply {
                    orientation = RecyclerView.HORIZONTAL
                }
                adapter = OrderImagesAdapter(job?.images!!.map { it.second })
            }
        }
    }

    private fun configureDenyButton(){
        if (job!!.privateRequest && job?.status == Job.JobStatus.OnRequest) {
            applyDenySettings()
        } else if (job?.status == Job.JobStatus.Accepted) {
            applyCancelSettings()
        }
    }

    private fun applyCancelSettings(){
        binding.techViewOrderDenyBtn.apply {
            visibility = View.VISIBLE
            text = getString(R.string.CancelJob)
            setOnClickListener {
                cancelJobDialog(R.string.CancelJobTitle, R.string.CancelJobMsg) {
                    contact?.token?.let { token ->
                        TechReplyPushNotification(
                            ReplyNotificationData(
                                Constants.NOTIFICATION_TYPE_TECH_REPLY_CANCEL,
                                USER_OBJECT?.name ?: "",
                                R.string.RequestCanceled,
                                R.string.CancelMessage, job!!.jobId
                            ),
                            arrayOf(token)
                        ).also {
                            viewModel.sendReplyNotification(it)
                        }
                        viewModel.canceledJob(job!!.jobId)
                        this@TechViewOrderScreen.finish()
                    }
                }
            }
        }
    }

    private fun applyDenySettings(){
        binding.techViewOrderDenyBtn.apply {
            visibility = View.VISIBLE
            setOnClickListener{
                cancelJobDialog (R.string.Deny, R.string.DenyDialogMsg) {
                    contact?.token?.let { token ->
                        TechReplyPushNotification(
                            ReplyNotificationData(
                                Constants.NOTIFICATION_TYPE_TECH_REPLY_DENY,
                                USER_OBJECT?.name ?: "",
                                R.string.RequestDenied,
                                R.string.DenyMessage, job!!.jobId
                            ),
                            arrayOf(token)
                        ).also {
                            viewModel.sendReplyNotification(it)
                        }
                        viewModel.removeBidders(job!!.jobId)
                        this@TechViewOrderScreen.finish()
                    }
                }
            }
        }
    }

    private fun fetchAndDisplayUserData(){
        viewModel.fetchUserFromDB(job?.uid) { person ->
            contact = person

            binding.techViewOrderChatBtn.setOnClickListener {
                Intent(this, ChatLogActivity::class.java).apply {
                    putExtra(Constants.TRANS_USERDATA, contact)
                }.also {
                    startActivity(it)
                }
            }
            if (person?.profilePicture != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.techViewOrderUserImageView.clipToOutline = true
                }
                binding.techViewOrderUserImageView.visibility = View.VISIBLE
                Picasso.get().load(person.profilePicture?.second)
                    .into(binding.techViewOrderUserImageView)
            } else {
                binding.techViewOrderUserImageLbl.visibility = View.VISIBLE
                binding.techViewOrderUserImageLbl.text =
                    person?.name?.first()?.toUpperCase().toString()
            }
            binding.techViewOrderUserNameLbl.text = person?.name
        }
    }

    private fun configurebottomLayout(){
        when {
            job?.bidders?.keys?.contains(USER_OBJECT!!.uid) == true -> displayBidderInfoBottom()
            job?.status == Job.JobStatus.OnRequest -> displayBiddingDetailsBottom()
            job?.status == Job.JobStatus.Accepted -> displayCompleteJobBottom()
        }
    }

    private fun displayBidderInfoBottom(){
        binding.techViewOrderBidFactLayout.visibility = View.VISIBLE
        binding.techViewBiddingValueLbl.text = job?.bidders?.get(USER_OBJECT?.uid)
        binding.techViewOrderCancelBiddingBtn.setOnClickListener {
            cancelJobDialog(R.string.CancelBidding, R.string.CancelBiddingMsg){
                job?.bidders?.remove(USER_OBJECT?.uid)
                viewModel.removeSelfFromBidders(job!!.jobId,job!!.bidders!!,onSuccessBinding = {
                    finish()
                },onFailBinding = {
                    Toast.makeText(this,R.string.FailedToRemoveBidder,Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

    private fun displayBiddingDetailsBottom(){
        binding.techViewOrderConfirmPriceLayout.visibility = View.VISIBLE
        binding.techViewOrderConfirmBtn.setOnClickListener {
            if (binding.techViewOrderPriceTxt.text.isNullOrEmpty()) {
                binding.techViewOrderPriceTxt.error = "enter a price"
            } else {
                if(job?.bidders == null){
                    job?.bidders = mutableMapOf(USER_OBJECT!!.uid!! to binding.techViewOrderPriceTxt.text.toString())
                }else{
                    job?.bidders?.put(USER_OBJECT!!.uid!!,binding.techViewOrderPriceTxt.text.toString())
                }
                viewModel.addToBidders(job!!.jobId, job!!.bidders!!) {
                    contact?.token?.let { token ->
                        TechReplyPushNotification(
                            ReplyNotificationData(
                                Constants.NOTIFICATION_TYPE_TECH_REPLY_CONFIRM,
                                USER_OBJECT?.name ?: "",
                                R.string.RequestConfirmed,
                                R.string.ConfirmMessage, job?.jobId!!,
                                binding.techViewOrderPriceTxt.text.toString()
                            ),
                            arrayOf(token)
                        ).also {
                            viewModel.sendReplyNotification(it)
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun displayCompleteJobBottom(){
        binding.techViewOrderTotalAccountLayout.visibility = View.VISIBLE
        binding.techViewOrderAccountLbl.text = job?.price.toString()
        binding.techViewOrderCompletedBtn.setOnClickListener {
            binding.techViewOrderCompletedBtn.apply {
                isClickable = false
                setBackgroundColor(Color.GRAY)
            }
            val currentDate = SimpleDateFormat("dd-MMM-YYYY").format(Calendar.getInstance().time)
            val newRating = (((USER_OBJECT!! as Technician).rating ?: 2.5) + 4)/2
            val jobCount = (USER_OBJECT!! as Technician).jobsCount++
            val increase = ((job?.price?.toDouble()?:0.0) * 0.1) + (((job?.price?.toDouble()?:0.0)*(0.8))/5)
            val monthlyRating = ((USER_OBJECT!! as Technician).monthlyRating?.toDouble() ?: 0.0) + increase
            viewModel.completeJob(job!!.jobId,currentDate,onSuccessBinding = {
                contact?.token?.let {token ->
                    TechReplyPushNotification(
                        ReplyNotificationData(
                            Constants.NOTIFICATION_TYPE_JOB_COMPLETED,
                            USER_OBJECT!!.name,R.string.JobCompletedTitle, R.string.JobCompletedMsg,
                            job!!.jobId, binding.techViewOrderPriceTxt.text.toString()),
                        arrayOf(token)).also {
                        viewModel.sendReplyNotification(it)
                        Toast.makeText(applicationContext,R.string.JobCompletedTitle,Toast.LENGTH_SHORT).show()
                    }
                }
                viewModel.updateRatingAndJobCount(USER_OBJECT!!.uid!!,newRating,monthlyRating.roundToInt(),jobCount,
                    onSuccessBinding = {
                        finish()
                    },onFailBinding = {})
            },onFailBinding = {
                Toast.makeText(applicationContext,R.string.JobStatusFail,Toast.LENGTH_SHORT).show()
                binding.techViewOrderCompletedBtn.apply {
                    isClickable = true
                    setBackgroundColor(Color.BLUE)
                }
            })
        }
    }

    private fun cancelJobDialog(title : Int,message:Int,onAcceptHandler:()->Unit){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(getString(title))
        //set message for alert dialog
        builder.setMessage(getString(message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(getString(R.string.yes)){ _, _ ->
            onAcceptHandler()
        }
        //performing negative action
        builder.setNegativeButton(getString(R.string.no)){ _, _ ->

        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
        alertDialog.window!!.setBackgroundDrawableResource(R.drawable.btn_border)

        val positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        with(positiveButton) {
            setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        val negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        with(negativeButton) {
            setTextColor(ContextCompat.getColor(context, R.color.green))
        }

    }
}