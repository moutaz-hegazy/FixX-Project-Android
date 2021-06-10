package com.example.fixx.Addresses.view

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fixx.Addresses.viewmodel.AddAddressViewmodel
import androidx.core.content.ContextCompat
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.android.synthetic.main.activity_add_address.*


class AddAddressActivity : AppCompatActivity() {


    private lateinit var addedAddress: String
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
                getString(R.string.Smouha),
                getString(R.string.SidiGaber),
                getString(R.string.Shatebi),
                getString(R.string.Sporting),
                getString(R.string.Victoria),
                getString(R.string.Stanli),
                getString(R.string.WaborElMaya),
                getString(R.string.ElHanovil),
                getString(R.string.ElBitash),
                getString(R.string.QismBabSharqi),
                getString(R.string.Mansheya),
                getString(R.string.AlAttarin),
                getString(R.string.FirstAlRaml),
                getString(R.string.MustafaKamel),
                getString(R.string.EzbetSaad),
                getString(R.string.Abis)
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

                add_address_activity_address_name_txt.setText("")
                add_address_activity_notes_txt.setText("")
                add_address_activity_building_number_txt.setText("")
                add_address_activity_floor_txt.setText("")
                add_address_activity_street_txt.setText("")

                val address = data!!.getStringExtra(Constants.TRANS_ADDRESS)

                var cityFound = false
                var areaFound = false

                for (iterator in Constants.cities.indices) {
                    if (address!!.contains(Constants.cities[iterator], ignoreCase = true)
                        || address!!.contains(Constants.citiesInArabic[iterator], ignoreCase = true)
                    ) {
                        add_address_activity_city_spinner.setSelection(iterator, true)
                        cityFound = true
                        break
                    }
                }

                if (!cityFound) {
                    add_address_activity_city_spinner.setSelection(0, true)
                }

                if (cityFound && add_address_activity_city_spinner.selectedItem == getString(R.string.Cairo)) {
                    for (iterator in Constants.cairoArea.indices) {
                        if (address!!.contains(
                                Constants.cairoArea[iterator],
                                ignoreCase = true
                            ) || address!!.contains(
                                Constants.cairoAreaInArabic[iterator],
                                ignoreCase = true
                            )
                        ) {
                            add_address_activity_area_spinner.post(Runnable {
                                add_address_activity_area_spinner.setSelection(
                                    iterator
                                )
                            })
                            areaFound = true
                            break
                        }
                    }

                } else
                    if (cityFound && add_address_activity_city_spinner.selectedItem == getString(
                            R.string.Alexandria
                        )
                    ) {
                        for (iterator in Constants.alexArea.indices) {
                            if (address!!.contains(Constants.alexArea[iterator], ignoreCase = true)
                                || address!!.contains(
                                    Constants.alexAreaInArabic[iterator],
                                    ignoreCase = true
                                )
                            ) {
                                add_address_activity_area_spinner.post(Runnable {
                                    add_address_activity_area_spinner.setSelection(
                                        iterator
                                    )
                                })
                                areaFound = true
                                break
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
        add_address_activity_area_spinner.adapter = MySpinnerAdapter<String>(
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
        add_address_activity_city_spinner.adapter = MySpinnerAdapter<String>(
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

        addedAddress =
            addressName + "%" + add_address_activity_city_spinner.selectedItem.toString() + "," +
                    add_address_activity_area_spinner.selectedItem.toString() + "/"

        if (!add_address_activity_street_txt.text.isNullOrEmpty()) {
            addedAddress += "street: ${add_address_activity_street_txt.text}"
        }
        if (!add_address_activity_building_number_txt.text.isNullOrEmpty()) {
            addedAddress += ",building: ${add_address_activity_building_number_txt.text}"
        }
        if (!add_address_activity_floor_txt.text.isNullOrEmpty()) {
            addedAddress += ",floor: ${add_address_activity_floor_txt.text}"
        }
        if (!add_address_activity_notes_txt.text.isNullOrEmpty()) {
            addedAddress += ", ${add_address_activity_notes_txt.text}"
        }

        AddAddressViewmodel(addedAddress, onSuccessBinding = {
            Intent().apply {
                putExtra(Constants.TRANS_ADDRESS, addedAddress)
            }.also {
                setResult(Activity.RESULT_OK, it)
                finish()
            }

        }, onFailBinding = {
            Toast.makeText(this, "upload failed", Toast.LENGTH_SHORT)
        })
    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(getString(R.string.leaveDialogTitle))
        //set message for alert dialog
        builder.setMessage(getString(R.string.leaveDialogMsg))

        builder.setPositiveButton(getString(R.string.exit)){ _, _ ->
            super.onBackPressed()
        }
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


    private fun setAddedCity(spinnerCity: String): String {
        var city = spinnerCity
        var cityFound = false
        for (iterator in Constants.cities.indices) {
            if (spinnerCity.equals(Constants.cities[iterator])) {
                cityFound = true
            }
        }
        if (!cityFound) {
            for (iterator in Constants.citiesInArabic.indices) {
                if (spinnerCity.equals(Constants.citiesInArabic[iterator])) {
                    city = getCityEnglishName(spinnerCity)
                }
            }
        }

        return city
    }

    private fun setAddedArea(spinnerArea: String, spinnerCity: String): String {
        var area = spinnerArea
        var areaFound = false

        if (spinnerCity.equals(getString(R.string.Alexandria))) {
            for (iterator in Constants.alexArea.indices) {
                if (spinnerArea.equals(Constants.alexArea[iterator])) {
                    areaFound = true
                }
            }
            if (!areaFound) {
                for (iterator in Constants.alexAreaInArabic.indices) {
                    if (spinnerArea.equals(Constants.alexAreaInArabic[iterator])) {
                        area = getAreaEnglishName(spinnerArea, spinnerCity)
                    }
                }
            }
        } else if (spinnerCity.equals(getString(R.string.Cairo))) {
            for (iterator in Constants.cairoArea.indices) {
                if (spinnerArea.equals(Constants.cairoArea[iterator])) {
                    areaFound = true
                }
            }
            if (!areaFound) {
                for (iterator in Constants.cairoAreaInArabic.indices) {
                    if (spinnerArea.equals(Constants.cairoAreaInArabic[iterator])) {
                        area = getAreaEnglishName(spinnerArea, spinnerCity)
                    }
                }
            }
        }


        return area
    }

    private fun getCityEnglishName(city: String): String {
        var myCity = city
        for (iterator in Constants.citiesInArabic.indices) {
            if (city.equals(Constants.citiesInArabic[iterator])) {
                myCity = Constants.cities[iterator]
            }
        }
        return myCity
    }

    private fun getAreaEnglishName(area: String, city: String): String {
        var myArea = area

        if (city.equals(getString(R.string.Cairo))) {
            for (iterator in Constants.cairoAreaInArabic.indices) {
                if (area.equals(Constants.cairoAreaInArabic[iterator])) {
                    myArea = Constants.cairoArea[iterator]
                }
            }
        } else if (city.equals(getString(R.string.Alexandria))) {
            for (iterator in Constants.alexAreaInArabic.indices) {
                if (area.equals(Constants.alexAreaInArabic[iterator])) {
                    myArea = Constants.alexArea[iterator]
                }
            }
        }

        return myArea
    }
}