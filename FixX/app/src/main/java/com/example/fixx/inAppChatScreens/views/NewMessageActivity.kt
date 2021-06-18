package com.example.fixx.inAppChatScreens.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.inAppChatScreens.viewModels.NewMessageViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*


class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = getString(R.string.Chats)

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
            val progressBar : ProgressBar= findViewById(R.id.chatList_progressBar)
            progressBar.visibility = View.INVISIBLE
            adapter.add(UserItem(person, channel))
            adapter.notifyDataSetChanged()
        }
    }
}
