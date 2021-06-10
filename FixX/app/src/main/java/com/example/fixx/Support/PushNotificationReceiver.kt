package com.example.fixx.Support

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.fixx.JobDetailsDisplay.views.JobDetailsDisplayActivity
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.NavigationBar.viewmodels.NavBarViewmodel
import com.example.fixx.constants.Constants
import com.example.fixx.inAppChatScreens.views.ChatLogActivity
import com.example.fixx.techOrderDetailsScreen.views.TechViewOrderScreen

class PushNotificationReceiver()
    : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("TAG", "onReceive: >>>>>>>>>>>> ${intent.getStringExtra(Constants.TRANS_NOTIFICATION_TYPE)}")
        Log.i("TAG", "onReceive: >>>>>>>>>>>>>>>> ${intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE)}")
        when (intent.getStringExtra(Constants.TRANS_NOTIFICATION_TYPE)) {
            Constants.NOTIFICATION_TYPE_JOB_COMPLETED -> navigateToActivity(context,JobDetailsDisplayActivity::class.java,
                intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
            Constants.NOTIFICATION_TYPE_TECH_REPLY_CANCEL -> navigateToActivity(context,JobDetailsDisplayActivity::class.java,
                intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
            Constants.NOTIFICATION_TYPE_TECH_REPLY_DENY -> navigateToActivity(context,JobDetailsDisplayActivity::class.java,
                intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
            Constants.NOTIFICATION_TYPE_TECH_REPLY_CONFIRM -> navigateToActivity(context,JobDetailsDisplayActivity::class.java,
                intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
            Constants.NOTIFICATION_TYPE_USER_JOB_REQUEST -> navigateToActivity(context,TechViewOrderScreen::class.java,
                intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
            Constants.NOTIFICATION_TYPE_USER_ACCEPT -> navigateToActivity(context,TechViewOrderScreen::class.java,
                intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
            Constants.NOTIFICATION_TYPE_CHAT_MESSAGE -> navigateToActivity(context,ChatLogActivity::class.java,
                intent.getBundleExtra(Constants.TRANS_DATA_BUNDLE))
            else -> {}
        }
    }

    private fun navigateToActivity(context:Context,activity : Class<*>, bundle : Bundle?){
        Intent(context,activity).apply {
            putExtra(Constants.TRANS_DATA_BUNDLE,bundle)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }.also {
            context.startActivity(it)
        }
    }
}