package com.example.fixx.LoginScreen.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.R
import kotlinx.android.synthetic.main.fragment_tecnician_address.*
import kotlinx.android.synthetic.main.fragment_tecnician_address.technician_address_fragment_recycler_view

class TecnicianAddressFragment : Fragment() {

    var addresses = mutableListOf<String>()

    var cities = arrayOf<String>()
    var area = mutableListOf<String>()
    var cairoArea = arrayOf<String>()
    var alexArea = arrayOf<String>()
    var emptyArea = arrayOf<String>()
    var areas = arrayOf<String>()

    private val adapter = TechnicianAddressAdapter(addresses)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cities = arrayOf("City", "Cairo", "Alexandria")

        cairoArea = arrayOf(
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

        alexArea = arrayOf(
            "Area",
            "Moharam Bek",
            "Abu Qir",
            "Montaza",
            "Al Hadarah",
            "Al Ibrahimeyah",
            "Asafra",
            "Al Azaritah",
            "Bahari",
            "Dekhela",
            "Bokli",
            "Borg Al Arab",
            "Al Qabari",
            "Fleming",
            "Janklees",
            "Gleem",
            "Kafr Abdou",
            "Louran",
            "El Mandara",
            "Miami",
            "San Stifano",
            "Sidi Beshr",
            "Sidi Gaber",
            "Shatebi",
            "Sporting",
            "Victoria",
            "Smouha",
            "Stanli",
            "Wabor El Maya",
            "El Hanovil",
            "El Bitash",
            "Qism Bab Sharqi",
            //"Qism El-Raml",
            "Mansheya",
            "Al Attarin",
            "First Al Raml",
            "Mustafa Kamel"
        )

        emptyArea = arrayOf("Other")

        areas = alexArea + cairoArea

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
            addresses.add(pick_address_fragment_city_spinner.selectedItem.toString() + pick_address_fragment_area_spinner.selectedItem.toString())
            showAddressList()
            pick_address_fragment_city_spinner.setSelection(0)
            pick_address_fragment_area_spinner.setSelection(0)
        }

        pick_address_fragment_next_btn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.pick_tecnician_address_fragment, SignUpTabFragment())?.commit()
        }
    }

    private fun showAddressList(){
        technician_address_fragment_recycler_view.adapter = adapter
        technician_address_fragment_recycler_view.layoutManager = LinearLayoutManager(context)
        technician_address_fragment_recycler_view.setHasFixedSize(true)
    }

    private fun setAreaSpinner() {

        pick_address_fragment_area_spinner.adapter = ArrayAdapter<String>(
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
        pick_address_fragment_city_spinner.adapter = ArrayAdapter<String>(
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
                    "Cairo" -> {
                        cairoArea.toMutableList()

                    }
                    "Alexandria" -> {
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
}