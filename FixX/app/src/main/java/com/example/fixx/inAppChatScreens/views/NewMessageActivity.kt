package com.example.fixx.inAppChatScreens.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.POJOs.User
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.inAppChatScreens.model.FirebaseService
import com.example.fixx.inAppChatScreens.viewModels.NewMessageViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*


class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Chat"

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FirebaseService.token = it
        }

        FirebaseMessaging.getInstance()
            .subscribeToTopic("/topics/${NavigationBarActivity.USER_OBJECT!!.uid}")
        val adapter = GroupAdapter<ViewHolder>().apply {
            setOnItemClickListener { item, view ->

                val userItem = item as UserItem

                val intent = Intent(view.context, ChatLogActivity::class.java)
                intent.putExtra(Constants.TRANS_USERDATA, userItem.user)
                intent.putExtra(Constants.TRANS_CHAT_CHANNEL, userItem.channel)
                startActivity(intent)
            }
        }
        recyclerview_newmessage.adapter = adapter

        NewMessageViewModel { person, channel ->
            adapter.add(UserItem(person, channel))
            adapter.notifyDataSetChanged()
        }
    }
}
