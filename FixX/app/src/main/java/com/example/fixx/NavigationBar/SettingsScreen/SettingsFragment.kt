package com.example.project.bottom_navigation_fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.fixx.NavigationBar.SettingsScreen.HelpActivity
import com.example.fixx.NavigationBar.SettingsScreen.ProfileActivity
import com.example.fixx.R

import kotlinx.android.synthetic.main.fragment_settings.*
import com.example.fixx.Addresses.MyAdresses
import com.example.fixx.LoginScreen.Views.RegistrationActivity
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



//            val popupMenu = PopupMenu(getActivity(), settingsfragment_language_linear_layout)
//            popupMenu.menuInflater.inflate(R.menu.language_popup, popupMenu.menu)



       // val menuItemView = view!!.findViewById<View>(R.id.settingsfragment_language_linear_layout)


//        val popupMenu = PopupMenu(activity, menuItemView)
//        popupMenu.getMenuInflater().inflate(R.menu.language_popup, popupMenu.getMenu())
//
//            popupMenu.setOnMenuItemClickListener { menuItem ->
//
//                val id = menuItem.itemId
//
//                if (id == R.id.language_english) {
//
//                    println("english")
//                } else if (id == R.id.language_arabic) {
//
//                    println("arabic")
//                }
//
//                false
//
//            }
//
//
//            settingsfragment_language_linear_layout.setOnClickListener {
//                popupMenu.show()
//            }




       

        val language = rootView.findViewById(R.id.settingsfragment_language_linear_layout) as LinearLayout
        language.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onClick(v: View) {

              //  val popupMenu = PopupMenu(getActivity(),view, Gravity.BOTTOM)
//            popupMenu.menuInflater.inflate(R.menu.language_popup, popupMenu.menu)

                val popupMenu = PopupMenu(getActivity(),settingsfragment_language_linear_layout)

                popupMenu.getMenuInflater().inflate(R.menu.language_popup, popupMenu.getMenu())

            popupMenu.setOnMenuItemClickListener { menuItem ->

                val id = menuItem.itemId

               if (id == R.id.language_english) {

                   println("english")
              } else if (id == R.id.language_arabic) {

                    println("arabic")
               }

               false
            }
                             popupMenu.show()


            }
        })


        val help = rootView.findViewById(R.id.settingsfragment_help_linear_layout) as LinearLayout
        help.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                openHelpActivity();
            }
        })

        val exit = rootView.findViewById(R.id.settingsfragment_exit_linear_layout) as LinearLayout
        exit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent (getActivity(), RegistrationActivity::class.java)
                getActivity()?.startActivity(intent)

            }
        })


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

    fun openHelpActivity(){

        val intent = Intent (getActivity(), HelpActivity::class.java)
        getActivity()?.startActivity(intent)

    }

    private fun openAddressesScreen(){
        startActivity(Intent(context, MyAdresses::class.java))
    }

}