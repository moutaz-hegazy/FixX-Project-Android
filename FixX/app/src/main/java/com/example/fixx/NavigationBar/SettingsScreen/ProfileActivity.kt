package com.example.fixx.NavigationBar.SettingsScreen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_email.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_name.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_password.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_phone.*
import kotlinx.android.synthetic.main.bottom_sheet_pick.view.*
import java.io.ByteArrayOutputStream

class ProfileActivity : AppCompatActivity() {
    var floatingActionButton: FloatingActionButton? = null
    var bottomSheet : BottomSheetDialog?= null
    private var imagePath : Uri? = null
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         var image : Bitmap? = null
        if(resultCode == Activity.RESULT_OK){
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

            image?.let {
                profile_profilepicture_image_view.setImageBitmap(it)
            }
        }
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

//    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
//        val bytes = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
//        return Uri.parse(path.toString())
//    }

   private fun showBottomSheetDialog(){
        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_pick, null)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet.rootView)

       btnsheet.bottomSheet_gallery_layout.setOnClickListener {
           val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
           startActivityForResult(gallery, Constants.galleryPickerRequestCode)
           dialog.dismiss()
       }

       if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
           == PackageManager.PERMISSION_DENIED)
           ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),Constants.CAMERA_PERMISSION_REQUEST_CODE)

      btnsheet.bottomSheet_camera_layout.setOnClickListener {
           val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

          //cameraIntent.putExtra("data", imagePath)

           startActivityForResult(cameraIntent, Constants.cameraPickerRequestCode)
           dialog.dismiss()
       }


        btnsheet.setOnClickListener {
            dialog.dismiss()
        }
      // setContentView(R.layout.bottom_sheet_pick)





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