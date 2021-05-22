package com.example.fixx.technicianProfileScreen.view

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Comment
import com.example.fixx.databinding.CommentItemBinding

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
        name.text = arrayList[position].username
        var image : TextView = holder.binding.commentItemRoundedLetterLbl
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image.clipToOutline = true
        }
        var comment : TextView = holder.binding.commentItemCommentBodyLbl
        comment.text = arrayList[position].commentContent
        var date : TextView = holder.binding.commentItemDateLbl
        date.text = arrayList[position].date
    }

    inner class ProfileViewHolder(var binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root)
}