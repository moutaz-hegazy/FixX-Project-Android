package com.example.fixx

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.fixx.POJOs.Details
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = layoutInflater.inflate(R.layout.fragment_sign_up_on_boarding, container, false)
        phoneNumberEditText = root.findViewById(R.id.phoneNumber_id)
        userAvatar = root.findViewById(R.id.avatar_user_id)
        techAvatar = root.findViewById(R.id.avatar_technician_id)
        userChoice = root.findViewById(R.id.userChoice)
        techChoice = root.findViewById(R.id.techieChoice)
        nextButton = root.findViewById(R.id.next_button_id)

        userAvatar?.setImageResource(R.drawable.colored_avatar_user)
        techAvatar?.setImageResource(R.drawable.colored_avatar_technician)

        phoneNumberEditText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        userAvatar?.setOnClickListener(View.OnClickListener {
            isProfileTypeSelected = true
            isUserProfile = true
            isTechieProfile = false
            techAvatar?.setBackgroundResource(0)
            userAvatar?.setBackgroundResource(R.drawable.avatar_frame)
            techChoice?.setTextColor(Color.BLACK)
            userChoice?.setTextColor(Color.YELLOW)
            accountType = getAppUserAccountType()
        })

        techAvatar?.setOnClickListener(View.OnClickListener {
            isProfileTypeSelected = true
            isTechieProfile = true
            isUserProfile = false
            userAvatar?.setBackgroundResource(0)
            techAvatar?.setBackgroundResource(R.drawable.avatar_frame)
            userChoice?.setTextColor(Color.BLACK)
            techChoice?.setTextColor(Color.YELLOW)
            accountType = getAppUserAccountType()
        })

        nextButton?.setOnClickListener(View.OnClickListener {
            phoneNumber = phoneNumberEditText?.text.toString()
            if(validateSignUpForm1()){
                passAppUserData(phoneNumber, accountType)
            }
        })
        return root
    }

    private fun String.isPhoneNumberValid(): Boolean {
        if(Pattern.matches("(011|012|010|015)[0-9]{8}", phoneNumber)) {
            return true
        }
        return false
    }

    private fun getAppUserAccountType(): String{
        var appUserAccountType: String = ""
        if(isUserProfile)
            appUserAccountType = "User"
        else if(isTechieProfile)
            appUserAccountType = "Technician"
        return appUserAccountType
    }

    private fun validatePhoneNumber(): Boolean{
        var validPhoneNumber = false
        if(phoneNumber.trim().length == 11 && phoneNumber.isPhoneNumberValid()){
            validPhoneNumber = true
            nextButton?.isEnabled = true
        }else{
            phoneNumberEditText?.error = "Phone number format must start with 01x followed by 8 numbers"
            userAvatar?.isEnabled = false
            techAvatar?.isEnabled = false
            nextButton?.isEnabled = false
        }
        return validPhoneNumber
    }

    private fun validateSignUpForm1(): Boolean {
        var validRequiredFields: Boolean = false
        if (validatePhoneNumber() && isProfileTypeSelected) {
            validRequiredFields = true
        } else if (phoneNumber.isEmpty()) {
            phoneNumberEditText?.error = "This field is required."
        } else if (!isProfileTypeSelected) {
            Toast.makeText(context, "Please, choose profile type.", Toast.LENGTH_LONG).show()
        }
        return validRequiredFields
    }

    private fun passAppUserData(phoneNumber: String, accountType: String){
        var data = Details(phoneNumber, accountType)
        val newSignUpTabFragment = SignUpTabFragment()
        val sentDataBundle = Bundle()
        sentDataBundle.putParcelable("AppUserData", data)
        newSignUpTabFragment.arguments = sentDataBundle
        fragmentManager?.beginTransaction()?.replace(R.id.signup_onboarding_fragment_id, newSignUpTabFragment)?.commit()
    }
}