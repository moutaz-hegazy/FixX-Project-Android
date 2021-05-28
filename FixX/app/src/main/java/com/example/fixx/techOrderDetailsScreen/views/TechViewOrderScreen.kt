package com.example.fixx.techOrderDetailsScreen.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

    lateinit var job : Job

    var contact : Person? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar.apply {
            title = "Job Details"
        }

        intent.apply {
            jobId = getStringExtra(Constants.TRANS_JOB)
        }

        jobId?.let {
            viewModel.fetchJobFromDB(it, onSuccessBinding = {
                job = it
                viewModel.fetchUserFromDB(job.uid){  person ->
                    contact = person

                    binding.techViewOrderChatBtn.setOnClickListener {
                        Intent(this,ChatLogActivity::class.java).apply {
                            putExtra(Constants.TRANS_USERDATA,contact)
                        }.also {
                            startActivity(it)
                        }
                    }

                    binding.techViewOrderConfirmBtn.setOnClickListener {
                        if(binding.techViewOrderPriceTxt.text.isNullOrEmpty()){
                            binding.techViewOrderPriceTxt.error = "enter a price"
                        }else{
                            contact?.token?.let {   token ->
                                TechReplyPushNotification(ReplyNotificationData(Constants.NOTIFICATION_TYPE_TECH_REPLY_CONFIRM,
                                    USER_OBJECT?.name ?: "",
                                    R.string.RequestConfirmed,
                                    R.string.ConfirmMessage,job.jobId, USER_OBJECT?.uid,
                                    binding.techViewOrderPriceTxt.text.toString()),
                                    arrayOf(token))
                            }
                        }
                    }

                    if (person?.profilePicture != null){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            binding.techViewOrderUserImageView.clipToOutline = true
                        }
                        binding.techViewOrderUserImageView.visibility = View.VISIBLE
                        Picasso.get().load(person.profilePicture).into( binding.techViewOrderUserImageView)
                    } else{
                        binding.techViewOrderUserImageLbl.visibility = View.VISIBLE
                        binding.techViewOrderUserImageLbl.text = person?.name?.first()?.toUpperCase().toString()
                    }
                    binding.techViewOrderUserNameLbl.text = person?.name
                }

                binding.techViewOrderDenyBtn.setOnClickListener {
                    contact?.token?.let {   token ->
                        TechReplyPushNotification(ReplyNotificationData(Constants.NOTIFICATION_TYPE_TECH_REPLY_DENY,
                            USER_OBJECT?.name ?: "",
                            R.string.RequestDenied,
                            R.string.DenyMessage,job.jobId),
                            arrayOf(token))
                    }
                }

                binding.techViewOrderDateLbl.text = job.date
                binding.techViewOrderFromTimeLbl.text = job.fromTime
                binding.techViewOrderToTimeLbl.text = job.toTime
                binding.techViewOrderDescriptionLbl.text = job.description

                job.images?.let { images ->
                    binding.techViewOrderImagesRecycler.apply {
                        visibility = View.VISIBLE
                        layoutManager = LinearLayoutManager(applicationContext).apply {
                            orientation = RecyclerView.HORIZONTAL
                        }
                        adapter = OrderImagesAdapter(images)
                    }
                }
            }, onFailBinding = {
                Log.i("TAG", "onCreate: Failed<<<<")
                finish()
            })
        }
    }
}