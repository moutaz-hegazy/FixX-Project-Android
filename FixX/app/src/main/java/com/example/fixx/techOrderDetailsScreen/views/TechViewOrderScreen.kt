package com.example.fixx.techOrderDetailsScreen.views

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
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
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.ActivityTechViewOrderScreenBinding
import com.example.fixx.inAppChatScreens.views.ChatLogActivity
import com.example.fixx.techOrderDetailsScreen.models.ReplyNotificationData
import com.example.fixx.techOrderDetailsScreen.models.TechReplyPushNotification
import com.example.fixx.techOrderDetailsScreen.viewModels.TechViewOrderViewModel
import com.squareup.picasso.Picasso

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
            jobId = getStringExtra(Constants.TRANS_JOB)

        }

        jobId?.let {    jobID ->
            viewModel.fetchJobFromDB(jobID, onSuccessBinding = {    returnedJob->
                job = returnedJob
                viewModel.fetchUserFromDB(job?.uid) { person ->
                    contact = person

                    binding.techViewOrderChatBtn.setOnClickListener {
                        Intent(this, ChatLogActivity::class.java).apply {
                            putExtra(Constants.TRANS_USERDATA, contact)
                        }.also {
                            startActivity(it)
                        }
                    }

                    if(returnedJob.status == Job.JobStatus.OnRequest){
                        binding.techViewOrderConfirmPriceLayout.visibility = View.VISIBLE
                        binding.techViewOrderConfirmBtn.setOnClickListener {
                                if (binding.techViewOrderPriceTxt.text.isNullOrEmpty()) {
                                    binding.techViewOrderPriceTxt.error = "enter a price"
                                } else {
                                    job?.bidders?.put(USER_OBJECT!!.uid!!,binding.techViewOrderPriceTxt.text.toString())
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
                                            }
                                        }
                                    }
                                }
                        }
                    }else if(returnedJob.status == Job.JobStatus.Accepted){
                        binding.techViewOrderTotalAccountLayout.visibility = View.VISIBLE
                        binding.techViewOrderAccountLbl.text = returnedJob.price.toString()
                        binding.techViewOrderCompletedBtn.setOnClickListener {
                            viewModel.completeJob(returnedJob.jobId,onSuccessBinding = {
                                contact?.token?.let {token ->
                                    TechReplyPushNotification(
                                        ReplyNotificationData(
                                            Constants.NOTIFICATION_TYPE_JOB_COMPLETED,
                                            USER_OBJECT!!.name,R.string.JobCompletedTitle, R.string.JobCompletedMsg,
                                            returnedJob.jobId, binding.techViewOrderPriceTxt.text.toString()),
                                        arrayOf(token)).also {
                                        viewModel.sendReplyNotification(it)
                                        Toast.makeText(applicationContext,R.string.JobCompletedTitle,Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                }
                            },onFailBinding = {
                                Toast.makeText(applicationContext,R.string.JobStatusFail,Toast.LENGTH_SHORT).show()
                            })
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

                binding.techViewOrderDenyBtn.apply {
                    if(returnedJob.privateRequest && returnedJob.status == Job.JobStatus.OnRequest){
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
                                    viewModel.removeSelfFromBidders(jobID)
                                    this@TechViewOrderScreen.finish()
                                }
                            }
                        }
                    }else if(returnedJob.status == Job.JobStatus.Accepted){
                        visibility = View.VISIBLE
                        text = getString(R.string.CancelJob)
                        setOnClickListener {
                            cancelJobDialog(R.string.CancelJobTitle,R.string.CancelJobMsg){
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

                binding.techViewOrderAddressLbl.text = job?.location
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

                job?.images?.let { images ->
                    binding.techViewOrderImagesLbl.visibility = View.VISIBLE
                    binding.techViewOrderImagesRecycler.apply {
                        visibility = View.VISIBLE
                        layoutManager = LinearLayoutManager(applicationContext).apply {
                            orientation = RecyclerView.HORIZONTAL
                        }
                        adapter = OrderImagesAdapter(images.map { it.second })
                    }
                }
            }, onFailBinding = {

                finish()
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