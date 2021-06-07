package com.example.fixx.WorkAddresses.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.CURRENT_LANGUAGE
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.WorkAddresses.viewmodels.WorkAddressesViewmodel
import com.example.fixx.constants.Constants
import kotlinx.android.synthetic.main.activity_my_work_addresses.*

class MyWorkAddresses : AppCompatActivity(), WorkAddressesAdapter.OnItemClickListener {

    private var myWorkAddresses = mutableListOf<String>()
    private lateinit var adapter : WorkAddressesAdapter
    private lateinit var viewmodel : WorkAddressesViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_work_addresses)

        viewmodel = WorkAddressesViewmodel(USER_OBJECT!!.uid!!)
        supportActionBar?.apply {
            title = getString(R.string.myWokAddressesTitle)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF6200EE")))
        }

        (USER_OBJECT as? Technician)?.workLocations?.let{
            if(CURRENT_LANGUAGE == "en"){
                myWorkAddresses.addAll(it)
            }else {
                myWorkAddresses.addAll(getLocationsInArabic(it))
            }
        }

        adapter = WorkAddressesAdapter(myWorkAddresses,this){
            deleteLocation(it)
        }

        showAddressList()

        my_work_addresses_activity_add_address_btn.setOnClickListener {
            val addAddressIntent = Intent(this, AddWorkAddress::class.java)
            startActivityForResult(addAddressIntent, Constants.START_WORK_ADDRESS_ACTIVITY_REQUEST_CODE)

        }
    }

    private fun deleteLocation(position: Int){
        val location = (USER_OBJECT as? Technician)?.workLocations?.get(position) ?: ""
        Log.i("TAG", "deleteLocation: >>>>>> $location")
        viewmodel.removeWorkLocation(location, onSuccessBinding = {
            myWorkAddresses.removeAt(position)
            (USER_OBJECT as? Technician)?.workLocations?.removeAt(position)
            adapter.notifyDataSetChanged()
            val topic = (USER_OBJECT!! as Technician).jobTitle!!+getWorkTopic(location)
            viewmodel.unsubscribeFromTopic(topic)
        },onFailBinding = {
            Toast.makeText(this, R.string.LocationRemoveFailed, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.START_WORK_ADDRESS_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val address = data!!.getStringExtra(Constants.TRANS_ADDRESS)
                if(address.isNullOrEmpty()){

                }else if(!myWorkAddresses.contains(address)){
                    viewmodel.addNewWorkLocation(getEnglishAddress(address), onSuccessBinding = {
                        myWorkAddresses.add(address)
                        (USER_OBJECT as? Technician)?.apply {
                            Log.i("TAG", "onActivityResult: IAM TECHNICIAN <<<<<<<$address")
                            workLocations?.add(address)
                            val topic = (USER_OBJECT!! as Technician).jobTitle!!+getWorkTopic(address)
                            Log.i("TAG", "onActivityResult: >>>>$topic")
                            viewmodel.subscribeToTopic(topic)
                        }
                        showAddressList()
                        adapter.notifyDataSetChanged()
                    }, onFailBinding = {
                        Toast.makeText(this, R.string.WorkLocationAddFail, Toast.LENGTH_SHORT)
                            .show()
                    })
                }else{
                    Toast.makeText(this, R.string.AddressExists, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getEnglishAddress(address: String) : String{
        val city = address.substringBefore(",")
        return "${getCityEnglishName(city)},${getAreaEnglishName(address.substringAfter(","),city)}"
    }

    private fun showAddressList(){
        if (myWorkAddresses.isNotEmpty()){
            my_work_addresses_activity_no_address_txt.visibility = View.INVISIBLE

            my_work_addresses_activity_recycler_view.adapter = adapter
            my_work_addresses_activity_recycler_view.layoutManager = LinearLayoutManager(this)
            my_work_addresses_activity_recycler_view.setHasFixedSize(true)

        }
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, getString(R.string.longPressToast), Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(position: Int) {
        confirmDeleteDialog(position,myWorkAddresses)
    }


    private fun getLocationsInArabic(locations : List<String>) = locations.map {
        getArabicLocation(it)
    }

    private fun getArabicLocation(address : String): String {
        val city = address.substringBefore(",")
        val area = address.substringAfter(",")
        val arCity = Constants.citiesInArabic[Constants.cities.indexOf(city)]
        val arArea = if(city == "Cairo"){
            Constants.cairoAreaInArabic[Constants.cairoArea.indexOf(area)]
        }else if(city == "Alexandria"){
            Constants.alexAreaInArabic[Constants.alexArea.indexOf(area)]
        }else{
            ""
        }
        return "$arCity,$arArea"
    }

    private fun confirmDeleteDialog(position: Int, list: MutableList<String>){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(getString(R.string.deleteLocaionTitle))
        //set message for alert dialog
        builder.setMessage(getString(R.string.deleteLocationQuestion))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(getString(R.string.yes)){ _, _ ->
            deleteLocation(position)
        }
        //performing negative action
        builder.setNegativeButton(getString(R.string.no)){ _, _ ->

        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
        alertDialog.window!!.setBackgroundDrawableResource(R.drawable.btn_border)

    }

    fun getWorkTopic(location: String) : String{
        val city = location.substringBefore(",")
        val area = location.substringAfter(",")

        return "%"+getCityEnglishName(city).replace(" ","_", true)+"."+
                getAreaEnglishName(area,city).replace(" ","_",true).
                replace("-","_",true)
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