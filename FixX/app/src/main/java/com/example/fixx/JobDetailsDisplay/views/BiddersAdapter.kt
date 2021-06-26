package com.example.fixx.JobDetailsDisplay.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Technician
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.BidderRecyclerItemBinding
import com.example.fixx.inAppChatScreens.views.ChatLogActivity
import com.example.fixx.technicianProfileScreen.view.TechnicianProfileActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_job_details_display.view.*

class JobDetailsBiddersAdapter(val data : List<Technician>, val prices : Map<String,String>,
                               val onAcceptHandler : (Technician,String)-> Unit)
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

        if (data[position].profilePicture != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.binding.bidderItemTechImageView.clipToOutline = true
            }
            holder.binding.bidderItemTechImageView.visibility = View.VISIBLE
            Picasso.get().load(data[position].profilePicture?.second)
                .into(holder.binding.bidderItemTechImageView)
        } else {
            Log.i("TAG", "onBindViewHolder: <<<<<<<<<<<NNNNNNNNNNNNNNNNNNN >>>> ${data[position].name}")
            holder.binding.bidderItemTechImageLbl.visibility = View.VISIBLE
            holder.binding.bidderItemTechImageLbl.text =
                data[position].name.first().toUpperCase().toString()
        }

        holder.binding.bidderItemConfirmPriceLbl.text = prices[data[position].uid]
        holder.binding.bidderItemTechNameLbl.text = data[position].name
        holder.binding.bidderItemTechRating.rating = data[position].rating?.toFloat() ?: 0.0f

        holder.binding.bidderItemTechChatBtn.setOnClickListener {
            Intent(context, ChatLogActivity::class.java).apply {
                putExtra(Constants.TRANS_USERDATA, this@JobDetailsBiddersAdapter.data[position])
            }.also {
                context.startActivity(it)
            }
        }
        holder.binding.bidderItemTechCallBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${data[position].phoneNumber}")
            context.startActivity(intent)
        }

        holder.binding.bidderItemAcceptBtn.setOnClickListener {
            onAcceptHandler(data[position], prices[data[position].uid]!!)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context,TechnicianProfileActivity::class.java)
            intent.putExtra(Constants.TRANS_USERDATA,data[position])
            intent.putExtra(Constants.TRANS_RESPONSE_BOOL,true)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = data.size
}