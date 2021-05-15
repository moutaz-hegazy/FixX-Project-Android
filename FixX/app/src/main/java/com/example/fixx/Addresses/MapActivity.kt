package com.example.fixx.Addresses

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fixx.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*
import java.io.IOException
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {


    val locarionPermissionRequestCode = 1234
    val DEFAULT_ZOOM = 15f

    val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    var mLocationPermissionGranted = false

    var markedLocation: Array<String> = arrayOf("","","","")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        getLocationPermission()

    }

    override fun onBackPressed() {
        sendDataBackToPreviousActivity()
        super.onBackPressed()
    }

    private fun sendDataBackToPreviousActivity() {
        val resultIntent = Intent().apply {
            putExtra("address", markedLocation)
        }
        setResult(Activity.RESULT_OK, resultIntent)
    }

    fun getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            if (mLocationPermissionGranted) {
                val loaction = mFusedLocationProviderClient.lastLocation
                loaction.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "location found",
                                Toast.LENGTH_SHORT).show()
                        val currentLocation = task.result
                        if (currentLocation != null) {

                            val district =  getdistrictName(currentLocation.latitude,currentLocation.longitude)
                            val city = getCityName(currentLocation.latitude,currentLocation.longitude)
                            val governorate = getGovernorateName(currentLocation.latitude,currentLocation.longitude)
                            val country = getCountryName(currentLocation.latitude,currentLocation.longitude)

                            markedLocation[0] = district
                            markedLocation[1] = city
                            markedLocation[2] = governorate
                            markedLocation[3] = country

                            moveCamera(LatLng(currentLocation.latitude, currentLocation.longitude), DEFAULT_ZOOM,"${district}, ${city}, ${governorate}, ${country}")
                        }
                    } else {
                        Toast.makeText(baseContext, "couldn't find location",
                                Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: SecurityException) {
        }
    }

    fun getLocationPermission() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
            initMap()
        } else {
            ActivityCompat.requestPermissions(this, permissions, locarionPermissionRequestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            locarionPermissionRequestCode -> {
                if (grantResults.size > 0) {
                    for (i in grantResults) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false
                            return
                        }
                    }
                    mLocationPermissionGranted = true
                    initMap()
                }
            }
        }

    }

    fun initMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    @Override
    override fun onMapReady(googleMap: GoogleMap) {
        Toast.makeText(baseContext, "map is ready",
                Toast.LENGTH_SHORT).show()

        mMap = googleMap

        if (mLocationPermissionGranted) {
            getDeviceLocation()
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = false
            initFun()
        }

        mMap.setOnMapClickListener {

            val district =  getdistrictName(it.latitude,it.longitude)
            val city = getCityName(it.latitude,it.longitude)
            val governorate = getGovernorateName(it.latitude,it.longitude)
            val country = getCountryName(it.latitude,it.longitude)

            markedLocation[0] = district
            markedLocation[1] = city
            markedLocation[2] = governorate
            markedLocation[3] = country

            val markerOptions = MarkerOptions()
            markerOptions.position(it)
            markerOptions.title("${district}, ${city}, ${governorate}, ${country}")
            mMap.clear()

            moveCamera(it,DEFAULT_ZOOM,"${district}, ${city}, ${governorate}, ${country}" )
            mMap.addMarker(markerOptions)
        }

        map_activity_add_address_btn.setOnClickListener {
            val intent = Intent().apply {
                putExtra("address", markedLocation)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    fun initFun() {
        map_activity_search_input_txt.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (event != null) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                            event.action == KeyEvent.ACTION_DOWN || event.action == KeyEvent.KEYCODE_ENTER) {
                        geoLocate()
                    }
                }
                return false
            }
        })

        map_activity_gps_icon_img.setOnClickListener {
            mMap.clear()
            getDeviceLocation()
        }

        hideSoftKeyboard()
    }

    private fun geoLocate() {
        val searchInput = map_activity_search_input_txt.text.toString()
        val geocoder = Geocoder(this)
        var addressList: ArrayList<Address>
        try {
            addressList = geocoder.getFromLocationName(searchInput, 1) as ArrayList<Address>
            if (addressList.size > 0) {
                val address = addressList.get(0)
                Toast.makeText(baseContext, address.toString(),
                        Toast.LENGTH_SHORT).show()

                moveCamera(LatLng(address.latitude,address.longitude),DEFAULT_ZOOM,address.getAddressLine(0))
            }
        } catch (e: IOException) {
        }


    }

    private fun hideSoftKeyboard(){
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun moveCamera(latLng: LatLng, zoom: Float, title: String) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))

            val options = MarkerOptions()
                .position(latLng)
                .title(title)
            mMap.clear()
            mMap.addMarker(options)

        hideSoftKeyboard()
    }

    fun getdistrictName(lat: Double, lon: Double):String{
        var district:String = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,lon,3)

        district = Adress.get(0).locality

        return district
    }

    fun getCityName(lat: Double, lon: Double):String{
        var cityName:String = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,lon,3)

        cityName = Adress.get(0).subAdminArea

        return cityName
    }

     fun getGovernorateName(lat: Double, lon: Double):String{
        var gevornarate:String = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,lon,3)

        gevornarate = Adress.get(0).adminArea

        return gevornarate
    }

    fun getCountryName(lat: Double, lon: Double):String{
        var countryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,lon,3)

        countryName = Adress.get(0).countryName

        return countryName
    }

}