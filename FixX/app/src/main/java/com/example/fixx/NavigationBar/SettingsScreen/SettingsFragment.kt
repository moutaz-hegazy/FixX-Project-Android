package com.example.project.bottom_navigation_fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.fixx.Addresses.view.MyAddresses
import com.example.fixx.LoginScreen.Views.RegistrationActivity
import com.example.fixx.LoginScreen.Views.SplashScreen
import com.example.fixx.MainActivity
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.NavigationBar.SettingsScreen.HelpActivity
import com.example.fixx.NavigationBar.SettingsScreen.ProfileActivity
import com.example.fixx.NavigationBar.viewmodels.SettingsViewmodel
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.WorkAddresses.view.MyWorkAddresses
import com.example.fixx.constants.Constants
import com.example.fixx.inAppChatScreens.views.NewMessageActivity
import com.example.fixx.technicianProfileScreen.view.TechnicianProfileActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottom_sheet_language.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.java.Constant


class SettingsFragment : Fragment() {

    private val viewmodel : SettingsViewmodel by lazy {
        SettingsViewmodel()
    }
   lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false)
        val language = rootView.findViewById(R.id.settingsfragment_language_linear_layout) as LinearLayout
        language.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onClick(v: View) {
                val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_language, null)

                val dialog = BottomSheetDialog(btnsheet.context)
                dialog.setContentView(btnsheet)

                btnsheet.bottom_sheet_language_english.setOnClickListener {
                    val languagePreferences = context?.getSharedPreferences(
                        Constants.LANGUAGE_SHARED_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                    val currentLanguage = languagePreferences?.getString(
                        Constants.CURRENT_LANGUAGE,
                        "en"
                    ) ?: "en"
                    if (currentLanguage != "en") {
                        languagePreferences?.edit()?.putString(Constants.CURRENT_LANGUAGE, "en")
                            ?.commit()
                        dialog.dismiss()

                        val packageManager = context!!.packageManager
                        val intent = packageManager.getLaunchIntentForPackage(context!!.packageName)
                        val componentName = intent!!.component
                        val mainIntent = Intent.makeRestartActivityTask(componentName)
                        context!!.startActivity(mainIntent)
                        Runtime.getRuntime().exit(0)

                    }
                    dialog.dismiss()
                }

                btnsheet.bottom_sheet_language_arabic.setOnClickListener {
                    val languagePreferences = context?.getSharedPreferences(
                        Constants.LANGUAGE_SHARED_PREFERENCES,
                        Context.MODE_PRIVATE
                    )
                    val currentLanguage = languagePreferences?.getString(
                        Constants.CURRENT_LANGUAGE,
                        "en"
                    ) ?: "en"
                    if (currentLanguage != "ar") {
                        languagePreferences?.edit()?.putString(Constants.CURRENT_LANGUAGE, "ar")
                            ?.commit()
                        dialog.dismiss()


                        val packageManager = context!!.packageManager
                        val intent = packageManager.getLaunchIntentForPackage(context!!.packageName)
                        val componentName = intent!!.component
                        val mainIntent = Intent.makeRestartActivityTask(componentName)
                        context!!.startActivity(mainIntent)
                        Runtime.getRuntime().exit(0)
                    }
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

        val signOut = rootView.findViewById(R.id.settingsfragment_exit_linear_layout) as LinearLayout
        signOut.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                viewmodel.signoutAccount()
                val intent = Intent(getActivity(), RegistrationActivity::class.java)
                getActivity()?.startActivity(intent)
                activity?.finish()
            }
        })

        rootView.settingsfragment_profile_name_lbl.text = USER_OBJECT?.name

        if (USER_OBJECT?.profilePicture != null){
            rootView.settingsfragment_profile_imageView.visibility = View.VISIBLE
            Picasso.get().load(USER_OBJECT?.profilePicture?.second).into(rootView.settingsfragment_profile_imageView)
        }
        else{
            rootView.settingsfragment_profile_imageText.visibility = View.VISIBLE
            rootView.settingsfragment_profile_imageText.text = USER_OBJECT?.name?.first()?.toUpperCase().toString()
        }

        rootView.settingsfragment_profile_linear_layout.setOnClickListener {
            openProfileActivity()
        }

        if(USER_OBJECT?.accountType == "Technician"){
            rootView.settings_WorkLocations_layOut.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    // open work locations view.
                    Intent(context, MyWorkAddresses::class.java).also {
                        context.startActivity(it)
                    }
                }
            }

            rootView.settings_work_profile_layOut.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    Intent(context,TechnicianProfileActivity::class.java).apply {
                        putExtra(Constants.TRANS_USERDATA, (USER_OBJECT as? Technician))
                        putExtra(Constants.TRANS_RESPONSE_BOOL, true)
                    }.also {
                        context.startActivity(it)
                    }
                }
            }
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

        val intent = Intent(getActivity(), ProfileActivity::class.java)
        getActivity()?.startActivity(intent)

    }

    override fun onStart() {
        super.onStart()

        rootView.settingsfragment_profile_name_lbl.text = USER_OBJECT?.name

        if (USER_OBJECT?.profilePicture != null){
            rootView.settingsfragment_profile_imageView.visibility = View.VISIBLE
            Picasso.get().load(USER_OBJECT?.profilePicture?.second).into(rootView.settingsfragment_profile_imageView)
        }
        else{
            rootView.settingsfragment_profile_imageText.visibility = View.VISIBLE
            rootView.settingsfragment_profile_imageText.text = USER_OBJECT?.name?.first()?.toUpperCase().toString()
        }
    }



    fun openHelpActivity(){

        val intent = Intent(getActivity(), HelpActivity::class.java)
        getActivity()?.startActivity(intent)

    }

    private fun openAddressesScreen(){
        startActivity(Intent(context, MyAddresses::class.java))
    }
}


