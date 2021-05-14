package com.example.fixx.takeOrderScreen.views

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.ActivityCustomizeOrderBinding
import com.example.fixx.takeOrderScreen.contracts.DateSelected
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*


class CustomizeOrderActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener ,DateSelected {

    private lateinit var binding: ActivityCustomizeOrderBinding
    private val values = arrayListOf<String>("Select Location", "Home", "Alexandria,elmandara.20th st")
    private val images = mutableListOf<Bitmap>()

    private val imagesAdapter : ImagesAdapter by lazy {
        ImagesAdapter(images)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizeOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Action bar configuration.
        supportActionBar?.apply {
            title = getString(R.string.CustomizeOrderTitle)
            setBackgroundDrawable(ColorDrawable(Color.BLACK))
        }
        //-----------------------------------------------------------
        // Spinner configuration.
        val arrayAdapter = SpinnerAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, values)
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
                startMediaIntent()
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
                calender.set(Calendar.HOUR_OF_DAY,hourOfDay)
                calender.set(Calendar.MINUTE,minute)

                binding.customizeOrderFromTimeLbl.text = "from : ${SimpleDateFormat("HH:mm").format(calender.time)}"
            }

            TimePickerDialog(this,timeListener,calender.get(Calendar.HOUR_OF_DAY),calender.get(Calendar.MINUTE),true).show()
        }

        binding.customizeOrderToTimeBtn.setOnClickListener {
            val calender = Calendar.getInstance()
            val timeListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calender.set(Calendar.HOUR_OF_DAY,hourOfDay)
                calender.set(Calendar.MINUTE,minute)

                binding.customizeOrderToTimeLbl.text = "from : ${SimpleDateFormat("HH:mm").format(calender.time)}"
            }

            TimePickerDialog(this,timeListener,calender.get(Calendar.HOUR_OF_DAY),calender.get(Calendar.MINUTE),true).show()
        }
        //---------------------------------------------------------------

    }

    private fun startMediaIntent(){
        val pickerDialog = Intent(this,ImagePickerDialog::class.java)
        startActivityForResult(pickerDialog,Constants.imagePickerDialogRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var image : Bitmap? = null
        if (requestCode == Constants.imagePickerDialogRequestCode && resultCode == Activity.RESULT_OK){
            data?.getBooleanExtra("fromCamera",false)?.let {
                value ->
                if(value){
                    image = data?.getBundleExtra("imageFromCamera")?.get("data") as Bitmap?
                }else{
                    val uriString = data?.getStringExtra("uriFromGallery")
                    val imageUri = Uri.parse(uriString)
                     image = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
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

        datePickerFragment.show(supportFragmentManager,"pick a date")

    }

    // spinner on nothing selected.
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    // spinner on item selected.
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.i("TAG", "onItemSelected:<<<<<<<<<<<< " + values[position])
        Toast.makeText(this, values[position], Toast.LENGTH_SHORT).show()
    }

    override fun receiveDate(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = GregorianCalendar()
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        val viewFormatter = SimpleDateFormat("dd-MMM-YYYY")
        val viewFormattedDate = viewFormatter.format(calendar.time)
        binding.customizeOrderDateLbl.text = "On : $viewFormattedDate"
    }
}