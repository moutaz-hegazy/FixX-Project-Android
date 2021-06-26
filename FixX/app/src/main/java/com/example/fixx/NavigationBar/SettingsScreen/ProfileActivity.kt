package com.example.fixx.NavigationBar.SettingsScreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.NavigationBar.viewmodels.ProfileViewmodel
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.ActivityProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_email.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_email.view.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_name.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_name.view.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_new_password.view.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_password.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_password.view.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_phone.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_phone.view.*
import kotlinx.android.synthetic.main.bottom_sheet_pick.view.*
import kotlinx.android.synthetic.main.fragment_login_tab.*
import java.io.ByteArrayOutputStream

class ProfileActivity : AppCompatActivity() {

    var bottomSheet: BottomSheetDialog? = null
    private var imagePath: Uri? = null
    private var imageUri: Uri? = null
    private lateinit var binding : ActivityProfileBinding
    private val viewmodel : ProfileViewmodel by lazy {
        ProfileViewmodel(USER_OBJECT!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.profileNameTextView.text = USER_OBJECT?.name
        binding.profileEmailTextView.text = USER_OBJECT?.email
        binding.profilePhoneTextView.text = USER_OBJECT?.phoneNumber

        USER_OBJECT?.profilePicture?.let {
            Picasso.get().load(it.second).into(binding.profileProfilepictureImageView)
        }
        binding.profileCameraFloatingactionbutton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                showBottomSheetDialog()
            }
        })

        binding.profileEditNameImageButton.setOnClickListener {
            showBottomSheetEditName()
        }

        binding.profileEditEmailImageButton.setOnClickListener {
            showBottomSheetEnterPassword(){ password ->
                showBottomSheetEditEmail(password)
            }
        }

        binding.profileEditPhoneImageButton.setOnClickListener {
            showBottomSheetEditPhone()
        }

        binding.profileEditPasswordImageButton.setOnClickListener {
            showBottomSheetEnterPassword(){ oldPassword ->
                changePasswordBottomSheet(oldPassword)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var image : Bitmap? = null
        if(resultCode == Activity.RESULT_OK){

            val builder = AlertDialog.Builder(this)
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            @SuppressLint("InflateParams") val view: View = inflater.inflate(R.layout.layout_custom_progress_bar, null)
            builder.setView(view)

            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.show()

            when (requestCode) {
                Constants.cameraPickerRequestCode -> {
                    image = data?.extras?.get("data") as? Bitmap
                    imagePath = image?.let { getImageUriFromBitmap(this, it) }
                }

                Constants.galleryPickerRequestCode -> {
                    image = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
                    imagePath = data?.data
                }
            }

            imagePath?.let {
                viewmodel.updateProfilePic(it,
                    onSuccessBinding = {    link ->
                        USER_OBJECT?.profilePicture = link
                        image?.let {
                            profile_profilepicture_image_view.setImageBitmap(it)
                        }
                        dialog.hide()
                    },onFailBinding = {
                        Toast.makeText(this,getString(R.string.ImageUploadFailed),Toast.LENGTH_LONG).show()
                    })
            }
        }
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }


    private fun showBottomSheetDialog(){
        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_pick, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet.rootView)
        btnsheet.bottomSheet_camera_layout.setOnClickListener {
            checkForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,"Save"
                ,Constants.EXTERNAL_STORAGE_REQUEST_CODE){
                checkForPermission(android.Manifest.permission.CAMERA,"Camera",
                    Constants.CAMERA_PERMISSION_REQUEST_CODE){
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(
                        takePicture,
                        Constants.cameraPickerRequestCode
                    )
                }
            }

            dialog.dismiss()
        }
        btnsheet.bottomSheet_gallery_layout.setOnClickListener {
            checkForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,"Gallery",
                Constants.GALLERY_PERMISSION_REQUEST_CODE){
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, Constants.galleryPickerRequestCode)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    // camera permission Code ->
    private fun checkForPermission(permission : String, name : String, requestCode : Int ,onGrantedHandler: ()->Unit){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when {
                ContextCompat.checkSelfPermission(applicationContext,permission) == PackageManager.PERMISSION_GRANTED -> {

                    onGrantedHandler()
                    Toast.makeText(this, "$name Permission Granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission),requestCode)
            }
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int){
        val builder = AlertDialog.Builder(this).apply {
            title = "Permission Required"
            setMessage("Permission to access $name is required to continue")
            setPositiveButton("Ok"){
                    dialog, which ->
                ActivityCompat.requestPermissions(this@ProfileActivity, arrayOf(permission), requestCode)
            }
        }
        builder.create().show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String) : Boolean{
            return if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"$name permission refused",Toast.LENGTH_SHORT)
                false
            }else{
                Toast.makeText(this,"$name permission accepted",Toast.LENGTH_SHORT)
                true
            }
        }

        when(requestCode){
            Constants.EXTERNAL_STORAGE_REQUEST_CODE ->{
                checkForPermission(android.Manifest.permission.CAMERA,"Camera",
                    Constants.CAMERA_PERMISSION_REQUEST_CODE){
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(
                        takePicture,
                        Constants.cameraPickerRequestCode
                    )
                }
            }

            Constants.CAMERA_PERMISSION_REQUEST_CODE -> {
                if (innerCheck("Camera")){
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(
                        takePicture,
                        Constants.cameraPickerRequestCode
                    )
                }
            }
            Constants.GALLERY_PERMISSION_REQUEST_CODE -> {
                if(innerCheck("Gallery")){
                    val pickPhoto = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(pickPhoto, Constants.galleryPickerRequestCode)
                }
            }
        }
    }



