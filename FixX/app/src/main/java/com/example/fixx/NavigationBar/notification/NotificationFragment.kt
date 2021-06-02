package com.example.fixx.NavigationBar.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.POJOs.Notifications
import com.example.fixx.R
import kotlinx.android.synthetic.main.fragment_notification.*


class NotificationFragment : Fragment() {

    var myNotifications = mutableListOf<Notifications>()
    private val adapter = NotificationAdapter(myNotifications,this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val not1 = Notifications("esraa","job1")
        val not2 = Notifications("dina","job2")
        myNotifications.add(not1)
        myNotifications.add(not2)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notification_fragment_recycler_view.adapter = adapter
        notification_fragment_recycler_view.layoutManager = LinearLayoutManager(context)
        notification_fragment_recycler_view.setHasFixedSize(true)
    }


}