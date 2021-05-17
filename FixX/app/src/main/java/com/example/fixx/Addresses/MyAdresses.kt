package com.example.fixx.Addresses

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.Addresses.view.RecycleAdapter
import com.example.fixx.R
import kotlinx.android.synthetic.main.activity_my_adresses.*

class MyAdresses : AppCompatActivity(),RecycleAdapter.OnItemClickListener {

    companion object {
        const val START_ACTIVITY_2_REQUEST_CODE = 1
    }
    var myAdresses = mutableListOf<String>()
    private val adapter = RecycleAdapter(myAdresses,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_adresses)

        showAddressList()

        my_addresses_activity_add_address_btn.setOnClickListener {
            val addAddressIntent = Intent(this, AddAddressActivity::class.java)
            startActivityForResult(addAddressIntent,START_ACTIVITY_2_REQUEST_CODE);

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == START_ACTIVITY_2_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val address = data!!.getStringExtra("address")
                myAdresses.add(address!!.toString())

               showAddressList()

                adapter.notifyDataSetChanged()
                Toast.makeText(this, address, Toast.LENGTH_SHORT).show()

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        myAdresses[position] = "Clicked"
        adapter.notifyItemChanged(position)
    }

    fun showAddressList(){
        if (!myAdresses.isEmpty()){
            my_addresses_activity_no_address_txt.visibility = View.INVISIBLE

            my_addresses_recycler_view.adapter = adapter
            my_addresses_recycler_view.layoutManager = LinearLayoutManager(this)
            my_addresses_recycler_view.setHasFixedSize(true)

        }
    }


}