package com.example.fixx.inAppChatScreens.views

import android.os.Build
import android.view.View
import com.example.fixx.POJOs.Person
import com.example.fixx.POJOs.User
import com.example.fixx.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*


class ChatFromItem(val text: String, val user: Person): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text = text
        if (user.profilePicture != null){
            viewHolder.itemView.imageview_chat_from_row.visibility = View.VISIBLE
            Picasso.get().load(user.profilePicture?.second).into(viewHolder.itemView.imageview_chat_from_row)
        }
        else{
            viewHolder.itemView.chat_from_profile_lbl.visibility = View.VISIBLE
            viewHolder.itemView.chat_from_profile_lbl.text = user.name.first().toUpperCase().toString()
        }

    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String, val user: Person): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text
        if (user.profilePicture != null){
            viewHolder.itemView.imageview_chat_to_row.visibility = View.VISIBLE
            Picasso.get().load(user.profilePicture?.second).into(viewHolder.itemView.imageview_chat_to_row)
        }
        else{
            viewHolder.itemView.chat_to_profile_lbl.visibility = View.VISIBLE
            viewHolder.itemView.chat_to_profile_lbl.text = user.name.first().toUpperCase().toString()
        }

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}


class UserItem(val user: Person, val channel : String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_new_message.text = user.name
        if (user.profilePicture != null){
            viewHolder.itemView.imageview_new_message.visibility = View.VISIBLE
            Picasso.get().load(user.profilePicture?.second).into(viewHolder.itemView.imageview_new_message)
        }
        else{
            viewHolder.itemView.Users_profile_lbl.visibility = View.VISIBLE
            viewHolder.itemView.Users_profile_lbl.text = user.name.first().toUpperCase().toString()
        }
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}