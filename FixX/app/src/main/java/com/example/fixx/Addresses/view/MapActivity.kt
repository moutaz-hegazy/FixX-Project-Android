package com.example.fixx.Addresses.view

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_map.*
import java.io.IOException
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {


    private val locationPermissionRequestCode = 1234
    private val DEFAULT_ZOOM = 15f

    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient


    var mLocationPermissionGranted = false

    var markedLocation: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        map_activity_search_input_txt.isFocusable = false
        placesSuggestions()
        getLocationPermission()

    }

    override fun onBackPressed() {
        sendDataBackToPreviousActivity()
        super.onBackPressed()
    }


    private fun sendDataBackToPreviousActivity() {
        val resultIntent = Intent().apply {
            putExtra(Constants.TRANS_ADDRESS, markedLocation)
        }
        setResult(Activity.RESULT_OK, resultIntent)
    }

    fun getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            if (mLocationPermissionGranted) {
                var loaction = mFusedLocationProviderClient.lastLocation
                loaction.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentLocation = task.result

                        if (currentLocation != null) {

                            val geocoder = Geocoder(this, Locale.getDefault())
                            val addresses =
                                geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 3)
                            if (addresses.size > 0) {
                                val address = addresses[0]
                              markedLocation = address.getAddressLine(0)
                            }

                            moveCamera(LatLng(currentLocation.latitude, currentLocation.longitude), DEFAULT_ZOOM,markedLocation)
                        }
                        else{
                        }
                    }
                }
            }
        } catch (e: SecurityException) {
        }
    }



    private fun getLocationPermission() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
            initMap()
        } else {
            ActivityCompat.requestPermissions(this, permissions, locationPermissionRequestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            locationPermissionRequestCode -> {
                if (grantResults.isNotEmpty()) {
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


    fun placesSuggestions(){
        Places.initialize(applicationContext, "AIzaSyCVOvMSNN18_AJKQjfKfoWKxsYNF5GNxK0")
        map_activity_search_input_txt.setOnClickListener {
            val fieldList: List<Place.Field> =
                listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
            val intent: Intent =
                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(this)
            startActivityForResult(intent, Constants.PLACES_AUTOCOMPLETE_REQUEST_CODE)
        }
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    @Override
    override fun onMapReady(googleMap: GoogleMap) {

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

            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses =
                geocoder.getFromLocation(it.latitude, it.longitude, 3)
            if (addresses.size > 0) {
                val address = addresses[0]
                markedLocation = address.getAddressLine(0)
            }
            val markerOptions = MarkerOptions()
            markerOptions.position(it)
            markerOptions.title(markedLocation)
            mMap.clear()

            moveCamera(it,DEFAULT_ZOOM,markedLocation )
            mMap.addMarker(markerOptions)
        }

        map_activity_add_address_btn.setOnClickListener {
            val intent = Intent().apply {
                putExtra(Constants.TRANS_ADDRESS, markedLocation)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun initFun() {
        map_activity_search_input_txt.setOnEditorActionListener { _, actionId, event ->
            if (event != null) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                    event.action == KeyEvent.ACTION_DOWN || event.action == KeyEvent.KEYCODE_ENTER) {
                    geoLocate()
                }
            }
            false
        }

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
                val address = addressList[0]

                    markedLocation = address.getAddressLine(0)

                val markerOptions = MarkerOptions()
                markerOptions.position(LatLng(address.latitude,address.longitude))
                markerOptions.title(markedLocation)
                mMap.clear()
                moveCamera(LatLng(address.latitude,address.longitude),DEFAULT_ZOOM,markedLocation)
                mMap.addMarker(markerOptions)
            }
        } catch (e: IOException) {
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            val place =
                Autocomplete.getPlaceFromIntent(data!!)
            if (requestCode == Constants.PLACES_AUTOCOMPLETE_REQUEST_CODE){
                map_activity_search_input_txt.setText(place.getAddress())
                mMap.clear()
                geoLocate()
            }
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






}