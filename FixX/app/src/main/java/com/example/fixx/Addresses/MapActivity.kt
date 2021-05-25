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
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
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

    var markedLocation: Array<String> = arrayOf("","","","")


    var gevornorate =""
    var city = ""
    var country = ""
    var district = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        getLocationPermission()
       // initPlacesSuggestion()

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

                           setAddressArray(LatLng(currentLocation.latitude,currentLocation.longitude))
                            moveCamera(LatLng(currentLocation.latitude, currentLocation.longitude), DEFAULT_ZOOM,"${district}, ${city}, ${gevornorate}, $country")
                        }
                        else{
                        }
                    }
//                    else {
//                        Toast.makeText(baseContext, "couldn't find location",
//                                Toast.LENGTH_SHORT).show()
//                    }
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

    fun initPlacesSuggestion(){
        // Initialize the SDK
      //  Places.initialize(applicationContext, getString(R.string.google_maps_api_key))

//        val placesClient = Places.createClient(this)
//
//        val autocompleteFragment =
//            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
//                    as AutocompleteSupportFragment
//
//        // Specify the types of place data to return.
//        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
//
//        // Set up a PlaceSelectionListener to handle the response.
//        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//            override fun onPlaceSelected(place: Place) {
//
//                Log.i("auto", "Place: ${place.name}, ${place.id}")
//            }
//
//            override fun onError(p0: Status) {
//
//                Log.i("auto", "An error occurred: $p0")
//            }
//        })



//        map_activity_search_input_txt.isFocusable = false
//
//        map_activity_search_input_txt.setOnClickListener {
//            val fieldList =
//                Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
//            val autocompleteIntent =
//                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(this)
//            startActivityForResult(autocompleteIntent,100)
//        }
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

            setAddressArray(LatLng(it.latitude,it.longitude))

            val markerOptions = MarkerOptions()
            markerOptions.position(it)
            markerOptions.title("${district}, ${city}, ${gevornorate}, $country")
            mMap.clear()

            moveCamera(it,DEFAULT_ZOOM,"${district}, ${city}, ${gevornorate}, $country" )
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
                val address = addressList.get(0)
//                Toast.makeText(baseContext, address.toString(),
//                        Toast.LENGTH_SHORT).show()

                moveCamera(LatLng(address.latitude,address.longitude),DEFAULT_ZOOM,address.getAddressLine(0))
            }
        } catch (e: IOException) {
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
//            val place = Autocomplete.getPlaceFromIntent(data!!)
//
//           // map_activity_search_input_txt.text = place.address.
//            map_activity_search_input_txt.setText(place.getAddress())
//        }
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

    private fun getdistrictName(lat: Double, lon: Double):String{
        var district:String = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var address = geoCoder.getFromLocation(lat,lon,3)

        district = address[0].locality

        return district
    }

    private fun getCityName(lat: Double, lon: Double):String{
        var cityName:String = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var address = geoCoder.getFromLocation(lat,lon,3)

        cityName = address[0].subAdminArea

        return cityName
    }

     private fun getGovernorateName(lat: Double, lon: Double):String{
        var governorate:String = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var address = geoCoder.getFromLocation(lat,lon,3)

        governorate = address[0].adminArea

        return governorate
    }

    private fun getCountryName(lat: Double, lon: Double):String{
        var countryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var address = geoCoder.getFromLocation(lat,lon,3)

        countryName = address[0].countryName

        return countryName
    }

    private fun setAddressArray(latlng: LatLng){

        district =  getdistrictName(latlng.latitude,latlng.longitude)
        city = getCityName(latlng.latitude,latlng.longitude)
        gevornorate = getGovernorateName(latlng.latitude,latlng.longitude)
        country = getCountryName(latlng.latitude,latlng.longitude)

        markedLocation[0] = district
        markedLocation[1] = city
        markedLocation[2] = gevornorate
        markedLocation[3] = country

    }

}