package com.example.project.bottom_navigation_fragments

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.fixx.Addresses.view.MyAddresses
import com.example.fixx.LoginScreen.Views.RegistrationActivity
import com.example.fixx.NavigationBar.SettingsScreen.HelpActivity
import com.example.fixx.NavigationBar.SettingsScreen.ProfileActivity
import com.example.fixx.R
import com.example.fixx.WorkAddresses.view.MyWorkAddresses
import com.example.fixx.inAppChatScreens.views.NewMessageActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_language.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import java.util.*


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

//         fragmentManager?.beginTransaction()?.replace(R.id.settings_fragment, HomeFragment())?.addToBackStack("tag" )?.commit()



        val language = rootView.findViewById(R.id.settingsfragment_language_linear_layout) as LinearLayout
        language.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onClick(v: View) {

//                val popupMenu = PopupMenu(getActivity(),settingsfragment_language_linear_layout)
//
//                popupMenu.getMenuInflater().inflate(R.menu.language_popup, popupMenu.getMenu())
//
//            popupMenu.setOnMenuItemClickListener { menuItem ->
//
//                val id = menuItem.itemId
//
//               if (id == R.id.language_english) {
//
//                   println("english")
//              } else if (id == R.id.language_arabic) {
//
//                    println("arabic")
//               }
//
//               false
//            }
//                             popupMenu.show()
                val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_language, null)

                val dialog = BottomSheetDialog(btnsheet.context)
                dialog.setContentView(btnsheet)

                btnsheet.bottom_sheet_language_english.setOnClickListener {

//                    val languageToLoad = "en" // your language
//
//                    val locale = Locale(languageToLoad)
//                    Locale.setDefault(locale)
//                    val config = Configuration()
//                    config.locale = locale
//                    context?.getResources()?.updateConfiguration(
//                        config,
//                        context?.getResources()!!.getDisplayMetrics()
//                    )

                    val res: Resources = context!!.resources

                    val dm: DisplayMetrics = res.getDisplayMetrics()
                    val conf: Configuration = res.getConfiguration()
                    conf.setLocale(Locale("en")) // API 17+ only.

                    res.updateConfiguration(conf, dm)

                    dialog.dismiss()


                }

                btnsheet.bottom_sheet_language_arabic.setOnClickListener {

//                    val languageToLoad = "ar" // your language
//
//                    val locale = Locale(languageToLoad)
//                    Locale.setDefault(locale)
//                    val config = Configuration()
//                    config.locale = locale
//                    context?.getResources()?.updateConfiguration(
//                        config,
//                        context?.getResources()!!.getDisplayMetrics()
//                    )
                    val res: Resources = context!!.resources

                    val dm: DisplayMetrics = res.getDisplayMetrics()
                    val conf: Configuration = res.getConfiguration()
                    conf.setLocale(Locale("ar")) // API 17+ only.

                    res.updateConfiguration(conf, dm)
                    dialog.dismiss()

                }



                btnsheet.setOnClickListener {
                    dialog.dismiss()

                }
                dialog.show()


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

//        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
////                fragmentManager?.beginTransaction()?.replace(R.id.settings_fragment, HomeFragment())?.commit()
//                val transaction = activity?.supportFragmentManager?.beginTransaction()
//                transaction?.replace(R.id.settings_fragment, HomeFragment())
//                transaction?.commit()
//
//            }
//        })

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
        startActivity(Intent(context, MyAddresses::class.java))
    }


//    override fun onBackPressed(): Boolean {
//        return if (myCondition) {
//            //action not popBackStack
//            true
//        } else {
//            false
//        }
//    }


}


