package com.example.fixx.Support

import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random


class FirebaseService : FirebaseMessagingService() {

    val CHANNEL_ID = "my_notification_channel"
    lateinit var notification : Notification
    companion object{
        var sharedPref:SharedPreferences? = null

        var token:String?
        get(){
            return sharedPref?.getString("token", "")
        }
        set(value){
            sharedPref?.edit()?.putString("token", value)?.apply()
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        token = p0
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)


        Log.i("TAG", "onMessageReceived: >>>>>>>>>>>> MESSAGE RECEIVED <<<<<<<<")
        val intent = Intent(this, NavigationBarActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        when(p0.data["type"]){
            Constants.NOTIFICATION_TYPE_TECH_REPLY_CONFIRM -> {
                val bundle = Bundle().apply {
                    putString(Constants.TRANS_JOB, p0.data["jobId"])
                }
                notification = createNotification(
                    getString(p0.data["title"]?.toInt() ?: 0),
                    "${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}",
                    navigateToActivity(bundle, Constants.NOTIFICATION_TYPE_TECH_REPLY_CONFIRM,notificationId)
                )
                notificationManager.notify(notificationId, notification)

                Intent(this, BroadcastReceiver::class.java).apply {
                    action = Constants.USER_JOB_DETAILS_FILTER
                    putExtra(Constants.CHANNEL_ID, notificationId)
                    putExtra(Constants.TRANS_JOB, p0.data["jobId"])
                }.also {
                    LocalBroadcastManager.getInstance(this).sendBroadcast(it)
                }
            }

            Constants.NOTIFICATION_TYPE_USER_JOB_REQUEST -> {
                val bundle = Bundle()
                bundle.putString(Constants.TRANS_JOB, p0.data["jobId"])
                notification = createNotification(
                    getString(p0.data["title"]?.toInt() ?: 0),
                    "${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}",
                    navigateToActivity(bundle, Constants.NOTIFICATION_TYPE_USER_JOB_REQUEST,notificationId)
                )
                notificationManager.notify(notificationId, notification)
            }

            Constants.NOTIFICATION_TYPE_CHAT_MESSAGE -> {
                val bundle = Bundle().apply {
                    putString(Constants.TRANS_CONTACT_UID, p0.data["uid"])
                    putString(Constants.TRANS_CHAT_CHANNEL, p0.data["channel"])
                    putBoolean(Constants.TRANS_RESPONSE_BOOL, true)
                }
                val channelId = p0.data["id"]?.toInt() ?: 0
                notification = createNotification(
                    "${p0.data["user"]}",
                    "${p0.data["message"]}",
                    navigateToActivity(bundle, Constants.NOTIFICATION_TYPE_CHAT_MESSAGE,channelId)
                )
                notificationManager.notify(channelId, notification)
                Intent(this, BroadcastReceiver::class.java).apply {
                    action = Constants.CHAT_RECEIVER_FILTER
                    putExtra(Constants.CHANNEL_ID, channelId)
                }.also {
                    LocalBroadcastManager.getInstance(this).sendBroadcast(it)
                }

            }

            Constants.NOTIFICATION_TYPE_USER_ACCEPT -> {
                val bundle = Bundle().apply {
                    putString(Constants.TRANS_JOB, p0.data["jobId"])
                }
                notification = createNotification(
                    getString(p0.data["title"]?.toInt() ?: 0),
                    "${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}",
                    navigateToActivity(bundle, Constants.NOTIFICATION_TYPE_USER_ACCEPT,notificationId)
                )
                notificationManager.notify(notificationId, notification)
                Intent(this, BroadcastReceiver::class.java).apply {
                    action = Constants.TECH_ORDER_DETAILS_FILTER
                    putExtra(Constants.CHANNEL_ID, notificationId)
                    putExtra(Constants.TRANS_JOB, p0.data["jobId"])
                }.also {
                    LocalBroadcastManager.getInstance(this).sendBroadcast(it)
                }

            }

            Constants.NOTIFICATION_TYPE_TECH_REPLY_DENY -> {
                val bundle = Bundle().apply {
                    putString(Constants.TRANS_JOB, p0.data["jobId"])
                }
                notification = createNotification(
                    getString(p0.data["title"]?.toInt() ?: 0),
                    "${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}",
                    navigateToActivity(bundle, Constants.NOTIFICATION_TYPE_TECH_REPLY_DENY,notificationId)
                )
                notificationManager.notify(notificationId, notification)

                Intent(this, BroadcastReceiver::class.java).apply {
                    action = Constants.USER_JOB_DETAILS_FILTER
                    putExtra(Constants.CHANNEL_ID, notificationId)
                    putExtra(Constants.TRANS_JOB, p0.data["jobId"])
                }.also {
                    LocalBroadcastManager.getInstance(this).sendBroadcast(it)
                }
            }

            Constants.NOTIFICATION_TYPE_TECH_REPLY_CANCEL -> {
                val bundle = Bundle().apply {
                    putString(Constants.TRANS_JOB, p0.data["jobId"])
                }
                notification = createNotification(
                    getString(p0.data["title"]?.toInt() ?: 0),
                    "${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}",
                    navigateToActivity(bundle, Constants.NOTIFICATION_TYPE_TECH_REPLY_CANCEL,notificationId)
                )
                notificationManager.notify(notificationId, notification)

                Intent(this, BroadcastReceiver::class.java).apply {
                    action = Constants.USER_JOB_DETAILS_FILTER
                    putExtra(Constants.CHANNEL_ID, notificationId)
                    putExtra(Constants.TRANS_JOB, p0.data["jobId"])
                }.also {
                    LocalBroadcastManager.getInstance(this).sendBroadcast(it)
                }
            }

            Constants.NOTIFICATION_TYPE_JOB_COMPLETED -> {
                val bundle = Bundle().apply {
                    putString(Constants.TRANS_JOB, p0.data["jobId"])
                }
                notification = createNotification(
                    getString(p0.data["title"]?.toInt() ?: 0),
                    "${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}",
                    navigateToActivity(bundle, Constants.NOTIFICATION_TYPE_JOB_COMPLETED,notificationId)
                )
                notificationManager.notify(notificationId, notification)

                Intent(this, BroadcastReceiver::class.java).apply {
                    action = Constants.USER_JOB_DETAILS_FILTER
                    putExtra(Constants.CHANNEL_ID, notificationId)
                    putExtra(Constants.TRANS_JOB, p0.data["jobId"])
                }.also {
                    LocalBroadcastManager.getInstance(this).sendBroadcast(it)
                }
            }

            else ->{}
        }

    }

    private fun applicationInForeground(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.runningAppProcesses
        var isActivityFound = false
        if (services[0].processName.equals(packageName, ignoreCase = true)
            && services[0].importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        ) {
            isActivityFound = true
        }
        Log.i(
            "TAG",
            "applicationInForeground: >>>>>>>>>>>>${activityManager.getRunningTasks(1)[0].topActivity?.className}"
        )
        return isActivityFound
    }

    private fun navigateToActivity(bundle: Bundle, notificationType: String,requestCode : Int) : PendingIntent{
        var pendingIntent : PendingIntent
        if(applicationInForeground()){
            Intent(applicationContext, PushNotificationReceiver::class.java).apply {
                putExtra(Constants.TRANS_DATA_BUNDLE, bundle)
                putExtra(Constants.TRANS_NOTIFICATION_TYPE, notificationType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }.also {
                pendingIntent = PendingIntent.getBroadcast(this, requestCode, it, PendingIntent.FLAG_CANCEL_CURRENT)
            }
        }else{
            TaskStackBuilder.create(applicationContext).apply {
                addNextIntentWithParentStack(
                    Intent(
                        applicationContext,
                        NavigationBarActivity::class.java
                    )
                )
                editIntentAt(0).apply {
                    putExtra(Constants.TRANS_DATA_BUNDLE, bundle)
                    putExtra(Constants.TRANSIT_FROM_NOTIFICATION, true)
                    putExtra(Constants.TRANS_NOTIFICATION_TYPE, notificationType)
                }
                pendingIntent = getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
        return pendingIntent
    }


    private fun createNotification(title: String, content: String, pendingIntent: PendingIntent)
    =    NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
        .setAutoCancel(true)
        .setGroup(Constants.NOTIFICATION_GROUP)
        .setContentIntent(pendingIntent)
        .build()

    private fun buildSummaryNotification() =  NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("SUMMARY")
        //set content text to support devices running API level < 24
        .setContentText("Two new messages")
        .setSmallIcon(R.drawable.phone)
        //build summary info into InboxStyle template
        .setStyle(
            NotificationCompat.InboxStyle()
                .addLine("Alex Faarborg Check this out")
                .addLine("Jeff Chang Launch Party")
                .setBigContentTitle("2 new messages")
                .setSummaryText("janedoe@example.com")
        )
        //specify which group this notification belongs to
        .setGroup(Constants.NOTIFICATION_GROUP)
        //set this notification as the summary for the group
        .setGroupSummary(true)
        .build()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channelName = "ChannelFirebaseChat"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description="FIREBASE CHAT DESCRIPTION"
            enableLights(true)
            lightColor = Color.WHITE
        }
        notificationManager.createNotificationChannel(channel)

    }
}