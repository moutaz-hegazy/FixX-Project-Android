package com.example.fixx.addExtensionScreen.views

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Extension
import com.example.fixx.R
import com.example.fixx.addExtensionScreen.viewmodels.JobExtensionViewmodel
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.ActivityAddJobExtensionBinding
import com.example.fixx.takeOrderScreen.views.ImagesAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_customize_order.*
import kotlinx.android.synthetic.main.bottom_sheet_pick.view.*
import java.io.ByteArrayOutputStream

class AddJobExtensionActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddJobExtensionBinding

    private val images = arrayListOf<Bitmap>()
    private var imagePathsList2 = arrayListOf<Uri>()

    private val imagesAdapter : ImagesAdapter by lazy {
        ImagesAdapter(images){}
    }

    private var imagePathsStringArray = arrayListOf<String>()
    private lateinit var viewmodel : JobExtensionViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJobExtensionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jobId = intent.getStringExtra(Constants.TRANS_JOB)

        if(jobId != null){
            viewmodel = JobExtensionViewmodel(jobId)
        }else{
            finish()
        }

        supportActionBar?.apply {
            title = getString(R.string.ExtendJob)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF6200EE")))
        }

        val imageAdderPic : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.add_image)
        images.add(imageAdderPic)

        val layoutMngr = LinearLayoutManager(applicationContext)
        layoutMngr.orientation = RecyclerView.HORIZONTAL
        binding.addExtensionRecycler.apply {
            this.layoutManager = layoutMngr
            imagesAdapter.scrollToPositionHandler = {
                this.smoothScrollToPosition(it)
            }
            imagesAdapter.imagePickerHandler = {
                showBottomSheetDialog()
            }
            adapter = imagesAdapter
        }

        binding.addExtensionBtn.setOnClickListener {
            binding.addExtensionBtn.isClickable = false
            binding.addExtensionBtn.setBackgroundColor(Color.GRAY)
            binding.addExtensionBtn.text = getString(R.string.Uploading)
            if(images.size < 2 && binding.addExtensionDescriptionTxt.text.toString().isNullOrEmpty()){
                Toast.makeText(this,R.string.ExtensionDataMissing,Toast.LENGTH_SHORT).show()
            }else{
                val jobExtension = buildExtension()
                viewmodel.uploadExtension(jobExtension,imagePathsList2,onSuccessBinding = {
                    ext ->
                    Intent().apply {
                        putExtra(Constants.TRANS_EXTENSION,ext)
                    }.also {
                        setResult(Activity.RESULT_OK,it)
                        finish()
                    }
                },onFailureBinding = {
                    binding.addExtensionBtn.isClickable = true
                    binding.addExtensionBtn.setBackgroundColor(Color.parseColor("#30D5C8"))
                    binding.addExtensionBtn.text = getString(R.string.AddExtension)
                    Toast.makeText(this, R.string.ExtendUploadFail,Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

    private fun buildExtension() : Extension{
        imagePathsStringArray.forEach { image ->
            imagePathsList2.add(Uri.parse(image))
        }
        val desc = binding.addExtensionDescriptionTxt.text.toString()
        val ext = Extension(description = desc)

        return ext
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var imagePath : Uri? = null
        var image : Bitmap? = null
        if(resultCode == Activity.RESULT_OK){
            when (requestCode) {
                Constants.cameraPickerRequestCode ->{
                    image = data?.extras?.get("data") as? Bitmap
                    imagePath = image?.let { getImageUriFromBitmap(this, it) }
                }

                Constants.galleryPickerRequestCode ->{
                    image = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
                    imagePath = data?.data
                }
            }
            //Log.i("TAG", "onActivityResult: will upload  ${imagePath.toString()}")
            imagePath?.let {
                imagePathsStringArray.add(it.toString())
                image?.let{
                    images.add(it)
                    imagesAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
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
                ActivityCompat.requestPermissions(this@AddJobExtensionActivity, arrayOf(permission), requestCode)
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
}