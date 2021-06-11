package com.example.fixx.inAppChatScreens.views

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.POJOs.ChatMessage
import com.example.fixx.POJOs.Person
import com.example.fixx.Support.PushNotificationReceiver
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
    private lateinit var nManager :NotificationManager
    private val chatReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            val channelId = intent?.getIntExtra(Constants.CHANNEL_ID,-1)
            channelId?.let {
                if(it != -1){
                    Log.i(TAG, "onReceive: >>>>>>> $it")
                    nManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    nManager.cancel(it)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        recyclerview_chat_log.adapter = adapter
        channel = intent.getStringExtra(Constants.TRANS_CHAT_CHANNEL)

        // from notification.
        val bundle = intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE)
        val fromNotification =bundle?.getBoolean(Constants.TRANS_RESPONSE_BOOL,false) ?: false
        if(fromNotification){
            channel = bundle?.getString(Constants.TRANS_CHAT_CHANNEL)
            val contactUid = bundle?.getString(Constants.TRANS_CONTACT_UID)
            Log.i(TAG, "onCreate:  %%??>>>>>>$contactUid")
            chatLogVm = ChatLogViewModel(channel, contactUid,
                observer = { msg ->
                    Log.i("TAG", "onCreate: New Msg ->>>> ${msg.text}")
                    displayMsg(msg)
                    adapter.notifyDataSetChanged()
                })
            chatLogVm.fetchContact() {
                contact = it!!
                nManager.cancel(contact.phoneNumber.toInt())
                Log.i(TAG, "onCreate: >>>>>>>> contact >>>>>>>>> ${contact.name}")

                supportActionBar?.apply {
                    title = contact.name
                }
                chatLogVm.fetchChatHistory()
                chatLogVm.addContactToList(it.uid!!)
                setButton()
            }
        }else{
            Log.i(TAG, "onCreate: IAM IN THE ELSE <<<<<<<<<<<<")
            contact = intent.getSerializableExtra(Constants.TRANS_USERDATA) as Person
            supportActionBar?.apply {
                title = contact.name
            }
            nManager.cancel(contact.phoneNumber.toInt())
            chatLogVm = ChatLogViewModel(channel,contact.uid,
                observer = { msg ->
                    Log.i("TAG", "onCreate: New Msg ->>>> ${msg.text}")
                    displayMsg(msg)
                    adapter.notifyDataSetChanged()
                    setButton()
                })
            chatLogVm.fetchChatHistory()
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(chatReceiver, IntentFilter(Constants.CHAT_RECEIVER_FILTER))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(chatReceiver)
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
                        USER_OBJECT!!.name, txt, channel ?: chatLogVm.channel ?: "", USER_OBJECT?.uid ?: "",
                        USER_OBJECT!!.phoneNumber.toInt()),
                        arrayOf(token)).also {
                            chatLogVm.sendNotification(it)
                        }
                }
            }
        }
    }
    private fun displayMsg(msg : ChatMessage){
        if(!msg.text.isNullOrEmpty()) {
            if (msg.fromId == USER_OBJECT?.uid) {
                USER_OBJECT?.let {
                    adapter.add(ChatToItem(msg.text, it))
                    binding.recyclerviewChatLog.scrollToPosition(adapter.itemCount - 1)
                    Log.i("TAG", "displayMsg: HERE 1 >>> ${msg.text}")
                }
            } else {
                adapter.add(ChatFromItem(msg.text, contact))
                binding.recyclerviewChatLog.smoothScrollToPosition(adapter.itemCount - 1)
                Log.i("TAG", "displayMsg: HERE 2 >>> ${msg.text}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: DESTROOOOOOOOOOOOOOOY TESTTTTTTTTTTTTTTT")
        chatLogVm.removeObserver()
    }
}
