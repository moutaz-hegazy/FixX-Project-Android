package com.example.fixx.JobDetailsDisplay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Bidder
import com.example.fixx.R
import kotlinx.android.synthetic.main.activity_job_details_display.view.*

class BiddersAdapter(val bidderList: ArrayList<Bidder>) : RecyclerView.Adapter<BiddersAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(Bidder: Bidder) {
            var bidderAvatar = itemView.findViewById(R.id.bidderAvatar) as ImageView
            var bidderName = itemView.findViewById(R.id.bidderName) as TextView
            var bidderRating = itemView.findViewById(R.id.bidderRating) as TextView
            var bidderPrice = itemView.findViewById(R.id.bidderBid) as TextView

            bidderAvatar.setImageResource(Bidder.image)
            bidderName.text = Bidder.name
            bidderRating.text = Bidder.rating
            bidderPrice.text = Bidder.bid.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bidder_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BiddersAdapter.ViewHolder, position: Int) {
        holder.bindItems(bidderList[position])
    }

    override fun getItemCount(): Int {
        return bidderList.size
    }
}