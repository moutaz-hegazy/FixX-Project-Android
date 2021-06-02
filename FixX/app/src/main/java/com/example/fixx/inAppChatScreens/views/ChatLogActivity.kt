package com.example.fixx.inAppChatScreens.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.POJOs.ChatMessage
import com.example.fixx.POJOs.Person
import com.example.fixx.Support.RetrofitInstance
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.ActivityChatLogBinding
import com.example.fixx.inAppChatScreens.model.ChatPushNotification
import com.example.fixx.inAppChatScreens.model.NotificationData
import com.example.fixx.inAppChatScreens.viewModels.ChatLogViewModel
import com.google.gson.Gson
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLog"
    }

    private val adapter = GroupAdapter<ViewHolder>()
    private lateinit var contact : Person

    private lateinit var binding : ActivityChatLogBinding
    private var channel : String? = null
    private lateinit var chatLogVm : ChatLogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerview_chat_log.adapter = adapter
        channel = intent.getStringExtra(Constants.TRANS_CHAT_CHANNEL)
        val fromNotification = intent.getBooleanExtra(Constants.TRANS_RESPONSE_BOOL,false)
        if(fromNotification){
            val contactUid = intent.getStringExtra(Constants.TRANS_CONTACT_UID)
            chatLogVm = ChatLogViewModel(channel,contactUid,
                observer = { msg ->
                    Log.i("TAG", "onCreate: New Msg ->>>> ${msg.text}")
                    displayMsg(msg)
                    adapter.notifyDataSetChanged()
                },onCompletion = { msgs ->
                    chatLogVm.fetchContact(){
                        contact = it!!
                        setButton()
                        Log.i("TAG", "onCreate: msgs   ALL >>>>>> $msgs")
                        msgs.forEach {
                                msg ->
                            displayMsg(msg)
                            adapter.notifyDataSetChanged()
                        }
                        supportActionBar?.apply {
                            title = contact.name
                        }
                    }
                })
        }else{
            contact = intent.getSerializableExtra(Constants.TRANS_USERDATA) as Person
            supportActionBar?.apply {
                title = contact.name
            }
            chatLogVm = ChatLogViewModel(channel,contact.uid,
                observer = { msg ->
                    Log.i("TAG", "onCreate: New Msg ->>>> ${msg.text}")
                    displayMsg(msg)
                    adapter.notifyDataSetChanged()
                },onCompletion = { msgs ->
                    setButton()
                    Log.i("TAG", "onCreate: msgs   ALL >>>>>> $msgs")
                    msgs.forEach {
                            msg ->
                        displayMsg(msg)
                        adapter.notifyDataSetChanged()
                    }
                })
        }
    }

    private fun setButton(){
        binding.sendButtonChatLog.setOnClickListener {
            val txt = binding.edittextChatLog.text.toString()
            if(!txt.isNullOrEmpty()){
                val newMsg = ChatMessage(txt,USER_OBJECT?.uid ?: "",
                    System.currentTimeMillis())
                chatLogVm.sendMessage(newMsg)
                binding.edittextChatLog.text.clear()
                contact.token?.let {    token ->
                    ChatPushNotification(NotificationData(Constants.NOTIFICATION_TYPE_CHAT_MESSAGE,
                        USER_OBJECT!!.name, txt, channel ?: chatLogVm.channel ?: "", USER_OBJECT?.uid ?: ""),
                        arrayOf(token)).also {
                            chatLogVm.sendNotification(it)
                        }
                }
            }
        }
    }
    private fun displayMsg(msg : ChatMessage){
        if(msg.fromId == USER_OBJECT?.uid){
            USER_OBJECT?.let {
                adapter.add(ChatToItem(msg.text, it))
                binding.recyclerviewChatLog.scrollToPosition(adapter.itemCount -1)
                Log.i("TAG", "displayMsg: HERE 1 >>> ${msg.text}")
            }
        }else{
            adapter.add(ChatFromItem(msg.text,contact))
            binding.recyclerviewChatLog.smoothScrollToPosition(adapter.itemCount -1)
            Log.i("TAG", "displayMsg: HERE 2 >>> ${msg.text}")
        }
    }
}
