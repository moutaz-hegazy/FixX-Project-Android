package com.example.fixx.technicianProfileScreen.view

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Comment
import com.example.fixx.databinding.CommentItemBinding
import com.squareup.picasso.Picasso

class TechnicianProfileRecyclerAdapter(val arrayList: MutableList<Comment>, val context: Context) : RecyclerView.Adapter<TechnicianProfileRecyclerAdapter.ProfileViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        var root = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(root)
    }

    override fun getItemCount(): Int {
        if (arrayList.size==0){
            Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show()
        }
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        var name : TextView = holder.binding.commentItemUsernameLbl
        var imageLbl : TextView = holder.binding.commentItemRoundedLetterLbl
        var image : ImageView = holder.binding.commentItemProfileImg
        var comment : TextView = holder.binding.commentItemCommentBodyLbl
        var date : TextView = holder.binding.commentItemDateLbl
        var rating : RatingBar = holder.binding.commentItemRatingBar
        name.text = arrayList[position].username

        if(arrayList[position].profilePicture != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.clipToOutline = true
            }
            image.visibility = View.VISIBLE
            Picasso.get().load(arrayList[position].profilePicture).into(image)
        } else{
            imageLbl.visibility = View.VISIBLE
            imageLbl.text = arrayList[position].username?.first()?.toUpperCase().toString()
        }

        rating.rating = arrayList[position].rating?.toFloat() ?: 0.0f
        comment.text = arrayList[position].commentContent
        date.text = arrayList[position].date
    }

    inner class ProfileViewHolder(var binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root)
}