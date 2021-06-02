package com.example.fixx.NavigationBar.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.POJOs.Notifications
import com.example.fixx.R
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {

    var myNotifications = mutableListOf<Notifications>()
    private val adapter = NotificationAdapter(myNotifications,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        supportActionBar!!.hide()

        val not1 = Notifications("esraa","job1")
        val not2 = Notifications("dina","job2")
        myNotifications.add(not1)
        myNotifications.add(not2)

        notification_activity_recycler_view.adapter = adapter
        notification_activity_recycler_view.layoutManager = LinearLayoutManager(this)
        notification_activity_recycler_view.setHasFixedSize(true)
    }
}