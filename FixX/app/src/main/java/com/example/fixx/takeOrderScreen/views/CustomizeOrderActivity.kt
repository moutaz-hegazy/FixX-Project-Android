package com.example.fixx.takeOrderScreen.views

import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.Addresses.view.AddAddressActivity
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.POJOs.Job
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.ActivityCustomizeOrderBinding
import com.example.fixx.showTechnicianScreen.view.ShowTechniciansScreen
import com.example.fixx.takeOrderScreen.contracts.DateSelected
import com.example.fixx.takeOrderScreen.viewModels.CustomizeOrderViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_customize_order.*
import kotlinx.android.synthetic.main.bottom_sheet_pick.view.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class CustomizeOrderActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener ,DateSelected {

    private lateinit var binding: ActivityCustomizeOrderBinding
    private val values = arrayListOf<String>("Select Location", "Add new Location")
    private val images = arrayListOf<Bitmap>()

    private var imagePath : Uri? = null

    private var imagePathsList = arrayListOf<Uri>()
    private var imagePathsStringArray = arrayListOf<String>()

    private var imagePathsList2 = arrayListOf<Uri>()

    private var selectedLocation : String? = null
    private var selectedDate : String = {
        SimpleDateFormat("dd-MMM-YYYY").format(Calendar.getInstance().time)
    }()

    private var selectedFromTime = ""
    private var selectedToTime = ""
    private var selectedJobType : String = ""

    private val imagesAdapter : ImagesAdapter by lazy {
        ImagesAdapter(images)
    }

    private lateinit var spinnerAdapter : SpinnerAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizeOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        USER_OBJECT?.locations?.forEach {   locationString ->
            locationString.substringBefore("%").let {  locName->
                if(!locName.isNullOrEmpty()){
                    values.add(values.size-1,locName)
                }else{
                    values.add(values.size-1 , locationString.substringAfter("%"))
                }
            }
        }

        Log.i("TAG", "onCreate: >>>>$selectedDate")
        val serviceName = intent.getIntExtra(Constants.serviceName, -1)
        setJobType(serviceName)
        //Action bar configuration.
        supportActionBar?.apply {
            title = "${getString(serviceName)} ${getString(R.string.Request)}"
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF6200EE")))
        }
        //-----------------------------------------------------------
        // Spinner configuration.
        spinnerAdapter = SpinnerAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,values)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.customizeOrderPickLocationSpinner.adapter = adapter
                binding.customizeOrderPickLocationSpinner.onItemSelectedListener = this@CustomizeOrderActivity
        }
        //----------------------------------------------------------------------------------

        // recycler configuration
        val imageAdderPic : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.add_image)
        images.add(imageAdderPic)

        val layoutMngr = LinearLayoutManager(applicationContext)
        layoutMngr.orientation = RecyclerView.HORIZONTAL
        binding.customizeOrderImagesRecycler.apply {
            this.layoutManager = layoutMngr
            imagesAdapter.scrollToPositionHandler = {
                this.smoothScrollToPosition(it)
            }
            imagesAdapter.imagePickerHandler = {
                showBottomSheetDialog()
            }
            adapter = imagesAdapter
        }

        //-----------------------------------------------------------------------------

        // data picker configuration.
        binding.customizeOrderDatePickerBtn.setOnClickListener {
            showDatePicker()
        }
        //------------------------------------------------------------

        // time picker configuration.
        binding.customizeOrderFromTimeBtn.setOnClickListener {
            val calender = Calendar.getInstance()
            val timeListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calender.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calender.set(Calendar.MINUTE, minute)

                val timeString =  "${SimpleDateFormat("HH:mm").format(calender.time)}"
                binding.customizeOrderFromTimeLbl.text = "from : ${timeString}"
                selectedFromTime = timeString
            }

            TimePickerDialog(
                this, timeListener, calender.get(Calendar.HOUR_OF_DAY), calender.get(
                    Calendar.MINUTE
                ), true
            ).show()
        }

        binding.customizeOrderToTimeBtn.setOnClickListener {
            val calender = Calendar.getInstance()
            val timeListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calender.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calender.set(Calendar.MINUTE, minute)

                val timeString =  "${SimpleDateFormat("HH:mm").format(calender.time)}"
                binding.customizeOrderToTimeLbl.text = "to : $timeString"
                selectedToTime = timeString
            }

            TimePickerDialog(
                this, timeListener, calender.get(Calendar.HOUR_OF_DAY), calender.get(
                    Calendar.MINUTE
                ), true
            ).show()
        }
        //---------------------------------------------------------------

        // select Tech configuration.
        binding.cusomizeOrderSelectTechBtn.setOnClickListener{
            //select tech.
            if (validateJobData()) {
                val job = createNewJob()

                val selectTechIntent =
                    Intent(applicationContext, ShowTechniciansScreen::class.java).apply {
                        putExtra(Constants.serviceName, serviceName)
                        putExtra(Constants.TRANS_JOB, job)
                        /*putExtra(Constants.TRANS_IMAGES,images.subList(1,images.size).toTypedArray())
                          putExtra(Constants.TRANS_IMAGES_PATHS,imagePathsList.toTypedArray())*/
                        putExtra(Constants.TRANS_IMAGES_PATHS,imagePathsStringArray.toTypedArray())

                    }
                startActivityForResult(selectTechIntent,Constants.TECH_LIST_REQUEST_CODE)
            }else{
                Toast.makeText(this,"Please Select your Location", Toast.LENGTH_SHORT).show()
            }
        }
        //---------------------------------------------------------------

        // publish order configuration.
        binding.customizeOrederPublishBtn.setOnClickListener {

            if (validateJobData()) {
                val job = createNewJob()
                imagePathsStringArray?.forEach { image ->
                    imagePathsList2.add(Uri.parse(image))
                }
                CustomizeOrderViewModel(job, imagePathsList2,
                    onSuccessBinding = {
                        Toast.makeText(applicationContext, "Job Uploaded.", Toast.LENGTH_SHORT).show()
                    }, onFaliureBinding = {
                        Toast.makeText(applicationContext, "Job Upload Failed.", Toast.LENGTH_SHORT).show()
                    })
                finish()

            }else{
                Toast.makeText(this,"Please Select your Location", Toast.LENGTH_SHORT).show()
            }
        }
        //------------------------------------------------------------------
    }

    private fun validateJobData(): Boolean {
        return  !selectedJobType.isNullOrEmpty() && !selectedLocation.isNullOrEmpty()
    }

    private fun createNewJob() : Job{
        return Job(FirestoreService.auth.currentUser?.uid, selectedJobType, selectedLocation).apply {
            date = selectedDate
            description = binding.customizeOrderDescriptionTxt.text.toString()
            fromTime = selectedFromTime
            toTime = selectedToTime
            location = location
        }
    }


    private fun setJobType(id: Int){
        when(id){
            R.string.Painter -> selectedJobType = "Painter"
            R.string.Plumber -> selectedJobType = "Plumber"
            R.string.Electrician -> selectedJobType = "Electrician"
            R.string.Carpenter -> selectedJobType = "Carpenter"
            R.string.Tiles_Handyman -> selectedJobType = "Tiles_Handyman"
            R.string.Parquet -> selectedJobType = "Parquet"
            R.string.Smith -> selectedJobType = "Smith"
            R.string.Decoration_Stones -> selectedJobType = "Decoration_Stones"
            R.string.Alumetal -> selectedJobType = "Alumetal"
            R.string.Air_Conditioner -> selectedJobType = "Air_Conditioner"
            R.string.Curtains -> selectedJobType = "Curtains"
            R.string.Glass -> selectedJobType = "Glass"
            R.string.Satellite -> selectedJobType = "Satellite"
            R.string.Gypsum_Works -> selectedJobType = "Gypsum_Works"
            R.string.Marble -> selectedJobType = "Marble"
            R.string.Pest_Control -> selectedJobType = "Pest_Control"
            R.string.Wood_Painter -> selectedJobType = "Wood_Painter"
            R.string.Swimming_pool -> selectedJobType = "Swimming_pool"
            else -> selectedJobType = ""
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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



                Constants.START_ADDRESS_MAP_REQUEST_CODE -> {
                    val newAddress = data?.getStringExtra(Constants.TRANS_ADDRESS)
                    newAddress?.let {   address->
                        address.substringBefore("%").let { addName->
                            if(!addName.isNullOrEmpty()){
                                values.add(values.size-1, addName)
                            }else{
                                values.add(values.size-1, address.substringAfter("%"))
                            }
                            spinnerAdapter.notifyDataSetChanged()
                            customizeOrder_pickLocation_spinner.setSelection(values.size -2)
                        }
                    }
                }

                Constants.TECH_LIST_REQUEST_CODE -> {
                    data?.getBooleanExtra(Constants.TECH_LIST_BOOLEAN , false)?.let {
                        if(it){
                            finish()
                        }
                    }
                }
            }
            image?.let{
                images.add(it)
                imagesAdapter.notifyDataSetChanged()
            }
            //Log.i("TAG", "onActivityResult: will upload  ${imagePath.toString()}")
            imagePath?.let {
                imagePathsList.add(it)
                imagePathsStringArray.add(it.toString())
            }
        }
    }

    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment(this)

        datePickerFragment.show(supportFragmentManager, "pick a date")

    }

    // spinner on nothing selected.
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    // spinner on item selected.
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.i("TAG", "onItemSelected:<<<<<<<<<<<< " + values[position])
        if(position == values.size - 1){
            // add new location Logic.
            customizeOrder_pickLocation_spinner.setSelection(0)
            startAddressActivity()
        }else if(position == 0 ){

        }else{
            selectedLocation = values[position]
        }
        Toast.makeText(this, values[position], Toast.LENGTH_SHORT).show()
    }

    private fun startAddressActivity(){
        startActivityForResult(Intent(applicationContext, AddAddressActivity::class.java),
                Constants.START_ADDRESS_MAP_REQUEST_CODE)
    }

    private fun showBottomSheetDialog(){
        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_pick, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet.rootView)
        btnsheet.bottomSheet_camera_layout.setOnClickListener {

            checkForPermission(android.Manifest.permission.CAMERA,"Camera",
                Constants.CAMERA_PERMISSION_REQUEST_CODE){
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(
                    takePicture,
                    Constants.cameraPickerRequestCode
                )
            }
            dialog.dismiss()
        }
        btnsheet.bottomSheet_gallery_layout.setOnClickListener {
            checkForPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE,"Gallery",
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

    override fun receiveDate(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = GregorianCalendar()
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        val viewFormatter = SimpleDateFormat("dd-MMM-YYYY")
        val viewFormattedDate = viewFormatter.format(calendar.time)
        binding.customizeOrderDateLbl.text = "On : $viewFormattedDate"
        selectedDate = viewFormattedDate
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
                ActivityCompat.requestPermissions(this@CustomizeOrderActivity, arrayOf(permission), requestCode)
            }
        }
        builder.create().show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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



    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(getString(R.string.leaveDialogTitle))
        //set message for alert dialog
        builder.setMessage(getString(R.string.leaveOrderDialogMsg))

        //performing positive action
        builder.setPositiveButton(getString(R.string.exit)){ _, _ ->
           super.onBackPressed()
        }
        //performing negative action
        builder.setNegativeButton(getString(R.string.stay)){ _, _ ->

        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
        alertDialog.window!!.setBackgroundDrawableResource(R.drawable.btn_border)

        val positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        with(positiveButton) {
            setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        val negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        with(negativeButton) {
            setTextColor(ContextCompat.getColor(context, R.color.green))
        }

    }



}