//    private fun showBottomSheetDialog() {
//        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_pick, null)
//
//        val dialog = BottomSheetDialog(this)
//        dialog.setContentView(btnsheet.rootView)
//
//        btnsheet.bottomSheet_gallery_layout.setOnClickListener {
//            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//            startActivityForResult(gallery, Constants.galleryPickerRequestCode)
//            dialog.dismiss()
//        }
//
//        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
//            == PackageManager.PERMISSION_DENIED
//        )
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.CAMERA),
//                Constants.CAMERA_PERMISSION_REQUEST_CODE
//            )
//
//        btnsheet.bottomSheet_camera_layout.setOnClickListener {
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivityForResult(cameraIntent, Constants.cameraPickerRequestCode)
//            dialog.dismiss()
//        }
//
//        btnsheet.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.show()
//    }

    private fun showBottomSheetEditName() {

        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_edit_name, null)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet)
        btnsheet.bottom_sheet_edit_name_cancel_button.setOnClickListener {
            dialog.dismiss()
        }

        btnsheet.bottom_sheet_edit_name_save_button.setOnClickListener {
            val text = btnsheet.bottom_sheet_edit_name_edit_text.text.toString()
            if(text.isNullOrEmpty()){
                Toast.makeText(this, getString(R.string.AddNewName),Toast.LENGTH_SHORT).show()
            }else if(text == USER_OBJECT?.name){
                dialog.dismiss()
            }else{
                btnsheet.bottom_sheet_edit_name_save_button.isClickable = false
                btnsheet.bottom_sheet_edit_name_cancel_button.isClickable = false
                viewmodel.updateName(text, onSuccessBinding = {
                    USER_OBJECT?.name = text
                    binding.profileNameTextView.text = text
                    dialog.dismiss()
                },onFailBinding = {
                    Toast.makeText(applicationContext, getString(R.string.NameUpdateFail),Toast.LENGTH_LONG).show()
                    btnsheet.bottom_sheet_edit_name_edit_text.text.clear()
                    btnsheet.bottom_sheet_edit_name_save_button.isClickable = true
                    btnsheet.bottom_sheet_edit_name_cancel_button.isClickable = true
                })
            }
        }
        dialog.show()

    }

    private fun showBottomSheetEnterPassword(onSuccessHanlder : (password: String) -> Unit){
        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_edit_password, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet)
        btnsheet.bottom_sheet_edit_password_cancel_button.setOnClickListener{
            dialog.dismiss()
        }

        btnsheet.bottom_sheet_edit_password_lbl.text = getString(R.string.VerifyPassword)

        btnsheet.bottom_sheet_edit_password_next_button.setOnClickListener {
            val password = btnsheet.bottom_sheet_edit_password_edit_text.text.toString()
            if(password.isNullOrEmpty() || password.length < 6){
                Toast.makeText(this, getString(R.string.EnterPassword),Toast.LENGTH_LONG).show()
            }else{
                btnsheet.bottom_sheet_edit_password_next_button.isClickable = false
                btnsheet.bottom_sheet_edit_password_cancel_button.isClickable = false
                viewmodel.verifyPassword(password, onSuccessBinding = {
                    onSuccessHanlder(password)
                    dialog.dismiss()
                },onFailBinding = {
                    Toast.makeText(this, getString(R.string.WrongPassword),Toast.LENGTH_SHORT).show()
                    btnsheet.bottom_sheet_edit_password_edit_text.text.clear()
                    btnsheet.bottom_sheet_edit_password_next_button.isClickable = true
                    btnsheet.bottom_sheet_edit_password_cancel_button.isClickable = true
                })
            }
        }

        dialog.show()
    }

    private fun showBottomSheetEditEmail(password: String) {

        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_edit_email, null)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet)
        btnsheet.bottom_sheet_edit_email_cancel_button.setOnClickListener {
            dialog.dismiss()
        }

        btnsheet.bottom_sheet_edit_email_save_button.setOnClickListener {
            val text = btnsheet.bottom_sheet_edit_email_edit_text.text.toString()
            if(text.isNullOrEmpty()){
                Toast.makeText(this, getString(R.string.AddNewEmail),Toast.LENGTH_SHORT).show()
            }else if(text == USER_OBJECT?.email){
                dialog.dismiss()
            }else{
                btnsheet.bottom_sheet_edit_email_save_button.isClickable = false
                btnsheet.bottom_sheet_edit_email_cancel_button.isClickable = false
                viewmodel.updateEmail(text,password, onSuccessBinding = {
                    USER_OBJECT?.email = text
                    binding.profileEmailTextView.text = text
                    dialog.dismiss()
                },onFailBinding = { repeated ->
                    if(repeated){
                        Toast.makeText(applicationContext, getString(R.string.EmailExists),
                            Toast.LENGTH_LONG).show()
                        btnsheet.bottom_sheet_edit_email_edit_text.text.clear()
                        btnsheet.bottom_sheet_edit_email_save_button.isClickable = true
                        btnsheet.bottom_sheet_edit_email_cancel_button.isClickable = true
                    }else {
                        Toast.makeText(applicationContext, getString(R.string.EmailUpdateFail),
                            Toast.LENGTH_LONG).show()
                        btnsheet.bottom_sheet_edit_email_edit_text.text.clear()
                        btnsheet.bottom_sheet_edit_email_save_button.isClickable = true
                        btnsheet.bottom_sheet_edit_email_cancel_button.isClickable = true
                    }
                })
            }
        }
        dialog.show()

    }

    private fun showBottomSheetEditPhone() {

        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_edit_phone, null)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet)
        btnsheet.bottom_sheet_edit_phone_cancel_button.setOnClickListener {
            dialog.dismiss()
        }

        btnsheet.bottom_sheet_edit_phone_save_button.setOnClickListener {
            val text = btnsheet.bottom_sheet_edit_phone_edit_text.text.toString()
            if(text.isNullOrEmpty()){
                Toast.makeText(this, getString(R.string.AddNewPhoneNumber),Toast.LENGTH_SHORT).show()
            }else if(text == USER_OBJECT?.phoneNumber){
                dialog.dismiss()
            }else{
                btnsheet.bottom_sheet_edit_phone_save_button.isClickable = false
                btnsheet.bottom_sheet_edit_phone_cancel_button.isClickable = false
                viewmodel.updatePhoneNumber(text, onSuccessBinding = {
                    USER_OBJECT?.phoneNumber = text
                    binding.profilePhoneTextView.text = text
                    dialog.dismiss()
                },onFailBinding = { repeated ->
                    if(repeated){
                        Toast.makeText(applicationContext, getString(R.string.PhoneExists),
                            Toast.LENGTH_LONG).show()
                    }else {
                        Toast.makeText(applicationContext, getString(R.string.PhoneUpdateFail),
                            Toast.LENGTH_LONG).show()
                    }
                    btnsheet.bottom_sheet_edit_phone_edit_text.text.clear()
                    btnsheet.bottom_sheet_edit_phone_save_button.isClickable = true
                    btnsheet.bottom_sheet_edit_phone_cancel_button.isClickable = true
                })
            }
        }
        dialog.show()

    }

