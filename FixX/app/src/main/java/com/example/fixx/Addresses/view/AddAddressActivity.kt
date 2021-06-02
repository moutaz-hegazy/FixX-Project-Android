package com.example.fixx.Addresses.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.fixx.Addresses.viewmodel.AddAddressViewmodel
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.android.synthetic.main.activity_add_address.*


class AddAddressActivity : AppCompatActivity() {


    private lateinit var addedAddress : String
    val cities = mutableListOf<String>()
    var area = mutableListOf<String>()
    val cairoArea = mutableListOf<String>()
    val alexArea = mutableListOf<String>()

    val emptyArea = arrayOf("")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        cities.addAll(
            arrayOf(
                getString(R.string.City),
                getString(R.string.Cairo),
                getString(R.string.Alexandria)
            )
        )
        cairoArea.addAll(
            arrayOf(
                getString(R.string.Area),
                getString(R.string.AlShrouk),
                getString(R.string.firstSettlement),
                getString(R.string.FifthSettlement),
                getString(R.string.Madenti),
                getString(R.string.AlRehab),
                getString(R.string.tenthOfRamadan),
                getString(R.string.BadrCity),
                getString(R.string.Zamalek),
                getString(R.string.Heliopolis),
                getString(R.string.NasserCity),
                getString(R.string.Qobbah),
                getString(R.string.Maadi),
                getString(R.string.Mokkatm),
                getString(R.string.Mohandsen),
                getString(R.string.ShekhZayed),
                getString(R.string.Dokki),
                getString(R.string.GizaSquare),
                getString(R.string.Haram),
                getString(R.string.Fissal),
                getString(R.string.Shobra),
                getString(R.string.Obour),
                getString(R.string.Matareya),
                getString(R.string.sixthOctober),
                getString(R.string.Helwan),
                getString(R.string.AinShams),
                getString(R.string.Manyal),
                getString(R.string.Agouza)
            )
        )


        alexArea.addAll(
            arrayOf(
                getString(R.string.Area),
                getString(R.string.Riyadah),
                getString(R.string.MoharamBek),
                getString(R.string.AbuQir),
                getString(R.string.Montaza),
                getString(R.string.AlHadarah),
                getString(R.string.AlIbrahimeyah),
                getString(R.string.Asafra),
                getString(R.string.AlAzaritah),
                getString(R.string.Bahari),
                getString(R.string.Dekhela),
                getString(R.string.Bokli),
                getString(R.string.BorgAlArab),
                getString(R.string.AlQabari),
                getString(R.string.Fleming),
                getString(R.string.Janklees),
                getString(R.string.Gleem),
                getString(R.string.KafrAbdou),
                getString(R.string.Louran),
                getString(R.string.ElMandara),
                getString(R.string.Miami),
                getString(R.string.SanStifano),
                getString(R.string.SidiBeshr),
                getString(R.string.SidiGaber),
                getString(R.string.Shatebi),
                getString(R.string.Sporting),
                getString(R.string.Victoria),
                getString(R.string.Smouha),
                getString(R.string.Stanli),
                getString(R.string.WaborElMaya),
                getString(R.string.ElHanovil),
                getString(R.string.ElBitash),
                getString(R.string.QismBabSharqi),
                getString(R.string.Mansheya),
                getString(R.string.AlAttarin),
                getString(R.string.FirstAlRaml),
                getString(R.string.MustafaKamel)
            )
        )



        supportActionBar?.apply {
            title = getString(R.string.addAddressTitle)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF6200EE")))
        }


        setCitySpinner()


        add_address_activity_use_map_btn.setOnClickListener {
            if (isServicesOk()) {
                initFun()
            }
        }

