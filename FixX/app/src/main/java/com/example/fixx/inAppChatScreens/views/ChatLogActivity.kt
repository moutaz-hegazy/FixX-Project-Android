package com.example.fixx.inAppChatScreens.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.POJOs.ChatMessage
import com.example.fixx.POJOs.Person
import com.example.fixx.POJOs.User
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.ActivityChatLogBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLog"
    }

    private val adapter = GroupAdapter<ViewHolder>()
    private lateinit var contact : Person

    private lateinit var binding : ActivityChatLogBinding
    private lateinit var channel : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contact = intent.getSerializableExtra(Constants.TRANS_USERDATA) as Person
        recyclerview_chat_log.adapter = adapter
        val msg1 = ChatMessage("Hello",FirestoreService.auth?.currentUser?.uid ?: "",System.currentTimeMillis() / 1000)

        supportActionBar?.apply {
            title = contact.name
        }
        FirestoreService.fetchChatHistory(contact.uid ?: "", observerHandler = {
            msg ->
            Log.i("TAG", "onCreate: New Msg ->>>> ${msg.text}")
            displayMsg(msg)
            adapter.notifyDataSetChanged()

        }, onCompletion = {
            msgs, ch ->
            channel = ch
            setButton()
            Log.i("TAG", "onCreate: msgs   ALL >>>>>> $msgs")
            msgs.forEach {
                msg ->
                displayMsg(msg)
                adapter.notifyDataSetChanged()
            }
        })



    }

    private fun setButton(){
        binding.sendButtonChatLog.setOnClickListener {
            val txt = binding.edittextChatLog.text.toString()
            if(!txt.isNullOrEmpty()){
                val newMsg = ChatMessage(txt,FirestoreService.auth.currentUser?.uid ?: "",
                    System.currentTimeMillis() / 1000)
                FirestoreService.sendChatMessage(newMsg, channel)
            }
        }
    }
    private fun displayMsg(msg : ChatMessage){
        if(msg.fromId == FirestoreService.auth.currentUser?.uid){
            NavigationBarActivity.USER_OBJECT?.let {
                adapter.add(ChatFromItem(msg.text, it))
                Log.i("TAG", "displayMsg: HERE 1 >>> ${msg.text}")
            }
        }else{
            adapter.add(ChatToItem(msg.text,contact))
            Log.i("TAG", "displayMsg: HERE 2 >>> ${msg.text}")
        }
    }
}
