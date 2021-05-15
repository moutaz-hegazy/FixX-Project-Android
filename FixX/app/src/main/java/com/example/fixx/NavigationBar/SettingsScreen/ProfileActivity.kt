package com.example.fixx.NavigationBar.SettingsScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import com.example.fixx.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfileActivity : AppCompatActivity() {
    var floatingActionButton: FloatingActionButton? = null
    var bottomSheet : BottomSheetDialog?= null
    private val pickImage = 100
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
//        val galleryLinearLayout = findViewById(R.id.profile_gallery_linear_layout) as LinearLayout
//        galleryLinearLayout.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View) {
//
//                openGallery();
//            }
//        })
    }

    fun showBottomSheetDialog(){
        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_pick, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet)
        btnsheet.setOnClickListener {
            dialog.dismiss()
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