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
import com.example.fixx.RegistrationActivity
import kotlinx.android.synthetic.main.fragment_settings.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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




        val profile = rootView.findViewById(R.id.settingsfragment_profile_linear_layout) as LinearLayout
        profile.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                openProfileActivity();
            }
        })

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


        return rootView
    }

    fun openProfileActivity(){

        val intent = Intent (getActivity(), ProfileActivity::class.java)
        getActivity()?.startActivity(intent)

    }

    fun openHelpActivity(){

        val intent = Intent (getActivity(), HelpActivity::class.java)
        getActivity()?.startActivity(intent)

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}