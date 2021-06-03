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
import com.example.fixx.R
import com.example.fixx.constants.Constants
import kotlinx.android.synthetic.main.activity_my_work_addresses.*

class MyWorkAddresses : AppCompatActivity(), WorkAddressesAdapter.OnItemClickListener {

    var myWorkAddresses = mutableListOf<String>()
    private lateinit var adapter : WorkAddressesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_work_addresses)

        supportActionBar?.apply {
            title = getString(R.string.myWokAddressesTitle)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF6200EE")))
        }

//        NavigationBarActivity.USER_OBJECT?.locations?.let{
//            myWorkAddresses.addAll(it)
//        }

        adapter = WorkAddressesAdapter(myWorkAddresses,this)

        showAddressList()

        my_work_addresses_activity_add_address_btn.setOnClickListener {
            val addAddressIntent = Intent(this, AddWorkAddress::class.java)
            startActivityForResult(addAddressIntent, Constants.START_WORK_ADDRESS_ACTIVITY_REQUEST_CODE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("TAG", "onActivityResult: >>>>>>> 1")
        if (requestCode == Constants.START_WORK_ADDRESS_ACTIVITY_REQUEST_CODE) {
            Log.i("TAG", "onActivityResult: >>>>>>> 2")
            if (resultCode == Activity.RESULT_OK) {
                Log.i("TAG", "onActivityResult: >>>>>>> 3")
                val address = data!!.getStringExtra(Constants.TRANS_ADDRESS)

                myWorkAddresses.add(address!!.toString())

                showAddressList()
                adapter.notifyDataSetChanged()
                // Toast.makeText(this, address, Toast.LENGTH_SHORT).show()

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
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



    private fun confirmDeleteDialog(position: Int, list: MutableList<String>){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(getString(R.string.deleteDialogTitle))
        //set message for alert dialog
        builder.setMessage(getString(R.string.deleteDialogQuestion))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(getString(R.string.yes)){ _, _ ->
            list.removeAt(position)
            adapter.notifyItemRemoved(position)
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
}