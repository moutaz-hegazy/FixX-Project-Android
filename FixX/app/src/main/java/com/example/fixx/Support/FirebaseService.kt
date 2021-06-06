package com.example.fixx.Support

import android.app.*
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.fixx.JobDetailsDisplay.views.JobDetailsDisplayActivity
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.inAppChatScreens.views.ChatLogActivity
import com.example.fixx.techOrderDetailsScreen.views.TechViewOrderScreen
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random
import kotlin.reflect.jvm.internal.impl.load.java.Constant

class FirebaseService : FirebaseMessagingService() {

    val CHANNEL_ID = "my_notification_channel"
    lateinit var notification : Notification
    companion object{
        var sharedPref:SharedPreferences? = null

        var token:String?
        get(){
            return sharedPref?.getString("token","")
        }
        set(value){
            sharedPref?.edit()?.putString("token",value)?.apply()
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        token = p0
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)


        Log.i("TAG", "onMessageReceived: >>>>>>>>>>>> MESSAGE RECEIVED <<<<<<<<")
        val intent = Intent(this,NavigationBarActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        when(p0.data["type"]){
            Constants.NOTIFICATION_TYPE_TECH_REPLY_CONFIRM ->{
                var pendingIntent : PendingIntent
                TaskStackBuilder.create(applicationContext).apply {
                    addNextIntentWithParentStack(Intent(applicationContext,JobDetailsDisplayActivity::class.java))
                    editIntentAt(0).apply {
                        putExtra(Constants.TRANS_JOB,p0.data["jobId"])
                    }
                    pendingIntent = getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT)
                }
                notification = createNotification(getString(p0.data["title"]?.toInt() ?: 0),
                    "${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}",pendingIntent)
            }

            Constants.NOTIFICATION_TYPE_USER_JOB_REQUEST -> {
                var pendingIntent : PendingIntent
                TaskStackBuilder.create(applicationContext).apply {
                    addNextIntentWithParentStack(Intent(applicationContext,TechViewOrderScreen::class.java))
                    editIntentAt(0).apply {
                        putExtra(Constants.TRANS_JOB,p0.data["jobID"])
                    }
                    pendingIntent = getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
                }
                notification = createNotification(getString(p0.data["title"]?.toInt() ?: 0),
                    "${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}",pendingIntent)
            }

            Constants.NOTIFICATION_TYPE_CHAT_MESSAGE ->{
                var pendingIntent : PendingIntent
                TaskStackBuilder.create(applicationContext).apply {
                    addNextIntentWithParentStack(Intent(applicationContext,ChatLogActivity::class.java))
                    editIntentAt(0).apply {
                        putExtra(Constants.TRANS_CONTACT_UID,p0.data["uid"])
                        putExtra(Constants.TRANS_CHAT_CHANNEL,p0.data["channel"])
                        putExtra(Constants.TRANS_RESPONSE_BOOL,true)
                    }
                    pendingIntent = getPendingIntent(2,PendingIntent.FLAG_UPDATE_CURRENT)
                }
                notification = createNotification("${p0.data["user"]}","${p0.data["message"]}",pendingIntent)
            }

            Constants.NOTIFICATION_TYPE_USER_ACCEPT -> {
                var pendingIntent : PendingIntent
                TaskStackBuilder.create(applicationContext).apply {
                    addNextIntentWithParentStack(Intent(applicationContext,TechViewOrderScreen::class.java))
                    editIntentAt(0).apply {
                        putExtra(Constants.TRANS_JOB,p0.data["jobId"])
                    }
                    pendingIntent = getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT)
                }
                notification = createNotification(getString(p0.data["title"]?.toInt() ?: 0),
                    "${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}",
                    pendingIntent)
            }

            Constants.NOTIFICATION_TYPE_TECH_REPLY_DENY ->{
                var pendingIntent : PendingIntent
                TaskStackBuilder.create(applicationContext).apply {
                    addNextIntentWithParentStack(Intent(applicationContext,JobDetailsDisplayActivity::class.java))
                    editIntentAt(0).apply {
                        putExtra(Constants.TRANS_JOB,p0.data["jobId"])
                    }
                    pendingIntent = getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT)
                }
                notification = createNotification(getString(p0.data["title"]?.toInt() ?: 0),
                    "${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}",
                    pendingIntent)
            }

            Constants.NOTIFICATION_TYPE_TECH_REPLY_CANCEL ->{
                var pendingIntent : PendingIntent
                TaskStackBuilder.create(applicationContext).apply {
                    addNextIntentWithParentStack(Intent(applicationContext,JobDetailsDisplayActivity::class.java))
                    editIntentAt(0).apply {
                        putExtra(Constants.TRANS_JOB,p0.data["jobId"])
                    }
                    pendingIntent = getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT)
                }
                notification = createNotification(getString(p0.data["title"]?.toInt() ?: 0),
                    "${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}",
                    pendingIntent)
            }

            Constants.NOTIFICATION_TYPE_JOB_COMPLETED -> {
                var pendingIntent : PendingIntent
                TaskStackBuilder.create(applicationContext).apply {
                    addNextIntentWithParentStack(Intent(applicationContext,JobDetailsDisplayActivity::class.java))
                    editIntentAt(0).apply {
                        putExtra(Constants.TRANS_JOB,p0.data["jobId"])
                    }
                    pendingIntent = getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT)
                }
                notification = createNotification(getString(p0.data["title"]?.toInt() ?: 0),
                    "${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}",
                    pendingIntent)
            }

            else ->{
                val pendingIntent = PendingIntent.getActivity(this,0,intent,FLAG_ONE_SHOT)
                notification = createNotification(getString(p0.data["title"]?.toInt() ?: 0),
                    "${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}",
                pendingIntent)

            }
        }
//        val pendingIntent = PendingIntent.getActivity(this,0,intent,FLAG_ONE_SHOT)
//        val notification = NotificationCompat.Builder(this,CHANNEL_ID)
//            .setContentTitle(getString(p0.data["title"]?.toInt() ?: 0))
//            .setContentText("${p0.data["user"]} ${getString(p0.data["message"]?.toInt() ?: 0)}")
//            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//            .build()

        notificationManager.notify(notificationId,notification)
    }


    private fun createNotification(title : String, content:String, pendingIntent : PendingIntent)
    =    NotificationCompat.Builder(this,CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build()


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channelName = "ChannelFirebaseChat"
        val channel = NotificationChannel(CHANNEL_ID,channelName,IMPORTANCE_HIGH).apply {
            description="FIREBASE CHAT DESCRIPTION"
            enableLights(true)
            lightColor = Color.WHITE
        }
        notificationManager.createNotificationChannel(channel)

    }
}