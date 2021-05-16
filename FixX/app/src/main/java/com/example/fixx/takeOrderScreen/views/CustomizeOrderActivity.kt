package com.example.fixx.takeOrderScreen.views

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.ActivityCustomizeOrderBinding
import com.example.fixx.showTechnicianScreen.view.ShowTechniciansScreen
import com.example.fixx.takeOrderScreen.contracts.DateSelected
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_customize_order.*
import kotlinx.android.synthetic.main.bottom_sheet_pick.view.*
import java.text.SimpleDateFormat
import java.util.*


class CustomizeOrderActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener ,DateSelected {

    private lateinit var binding: ActivityCustomizeOrderBinding
    private val values = arrayListOf<String>("Select Location", "Add new Location")
    private val images = mutableListOf<Bitmap>()

    private var uid : Int? = 13
    private var selectedLocation : String? = null
    private var selectedDate : String = {
        SimpleDateFormat("dd-MMM-YYYY").format(Calendar.getInstance().time)
    }()
    private var selectedFromTime = ""
    private var selectedToTime = ""
    private var selectedDescription = ""
    private var selectedJobType : String = ""

    private val imagesAdapter : ImagesAdapter by lazy {
        ImagesAdapter(images)
    }

    private lateinit var spinnerAdapter : SpinnerAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizeOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("TAG", "onCreate: >>>>$selectedDate")
        val serviceName = intent.getIntExtra(Constants.serviceName, -1)
        setJobType(serviceName)
        //Action bar configuration.
        supportActionBar?.apply {
            title = getString(serviceName) + " Request"
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF6200EE")))
        }
        //-----------------------------------------------------------
        // Spinner configuration.
        spinnerAdapter = SpinnerAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            values
        )
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
            val selectTechIntent = Intent(applicationContext,ShowTechniciansScreen::class.java).apply {
                putExtra(Constants.LOCATION_TO_TECH, selectedLocation)
                putExtra(Constants.JOB_TYPE_TO_TECH,selectedJobType)
                putExtra(Constants.serviceName,serviceName)
            }
            startActivity(selectTechIntent)
        }
        //---------------------------------------------------------------

        // publish order configuration.
        binding.customizeOrederPublishBtn.setOnClickListener {
            
        }
        //------------------------------------------------------------------

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
                Constants.cameraPickerRequestCode -> if (resultCode === RESULT_OK) {
                    image = data?.extras?.get("data") as? Bitmap
                }

                Constants.galleryPickerRequestCode -> if (resultCode === RESULT_OK) {
                    image = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
                }
            }
            image?.let{
                images.add(it)
                imagesAdapter.notifyDataSetChanged()
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
//            spinnerAdapter.addValue("SHIT")
//            customizeOrder_pickLocation_spinner.setSelection(spinnerAdapter.getPosition("SHIT"))
        }else{
            selectedLocation = values[position]
        }
        Toast.makeText(this, values[position], Toast.LENGTH_SHORT).show()
    }


    private fun showBottomSheetDialog(){
        val btnsheet = layoutInflater.inflate(R.layout.bottom_sheet_pick, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet.rootView)
        btnsheet.bottomSheet_camera_layout.setOnClickListener {

            checkForPermission(android.Manifest.permission.CAMERA,"Camera",Constants.CAMERA_PERMISSION_REQUEST_CODE)
            dialog.dismiss()
        }
        btnsheet.bottomSheet_gallery_layout.setOnClickListener {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhoto, Constants.galleryPickerRequestCode)
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
    private fun checkForPermission(permission : String, name : String,    requestCode : Int){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when {
                ContextCompat.checkSelfPermission(applicationContext,permission) == PackageManager.PERMISSION_GRANTED -> {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(
                        takePicture,
                        Constants.cameraPickerRequestCode
                    )
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
        fun innerCheck(name: String){
            if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"$name permission refused",Toast.LENGTH_SHORT)
            }else{
                Toast.makeText(this,"$name permission accepted",Toast.LENGTH_SHORT)
            }

            when(requestCode){
                Constants.CAMERA_PERMISSION_REQUEST_CODE -> innerCheck("Camera")
            }
        }
    }
}