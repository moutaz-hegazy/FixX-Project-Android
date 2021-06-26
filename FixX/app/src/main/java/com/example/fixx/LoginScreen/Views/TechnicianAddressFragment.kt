package com.example.fixx.LoginScreen.Views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.Addresses.view.MySpinnerAdapter
import com.example.fixx.LoginScreen.viewmodels.RegisterViewmodel
import com.example.fixx.NavigationBar.NavigationBarActivity
import com.example.fixx.POJOs.Details
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_tecnician_address.*
import kotlinx.android.synthetic.main.fragment_tecnician_address.technician_address_fragment_recycler_view

class TechnicianAddressFragment : Fragment() {

    var addresses = arrayListOf<String>()

    var cities = mutableListOf<String>()
    var area = mutableListOf<String>()
    var cairoArea = mutableListOf<String>()
    var alexArea = mutableListOf<String>()
    val emptyArea = arrayOf("")
    private var fromGoogle = false

    private val adapter = TechnicianAddressAdapter(addresses)
    private val viewmodel : RegisterViewmodel by lazy {
        RegisterViewmodel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fromGoogle = arguments?.getBoolean(Constants.TRANS_GOOGLE_BOOL,false) ?: false

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
                getString(R.string.MustafaKamel),
                getString(R.string.EzbetSaad),
                getString(R.string.Abis)
            )
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tecnician_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCitySpinner()
        showAddressList()

        pick_address_fragment_add_address_btn.setOnClickListener {
            if(pick_address_fragment_city_spinner.selectedItem == getString(R.string.City)){
                Toast.makeText(
                    context, getString(R.string.selectCity),
                    Toast.LENGTH_SHORT
                ).show()
            }else if (pick_address_fragment_area_spinner.selectedItem == getString(R.string.Area)){
                Toast.makeText(
                    context, getString(R.string.selectArea),
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                var addressFound = false
                for (add in addresses.indices){
                    val address = pick_address_fragment_city_spinner.selectedItem.toString()+"," + pick_address_fragment_area_spinner.selectedItem.toString()
                    if (address == addresses[add]){
                        addressFound = true
                    }
                }
                if (!addressFound){
                    addresses.add(pick_address_fragment_city_spinner.selectedItem.toString()+"," + pick_address_fragment_area_spinner.selectedItem.toString())
                    showAddressList()
                }
                pick_address_fragment_city_spinner.setSelection(0)
                pick_address_fragment_area_spinner.setSelection(0)

            }

        }

        pick_address_fragment_next_btn.setOnClickListener {
            if(addresses.isNotEmpty()){
                if(fromGoogle){
                    uploadUserData()
                }else {
                    arguments?.putStringArrayList(Constants.TRANS_ADDRESS, addresses)
                    SignUpTabFragment().apply {
                        this.arguments = this@TechnicianAddressFragment.arguments
                    }.also {
                        fragmentManager?.beginTransaction()
                            ?.replace(R.id.pick_tecnician_address_fragment, it)?.commit()
                    }
                }
            }else{
                Toast.makeText(context, getString(R.string.AddAddress),Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadUserData(){
        val technician: Technician = createTechnicianObject()
        FirestoreService.saveUserData(technician,
            onSuccessHandler = {
                    person ->
                (person as? Technician)?.apply {
                    workLocations?.forEach {
                        viewmodel.subscribeToTopic(this.jobTitle+getWorkTopic(it))
                    }
                }
                NavigationBarActivity.USER_OBJECT = person
                startActivity(Intent(context, NavigationBarActivity::class.java))
                activity?.finish()
            },onFailHandler = {
                Toast.makeText(context,"Register failed",Toast.LENGTH_SHORT)
            })
    }

    private fun createTechnicianObject() : Technician{
        val userData = arguments?.getSerializable(Constants.TRANS_USERDATA) as Details
        val passedPhoneNumber = userData.phoneNumber
        val passedAccountType = userData.accountType
        val username = arguments?.getString(Constants.TRANS_USER_NAME) ?: ""
        val email = FirebaseAuth.getInstance().currentUser?.email!!

        return Technician(passedPhoneNumber, passedAccountType, username, email).apply {
            jobTitle = arguments?.getString(Constants.TRANS_JOB)
            workLocations = arrayListOf()
            addresses.forEach {
                workLocations?.add("${getCityEnglishName(it.substringBefore(","))},${getAreaEnglishName(it.substringAfter(","),it.substringBefore(","))}")
            }
        }
    }
    private fun showAddressList(){
        technician_address_fragment_recycler_view.adapter = adapter
        technician_address_fragment_recycler_view.layoutManager = LinearLayoutManager(context)
        technician_address_fragment_recycler_view.setHasFixedSize(true)
    }

    private fun setAreaSpinner() {

        pick_address_fragment_area_spinner.adapter = MySpinnerAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_dropdown_item,
            area
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            pick_address_fragment_area_spinner.adapter = adapter
        }

        pick_address_fragment_area_spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
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
        pick_address_fragment_city_spinner.adapter = MySpinnerAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_dropdown_item,
            cities
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            pick_address_fragment_city_spinner.adapter = adapter
        }

        pick_address_fragment_city_spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                pick_address_fragment_city_spinner.setSelection(0)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                area = when (pick_address_fragment_city_spinner.selectedItem) {
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