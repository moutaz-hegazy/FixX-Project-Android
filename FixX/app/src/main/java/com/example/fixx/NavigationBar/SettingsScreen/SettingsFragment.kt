package com.example.project.bottom_navigation_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fixx.Addresses.MyAddresses
import com.example.fixx.NavigationBar.SettingsScreen.ProfileActivity
import com.example.fixx.R
import com.example.fixx.inAppChatScreens.views.NewMessageActivity
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        rootView.settingsfragment_profile_linear_layout.setOnClickListener {
            openProfileActivity()
        }

        rootView.settings_addresses_layOut.setOnClickListener {
            openAddressesScreen()
        }

        rootView.settings_chat_layout.setOnClickListener {
            startActivity(Intent(context, NewMessageActivity::class.java))
        }
        return rootView
    }

    private fun openProfileActivity(){

        val intent = Intent (getActivity(), ProfileActivity::class.java)
        getActivity()?.startActivity(intent)

    }

    private fun openAddressesScreen(){
        startActivity(Intent(context, MyAddresses::class.java))
    }

}