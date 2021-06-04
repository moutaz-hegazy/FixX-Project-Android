package com.example.fixx.NavigationBar.notification


import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Notifications
import com.example.fixx.R
import kotlinx.android.synthetic.main.notification_row.view.*

class NotificationAdapter(private val notificationList: MutableList<Notifications>, private val listener:NotificationActivity) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    lateinit var context: Context

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val userName = itemView.notification_row_name_lbl
        val jobDesc = itemView.notification_row_desc_lbl
        val acceptBtn = itemView.notification_row_accept_btn
        val uerImg = itemView.notification_row_img


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
               //listener.onItemClick(position)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notification_row, parent, false)
        context = parent.context
        return NotificationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val currentItem = notificationList[position]
        holder.userName.text = currentItem.username
        holder.jobDesc.text = currentItem.jobDescription
        if(currentItem.profilePicture == null){
            //we need to add another check if user or technician
            holder.uerImg.setImageResource(R.drawable.colored_avatar_technician)
        }else{
            holder.uerImg.setImageResource(currentItem.profilePicture!!)
        }
        holder.acceptBtn.setOnClickListener {

        }


    }

    override fun getItemCount() = notificationList.size

}