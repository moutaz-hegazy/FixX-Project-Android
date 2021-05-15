package com.example.fixx.takeOrderScreen.views

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.ActivityImagePickerDialogBinding


class ImagePickerDialog : AppCompatActivity() {
    private lateinit var binding: ActivityImagePickerDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePickerDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pickerDialogCameraBtn.setOnClickListener{
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(
                takePicture,
                Constants.cameraPickerRequestCode
            )
        }

        binding.pickerDialogGallaryBtn.setOnClickListener {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhoto, Constants.galleryPickerRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.cameraPickerRequestCode -> if (resultCode === RESULT_OK) {
                val returnIntent = Intent()
                returnIntent.putExtra("fromCamera", true)
                returnIntent.putExtra("imageFromCamera", data?.extras)
                setResult(RESULT_OK, returnIntent)
                finish()
            }

            Constants.galleryPickerRequestCode -> if (resultCode === RESULT_OK) {
                val returnIntent = Intent()
                returnIntent.putExtra("fromCamera", false)
                returnIntent.putExtra("uriFromGallery", data?.data.toString())
                setResult(RESULT_OK, returnIntent)
                finish()
            }
        }
    }
}