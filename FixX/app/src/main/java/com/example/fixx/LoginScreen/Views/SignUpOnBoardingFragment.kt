package com.example.fixx.LoginScreen.Views

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.SyncStateContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.POJOs.Details
import com.example.fixx.POJOs.User
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class SignUpOnBoardingFragment : Fragment() {

    private var isProfileTypeSelected = false
    private var isUserProfile = false
    private var isTechieProfile = false
    private var accountType: String = ""
    private var phoneNumber: String = ""
    private var phoneNumberEditText: EditText? = null
    private var userAvatar: ImageView? = null
    private var techAvatar: ImageView? = null
    private var userChoice: TextView? = null
    private var techChoice: TextView? = null
    private var nextButton: Button? = null
    private var userNameTxt : EditText? = null
    var fromGoogle = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = layoutInflater.inflate(R.layout.fragment_sign_up_on_boarding, container, false)
        phoneNumberEditText = root.findViewById(R.id.phoneNumber_id)
        userAvatar = root.findViewById(R.id.avatar_user_id)
        techAvatar = root.findViewById(R.id.avatar_technician_id)
        userChoice = root.findViewById(R.id.userChoice)
        techChoice = root.findViewById(R.id.techieChoice)
        nextButton = root.findViewById(R.id.next_button_id)
        userNameTxt = root.findViewById(R.id.userName_onboarding)

        userAvatar?.setImageResource(R.drawable.colored_avatar_user)
        techAvatar?.setImageResource(R.drawable.colored_avatar_technician)

        if(fromGoogle){
            userNameTxt?.visibility = View.VISIBLE
        }

        phoneNumberEditText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                phoneNumberEditText?.error = null
            }
        })

        userAvatar?.setOnClickListener(View.OnClickListener {
            isProfileTypeSelected = true
            isUserProfile = true
            isTechieProfile = false
            techAvatar?.setBackgroundResource(0)
            userAvatar?.setBackgroundResource(R.drawable.avatar_frame)
            techChoice?.setTextColor(Color.BLACK)
            userChoice?.setTextColor(Color.RED)
            accountType = getAppUserAccountType()
        })

        techAvatar?.setOnClickListener(View.OnClickListener {
            isProfileTypeSelected = true
            isTechieProfile = true
            isUserProfile = false
            userAvatar?.setBackgroundResource(0)
            techAvatar?.setBackgroundResource(R.drawable.avatar_frame)
            userChoice?.setTextColor(Color.BLACK)
            techChoice?.setTextColor(Color.RED)
            accountType = getAppUserAccountType()
        })

        nextButton?.setOnClickListener{
            phoneNumber = phoneNumberEditText?.text.toString()
            if(validateSignUpForm1()){
                FirestoreService.checkIfPhoneExists(phoneNumber){ exists->
                    if(exists){
                        phoneNumberEditText?.text?.clear()
                        Toast.makeText(context , "this phone number already exists.",Toast.LENGTH_SHORT).show()
                    }else {
                        if(fromGoogle && accountType == "User"){
                            val userObj = User()
                            userObj.name = userNameTxt?.text.toString()
                            userObj.email = FirebaseAuth.getInstance().currentUser?.email!!
                            userObj.accountType = "User"
                            userObj.phoneNumber = phoneNumber
                            FirestoreService.saveUserData(userObj,onSuccessHandler = {
                                USER_OBJECT = it
                                Intent(context?.applicationContext,NavigationBarActivity::class.java).also {
                                    context?.startActivity(it)
                                    activity?.finish()
                                }
                            },onFailHandler = {
                                Toast.makeText(context,R.string.CreateAccFail,Toast.LENGTH_SHORT).show()
                            })
                        }else {
                            passAppUserData(phoneNumber, accountType)
                        }
                    }
                }
            }
        }

        return root
    }

    private fun String.isValidPhoneNumber(): Boolean {
        var pattern = Pattern.compile("(011|012|010|015)[0-9]{8}")
        var matcher = pattern.matcher(phoneNumber)
        return matcher.matches()
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean{
        var flag = false
        if(this.phoneNumber.trim().length == 11 && this.phoneNumber.isValidPhoneNumber())
            flag = true
        else if(phoneNumber.isEmpty())
            phoneNumberEditText?.error = "This field is required"
        else
            phoneNumberEditText?.error = "Enter number as per Egyptian standards"
        return flag
    }

    private fun validateSignUpForm1(): Boolean {
        var flag: Boolean = false
        if (validatePhoneNumber(phoneNumber) && isProfileTypeSelected) {
            flag = true
            if(fromGoogle && userNameTxt?.text.isNullOrEmpty()){
                flag = false
                Toast.makeText(context,R.string.enterUserName,Toast.LENGTH_SHORT).show()
            }
        }else if (phoneNumber.isEmpty()) {
            phoneNumberEditText?.error = "This field is required."
        }else if (validatePhoneNumber(phoneNumber) && !isProfileTypeSelected) {
            Toast.makeText(context, "Please, choose profile type.", Toast.LENGTH_LONG).show()
        }
        return flag
    }

    private fun getAppUserAccountType(): String{
        var appUserAccountType: String = ""
        if(isUserProfile)
            appUserAccountType = "User"
        else if(isTechieProfile)
            appUserAccountType = "Technician"
        return appUserAccountType
    }

    private fun passAppUserData(phoneNumber: String, accountType: String){
        var data = Details(phoneNumber, accountType)
        val sentDataBundle = Bundle()
        if(fromGoogle) {
            sentDataBundle.putBoolean(Constants.TRANS_GOOGLE_BOOL, true)
            sentDataBundle.putString(Constants.TRANS_USER_NAME,userNameTxt?.text.toString())
        }
        sentDataBundle.putSerializable(Constants.TRANS_USERDATA, data)
        if(getAppUserAccountType() == "Technician"){
            PickJob().apply {
                arguments = sentDataBundle
            }.also {
                fragmentManager?.beginTransaction()?.replace(R.id.signup_onboarding_fragment_id, it)?.commit()
            }
        }else{
            val newSignUpTabFragment = SignUpTabFragment()
            newSignUpTabFragment.arguments = sentDataBundle
            fragmentManager?.beginTransaction()?.replace(R.id.signup_onboarding_fragment_id, newSignUpTabFragment)?.commit()
        }
    }
}