        add_address_activity_add_address_btn.setOnClickListener {

            if (add_address_activity_area_spinner.selectedItem == getString(R.string.Area) ||
                add_address_activity_city_spinner.selectedItem == getString(R.string.City)
            ) {
                if (add_address_activity_city_spinner.selectedItem == getString(R.string.City)) {
                    Toast.makeText(
                        baseContext, getString(R.string.selectCity),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (add_address_activity_area_spinner.selectedItem == getString(R.string.Area)) {
                    Toast.makeText(
                        baseContext, getString(R.string.selectArea),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {

                sendDataBackToPreviousActivity()
            }
        }
    }

    private fun initFun() {
        val mapIntent = Intent(this, MapActivity::class.java)
        startActivityForResult(mapIntent, Constants.START_ADDRESS_MAP_REQUEST_CODE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.START_ADDRESS_MAP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val address = data!!.getStringArrayExtra(Constants.TRANS_ADDRESS)

                var cityFound = false
                var areaFound = false
                for (iterator in cities.indices) {
                    if (address!![2].contains(cities[iterator], ignoreCase = true)) {
                        add_address_activity_city_spinner.setSelection(iterator, true)
                        cityFound = true
                    }
                }
                if (!cityFound) {
                    add_address_activity_city_spinner.setSelection(0, true)
                }

                if (cityFound && add_address_activity_city_spinner.selectedItem == getString(R.string.Cairo)) {
                    for (iterator in cairoArea.indices) {
                        if (address!![0].contains(
                                cairoArea[iterator],
                                ignoreCase = true
                            )
                        ) {
                            add_address_activity_area_spinner.post(Runnable {
                                add_address_activity_area_spinner.setSelection(
                                    iterator
                                )
                            })
                            areaFound = true
                        }
                    }

                    if (!areaFound) {
                        for (iterator in cairoArea.indices) {
                            if (address!![1].contains(
                                    cairoArea[iterator],
                                    ignoreCase = true
                                )
                            ) {
                                add_address_activity_area_spinner.post(Runnable {
                                    add_address_activity_area_spinner.setSelection(
                                        iterator
                                    )
                                })
                                areaFound = true
                            }
                        }
                    }
                } else if (cityFound && add_address_activity_city_spinner.selectedItem == getString(
                        R.string.Alexandria
                    )
                ) {
                    for (iterator in alexArea.indices) {
                        if (address!![0].contains(alexArea[iterator], ignoreCase = true)
                        ) {
                            add_address_activity_area_spinner.post(Runnable {
                                add_address_activity_area_spinner.setSelection(
                                    iterator
                                )
                            })
                            areaFound = true
                        }
                    }

                    if (!areaFound) {
                        for (iterator in alexArea.indices) {
                            if (address!![1].contains(
                                    alexArea[iterator],
                                    ignoreCase = true
                                )
                            ) {
                                add_address_activity_area_spinner.post(Runnable {
                                    add_address_activity_area_spinner.setSelection(
                                        iterator
                                    )
                                })
                                areaFound = true
                            }
                        }
                    }

                }


                if (!areaFound) {
                    add_address_activity_area_spinner.post(Runnable {
                        add_address_activity_area_spinner.setSelection(0)
                    })
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun isServicesOk(): Boolean {
        val availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        return availability == ConnectionResult.SUCCESS
    }


    private fun setAreaSpinner() {
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

    private fun setCitySpinner() {
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

                area = when (add_address_activity_city_spinner.selectedItem) {
                    getString(R.string.Cairo) -> {

                        cairoArea.toMutableList()

                    }
                    getString(R.string.Alexandria) -> {
                        alexArea.toMutableList()

                    }
                    else -> {
                        emptyArea.toMutableList()
                    }
                }

                setAreaSpinner()

            }

        }

    }

    private fun sendDataBackToPreviousActivity() {
        val addressName = add_address_activity_address_name_txt.text.toString()

        addedAddress = addressName + "%" + add_address_activity_city_spinner.selectedItem.toString() + "," +
                add_address_activity_area_spinner.selectedItem.toString()

        if(!add_address_activity_street_txt.text.isNullOrEmpty()){
            addedAddress += ": ${add_address_activity_street_txt.text}"
        }
        if(!add_address_activity_building_number_txt.text.isNullOrEmpty()){
            addedAddress += ", ${add_address_activity_building_number_txt.text}"
        }
        if(!add_address_activity_floor_txt.text.isNullOrEmpty()){
            addedAddress += ", ${add_address_activity_floor_txt.text}"
        }
        if(!add_address_activity_notes_txt.text.isNullOrEmpty()){
            addedAddress += ", ${add_address_activity_notes_txt.text}"
        }

        AddAddressViewmodel(addedAddress,onSuccessBinding ={
            Intent().apply {
                putExtra(Constants.TRANS_ADDRESS, addedAddress)
            }.also {
                setResult(Activity.RESULT_OK, it)
                finish()
            }

        },onFailBinding = {
            Toast.makeText(this, "upload failed", Toast.LENGTH_SHORT)
        })
    }


}