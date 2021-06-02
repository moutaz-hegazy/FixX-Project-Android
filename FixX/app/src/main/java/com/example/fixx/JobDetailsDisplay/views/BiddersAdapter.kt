package com.example.fixx.JobDetailsDisplay.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Technician
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.BidderRecyclerItemBinding
import com.example.fixx.inAppChatScreens.views.ChatLogActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_job_details_display.view.*

class JobDetailsBiddersAdapter(val data : List<String>, val prices : Map<String,String>,
                               val loadTechnicianHandler : (techId : String,
                                                            onSuccessHandler : (tech : Technician)->Unit,
                                                            onFailHandler : ()->Unit) -> Unit)
        : RecyclerView.Adapter<JobDetailsBiddersAdapter.VH>() {
    lateinit var context: Context

    class VH(var binding: BidderRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context
        val binding =
            BidderRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {

        excuteHandler(data[position], onSuccessHandler = {  tech ->
            if (tech.profilePicture != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.binding.bidderItemTechImageView.clipToOutline = true
                }
                holder.binding.bidderItemTechImageView.visibility = View.VISIBLE
                Picasso.get().load(tech.profilePicture).into( holder.binding.bidderItemTechImageView)
            } else{
                holder.binding.bidderItemTechImageView.visibility = View.VISIBLE
                holder.binding.bidderItemTechImageLbl.text = tech.name.first().toUpperCase().toString()
            }

            holder.binding.bidderItemConfirmPriceLbl.text = prices[tech.uid]
            holder.binding.bidderItemTechImageLbl.text = tech.name
            holder.binding.bidderItemTechRating.rating = tech.rating?.toFloat() ?: 0.0f

            holder.binding.bidderItemTechChatBtn.setOnClickListener {
                Intent(context, ChatLogActivity::class.java).apply {
                    putExtra(Constants.TRANS_USERDATA, this@JobDetailsBiddersAdapter.data[position])
                }.also {
                    context.startActivity(it)
                }
            }
            holder.binding.bidderItemTechCallBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${tech.phoneNumber}")
                context.startActivity(intent)
            }
        },onFailHandler = {
            // failure code.
        })
    }

    private fun excuteHandler (techId : String, onSuccessHandler : (tech : Technician)->Unit,
                               onFailHandler : ()->Unit){
        loadTechnicianHandler(techId,onSuccessHandler,onFailHandler)
    }

    override fun getItemCount() = data.size
}