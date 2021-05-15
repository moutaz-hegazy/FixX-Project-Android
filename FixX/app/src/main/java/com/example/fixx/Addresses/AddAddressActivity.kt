package com.example.fixx.Addresses

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.*
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
        val governorates = arrayOf("Cairo", "Alexandria", "El Beheira","Giza")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        add_address_activity_governorate_spinner.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
            governorates)

        add_address_activity_governorate_spinner.onItemSelectedListener = object: OnItemClickListener,
            OnItemSelectedListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

        }
        add_address_activity_use_map_btn.setOnClickListener {
            if (isServicesOk()) {
                initFun()
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
                add_address_activity_city_txt.setText(address!![1])
                add_address_activity_district_txt.setText(address!![0])
                for (iterator in governorates.indices){
                    if(address!![2] == governorates[iterator]+" Governorate"){
                        add_address_activity_governorate_spinner.setSelection(iterator)
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




}