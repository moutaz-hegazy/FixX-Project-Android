package com.example.fixx.Addresses

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fixx.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.android.synthetic.main.activity_add_address.*


class AddAddressActivity : AppCompatActivity() {

    companion object {
        const val START_ACTIVITY_3_REQUEST_CODE = 1

        var addedAddress = " "

        val cities = arrayOf("City", "Cairo", "Alexandria")
        var area = mutableListOf<String>()

        val cairoArea = arrayOf(
            "Area",
            "El Shrouk",
            "1st Settlement",
            "Fifth Settlement",
            "Madenti",
            "El Rehab",
            "10th Of Ramadan",
            "Badr City",
            "New Heliopolis City",
            "Zamalek",
            "Heliopolis",
            "Nasser City",
            "El Qobbah",
            "El Maadi",
            "El Mokkatm",
            "El Mohandsen",
            "El Shekh Zayed",
            " El Dokki",
            "Giza Square",
            "El Haram",
            "Fissal",
            "Shobra",
            "Obour",
            " El Matareya",
            "6th October",
            "Helwan",
            "Ain Shams",
            "DownTown",
            "Al-Manyal",
            "Al-Agouza",
            "other"
        )

        val alexArea = arrayOf(
            "Area",
            "Moharam Bek",
            "Abou Qeer",
            "El Hadara",
            "El Amerya",
            "El Asafra, El Azareta",
            "Bahari",
            "Bokla",
            "Burg El Arab",
            "El Qabary",
            "Fleming",
            "Janklees",
            "Gleem",
            "Kafr Abdou",
            "Louran",
            "El Mandara",
            "Miami",
            "San Stefano",
            "Sidi Beshr",
            "Sidi Gaber",
            "El Shatbi",
            "Sporting",
            "Victoria",
            "Smouha",
            "Stanly",
            "Wabor El Maya",
            "El Hanovil",
            "El Bitash",
            "Other"
        )

        val emptyArea = arrayOf("Other")

        var areas = alexArea + cairoArea

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)


        setCitySpinner()


        add_address_activity_use_map_btn.setOnClickListener {
            if (isServicesOk()) {
                initFun()
            }
        }

        add_address_activity_add_address_btn.setOnClickListener {

            if (add_address_activity_area_spinner.selectedItem == "Area" ||
                add_address_activity_city_spinner.selectedItem == "City"
            ) {
                if (add_address_activity_city_spinner.selectedItem == "City") {
                    Toast.makeText(
                        baseContext, "Please Select City",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (add_address_activity_area_spinner.selectedItem == "Area") {
                    Toast.makeText(
                        baseContext, "Please Select Area",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{

                addedAddress = add_address_activity_city_spinner.selectedItem.toString() + " " + add_address_activity_area_spinner.selectedItem.toString()
                val intent = Intent().apply {
                    putExtra("address", addedAddress)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    fun initFun() {
        val mapIntent = Intent(this, MapActivity::class.java)
        startActivityForResult(mapIntent, START_ACTIVITY_3_REQUEST_CODE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == START_ACTIVITY_3_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val address = data!!.getStringArrayExtra("address")

                var found = false
                for (iterator in cities.indices) {
                    if (address!![2].contains(cities[iterator], ignoreCase = true)) {
                        add_address_activity_city_spinner.setSelection(iterator, true)
                        found = true
                    }
                }
                if (!found) {
                    add_address_activity_city_spinner.setSelection(0, true)
                }

                for (iterator in areas.indices) {
                    if (address!![1].contains(areas[iterator], ignoreCase = true)) {
                        add_address_activity_area_spinner.post(Runnable {
                            add_address_activity_area_spinner.setSelection(
                                iterator
                            )
                        })
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun isServicesOk(): Boolean {
        val availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        return availability == ConnectionResult.SUCCESS
    }


    fun setAreaSpinner() {
        add_address_activity_area_spinner.adapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_dropdown_item,
            area
        ).also { adapter ->
           adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            add_address_activity_area_spinner.adapter = adapter
        }

        add_address_activity_area_spinner.onItemSelectedListener = object : OnItemClickListener,
            OnItemSelectedListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

        }
    }

    fun setCitySpinner() {
        add_address_activity_city_spinner.adapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_dropdown_item,
            cities
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            add_address_activity_city_spinner.adapter = adapter
        }

        add_address_activity_city_spinner.onItemSelectedListener = object : OnItemClickListener,
            OnItemSelectedListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                add_address_activity_city_spinner.setSelection(0)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (add_address_activity_city_spinner.selectedItem == "Cairo") {

                    area = cairoArea.toMutableList()

                } else if (add_address_activity_city_spinner.selectedItem == "Alexandria") {
                    area = alexArea.toMutableList()

                } else {
                    area = emptyArea.toMutableList()
                }

                setAreaSpinner()

            }

        }

    }

    override fun onBackPressed() {
        sendDataBackToPreviousActivity()
        super.onBackPressed()
    }

    private fun sendDataBackToPreviousActivity() {
        addedAddress = add_address_activity_city_spinner.selectedItem.toString() + " " + add_address_activity_area_spinner.selectedItem.toString()

        val resultIntent = Intent().apply {
            putExtra("address", addedAddress)
        }
        setResult(Activity.RESULT_OK, resultIntent)
    }


}