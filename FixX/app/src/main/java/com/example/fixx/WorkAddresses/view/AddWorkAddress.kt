package com.example.fixx.WorkAddresses.view


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.example.fixx.Addresses.view.MySpinnerAdapter
import com.example.fixx.R
import com.example.fixx.constants.Constants
import kotlinx.android.synthetic.main.activity_add_work_address.*

class AddWorkAddress : AppCompatActivity() {

    private lateinit var addedAddress: String
    val cities = mutableListOf<String>()
    var area = mutableListOf<String>()
    val cairoArea = mutableListOf<String>()
    val alexArea = mutableListOf<String>()

    val emptyArea = arrayOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work_address)

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
            title = getString(R.string.addWorkAddress)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF6200EE")))
        }

        setCitySpinner()

        add_work_address_activity_add_address_btn.setOnClickListener {

            if (add_work_address_activity_area_spinner.selectedItem == getString(R.string.Area) ||
                add_work_address_activity_city_spinner.selectedItem == getString(R.string.City)
            ) {
                if (add_work_address_activity_city_spinner.selectedItem == getString(R.string.City)) {
                    Toast.makeText(
                        baseContext, getString(R.string.selectCity),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (add_work_address_activity_area_spinner.selectedItem == getString(R.string.Area)) {
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


    private fun setAreaSpinner() {
        add_work_address_activity_area_spinner.adapter = MySpinnerAdapter<String>(
            this, android.R.layout.simple_spinner_dropdown_item,
            area
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            add_work_address_activity_area_spinner.adapter = adapter

        }

        add_work_address_activity_area_spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
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
        add_work_address_activity_city_spinner.adapter = MySpinnerAdapter<String>(
            this, android.R.layout.simple_spinner_dropdown_item,
            cities
        ).also { adapter ->


            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            add_work_address_activity_city_spinner.adapter = adapter

        }

        add_work_address_activity_city_spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                add_work_address_activity_city_spinner.setSelection(0)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                area = when (add_work_address_activity_city_spinner.selectedItem) {
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

        addedAddress =
            add_work_address_activity_city_spinner.selectedItem.toString() + "," +
                    add_work_address_activity_area_spinner.selectedItem.toString()


            Intent().apply {
                putExtra(Constants.TRANS_ADDRESS,addedAddress)
            }.also {
                setResult(Activity.RESULT_OK, it)
                finish()
            }
    }
}