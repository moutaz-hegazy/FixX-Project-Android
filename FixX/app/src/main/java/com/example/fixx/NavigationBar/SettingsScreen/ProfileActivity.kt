package com.example.fixx.NavigationBar.SettingsScreen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_email.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_name.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_password.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_phone.*
import kotlinx.android.synthetic.main.bottom_sheet_pick.*

class ProfileActivity : AppCompatActivity() {
    var floatingActionButton: FloatingActionButton? = null
    var bottomSheet : BottomSheetDialog?= null
    val REQUEST_CODE = 100


    private val cameraRequest = 1888
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.hide()



        floatingActionButton = findViewById(R.id.profile_camera_floatingactionbutton)
        floatingActionButton?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                showBottomSheetDialog()
            }
        }

        )




        profile_edit_name_image_button.setOnClickListener {
            showBottomSheetEditName()
        }

        profile_edit_email_image_button.setOnClickListener {
            showBottomSheetEditEmail()
        }

        profile_edit_phone_image_button.setOnClickListener {
            showBottomSheetEditPhone()
        }

        profile_edit_password_image_button.setOnClickListener {
            showBottomSheetEditPassword()
        }





//        val galleryLinearLayout = findViewById(R.id.profile_gallery_linear_layout) as LinearLayout
//        galleryLinearLayout.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View) {
//
//                val intent = Intent("MediaStore.ACTION_IMAGE_CAPTURE")
//            startActivityForResult(intent, 1)
//            }
//        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            imageUri = data?.data
            profile_profilepicture_image_view.setImageURI(imageUri)
        }
    }

   private fun showBottomSheetDialog(){
        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_pick, null)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet)
        btnsheet.setOnClickListener {
            dialog.dismiss()
        }
       setContentView(R.layout.bottom_sheet_pick)

       profile_gallery_linear_layout.setOnClickListener {
           val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
           startActivityForResult(gallery, Constants.galleryPickerRequestCode)
       }

       if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
           == PackageManager.PERMISSION_DENIED)
           ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequest)
       profile_camera_linear_layout.setOnClickListener {
           val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
           startActivityForResult(cameraIntent, Constants.cameraPickerRequestCode)
       }



//       profile_gallery_linear_layout.setOnClickListener {
//
//           val intent = Intent(Intent.ACTION_PICK)
//           intent.type = "image/*"
//           startActivityForResult(intent, REQUEST_CODE)
//       }
        dialog.show()
    }

   private fun showBottomSheetEditName(){

        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_edit_name, null)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet)
        btnsheet.setOnClickListener {
            dialog.dismiss()

            bottom_sheet_edit_name_cancel_button.setOnClickListener {
                   dialog.dismiss()
            }

            bottom_sheet_edit_name_save_button.setOnClickListener {

                updateName(bottom_sheet_edit_name_edit_text.text.toString())
                dialog.dismiss()
            }
        }
        dialog.show()

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//       super.onActivityResult(requestCode, resultCode, data)
//       if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
//            profile_profilepicture_image_view.setImageURI(data?.data) // handle chosen image
//        }
//   }


    private fun updateName(newName : String){

    }

    private fun showBottomSheetEditEmail(){

        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_edit_email, null)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet)
        btnsheet.setOnClickListener {
            dialog.dismiss()

            bottom_sheet_edit_email_cancel_button.setOnClickListener {
                dialog.dismiss()
            }

            bottom_sheet_edit_email_save_button.setOnClickListener {

                updateName(bottom_sheet_edit_email_edit_text.text.toString())
                dialog.dismiss()
            }
        }
        dialog.show()

    }

    private fun showBottomSheetEditPhone(){

        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_edit_phone, null)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet)
        btnsheet.setOnClickListener {
            dialog.dismiss()

            bottom_sheet_edit_phone_cancel_button.setOnClickListener {
                dialog.dismiss()
            }

            bottom_sheet_edit_phone_save_button.setOnClickListener {

                updateName(bottom_sheet_edit_phone_edit_text.text.toString())
                dialog.dismiss()
            }
        }
        dialog.show()

    }

    private fun showBottomSheetEditPassword(){

        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_edit_password, null)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet)
        btnsheet.setOnClickListener {
            dialog.dismiss()

            bottom_sheet_edit_password_cancel_button.setOnClickListener {
                dialog.dismiss()
            }

            bottom_sheet_edit_password_save_button.setOnClickListener {

                updateName(bottom_sheet_edit_password_edit_text.text.toString())
                dialog.dismiss()
            }
        }
        dialog.show()

    }
//    fun openGallery(){
//
//        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        startActivityForResult(gallery, pickImage)
//
//    }
}