//    private fun showBottomSheetEditPassword() {
//        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_edit_password, null)
//
//        val dialog = BottomSheetDialog(this)
//
//        dialog.setContentView(btnsheet)
//
//        btnsheet.bottom_sheet_edit_password_cancel_button.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        btnsheet.bottom_sheet_edit_password_next_button.setOnClickListener {
//            val password = btnsheet.bottom_sheet_edit_password_edit_text.text.toString()
//            if(password.isNullOrEmpty() || password.length < 6){
//                Toast.makeText(this, getString(R.string.EnterPassword),Toast.LENGTH_LONG).show()
//                btnsheet.bottom_sheet_edit_password_edit_text.text.clear()
//            }else{
//                btnsheet.bottom_sheet_edit_password_next_button.isClickable = false
//                btnsheet.bottom_sheet_edit_password_cancel_button.isClickable = false
//                viewmodel.verifyPassword(password, onSuccessBinding = {
//                    changePasswordBottomSheet(password)
//                    dialog.dismiss()
//                },onFailBinding = {
//                    Toast.makeText(this, getString(R.string.WrongPassword),Toast.LENGTH_SHORT).show()
//                    btnsheet.bottom_sheet_edit_password_edit_text.text.clear()
//                    btnsheet.bottom_sheet_edit_password_next_button.isClickable = true
//                    btnsheet.bottom_sheet_edit_password_cancel_button.isClickable = true
//                })
//            }
//        }
//        dialog.show()
//
//    }

    private fun changePasswordBottomSheet(oldPassword : String){
        val nextbtnsheet = layoutInflater.inflate(R.layout.bottom_sheet_edit_new_password, null)
        val newDialog = BottomSheetDialog(this)
        newDialog.setContentView(nextbtnsheet)

        nextbtnsheet.bottom_sheet_edit_newpassword_cancel_button.setOnClickListener {
            newDialog.dismiss()
        }

        nextbtnsheet.bottom_sheet_edit_newpassword_save_button.setOnClickListener {
            val newPassword = nextbtnsheet.bottom_sheet_edit_newpassword_edit_text.text.toString()
            val confirmedPassword = nextbtnsheet.bottom_sheet_edit_reenternewpassword_edit_text.text.toString()
            if(newPassword.isNullOrEmpty() || newPassword.length < 6){
                Toast.makeText(this, getString(R.string.EnterPassword),Toast.LENGTH_LONG).show()
                nextbtnsheet.bottom_sheet_edit_newpassword_edit_text.text.clear()
                nextbtnsheet.bottom_sheet_edit_reenternewpassword_edit_text.text.clear()
            }else if(newPassword != confirmedPassword){
                Toast.makeText(this, getString(R.string.WrongPassword),Toast.LENGTH_LONG).show()
                nextbtnsheet.bottom_sheet_edit_newpassword_edit_text.text.clear()
                nextbtnsheet.bottom_sheet_edit_reenternewpassword_edit_text.text.clear()
            }else{
                nextbtnsheet.bottom_sheet_edit_newpassword_save_button.isClickable = false
                nextbtnsheet.bottom_sheet_edit_newpassword_cancel_button.isClickable = false
                viewmodel.updatePassword(newPassword, oldPassword,
                    onSuccessBinding = {
                        newDialog.dismiss()
                    },onFailBinding = {
                        Toast.makeText(this, getString(R.string.PasswordUpdateFail),Toast.LENGTH_LONG).show()
                        nextbtnsheet.bottom_sheet_edit_newpassword_edit_text.text.clear()
                        nextbtnsheet.bottom_sheet_edit_reenternewpassword_edit_text.text.clear()
                        nextbtnsheet.bottom_sheet_edit_newpassword_save_button.isClickable = true
                        nextbtnsheet.bottom_sheet_edit_newpassword_cancel_button.isClickable = true
                    })
            }
        }
        newDialog.show()
    }
}
