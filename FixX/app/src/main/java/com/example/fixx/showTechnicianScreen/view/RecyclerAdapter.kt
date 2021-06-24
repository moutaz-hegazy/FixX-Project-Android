package com.example.fixx.showTechnicianScreen.view

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.databinding.TechnicianItemBinding
import com.squareup.picasso.Picasso


class RecyclerAdapter(val arrayList: MutableList<Technician>, val context: Context) : RecyclerView.Adapter<RecyclerAdapter.TechViewHolder>() {
    lateinit var bookTechnician : (postion : Int)->Unit
    lateinit var showTechProfileHandler : (Int)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechViewHolder {
        var root = TechnicianItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TechViewHolder(root)
    }

    override fun getItemCount(): Int {
        if (arrayList.size==0){
            Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show()
        }
        return arrayList.size
    }

    override fun onBindViewHolder(holder: TechViewHolder, position: Int) {
        var name :TextView = holder.binding.technicianItemNameLbl
        var image : ImageView = holder.binding.technicianItemImg
        var imageLbl : TextView = holder.binding.technicianItemImgLbl
        var ratingBar : RatingBar = holder.binding.bidderItemTechRating
        var jobsCountLbl : TextView = holder.binding.technicianItemDoneJobsLbl
        var reviewsLbl : TextView = holder.binding.technicianItemReviewLbl

        name.text = arrayList[position].name

        if (arrayList[position].profilePicture != null){
            image.visibility = View.VISIBLE
            Picasso.get().load(arrayList[position].profilePicture?.second).into(image)
        } else{
            imageLbl.visibility = View.VISIBLE
            imageLbl.text = arrayList[position].name.first().toUpperCase().toString()
        }

        ratingBar.rating = arrayList[position].rating?.toFloat() ?: 0F
        jobsCountLbl.text = arrayList[position].jobsCount.toString()
        reviewsLbl.text = arrayList[position].reviewCount.toString()

        holder.itemView.setOnClickListener{
            Toast.makeText(context,"clicked ${name.text}",Toast.LENGTH_SHORT).show()
            showTechProfileHandler(position)
        }

        holder.binding.technicianItemBookBtn.setOnClickListener{
            bookTechnician(position)
        }
    }

    inner class TechViewHolder (var binding : TechnicianItemBinding) : RecyclerView.ViewHolder(binding.root)
}