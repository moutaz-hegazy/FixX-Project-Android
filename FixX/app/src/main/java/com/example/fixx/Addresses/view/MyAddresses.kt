package com.example.fixx.Addresses.view

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.R
import com.example.fixx.Support.FirestoreService
import com.example.fixx.constants.Constants
import kotlinx.android.synthetic.main.activity_my_adresses.*
import kotlinx.android.synthetic.main.fragment_login_tab.*


class MyAddresses : AppCompatActivity(),RecycleAdapter.OnItemClickListener {

    var myAddresses = mutableListOf<String>()
    private lateinit var adapter : RecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_adresses)

        supportActionBar?.apply {
            title = getString(R.string.myAddressesTitle)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF6200EE")))
        }

        USER_OBJECT?.locations?.let{
            myAddresses.addAll(it)
        }

        adapter = RecycleAdapter(myAddresses,this)

        showAddressList()

        my_addresses_activity_add_address_btn.setOnClickListener {
            val addAddressIntent = Intent(this, AddAddressActivity::class.java)
            startActivityForResult(addAddressIntent,Constants.START_ADDRESS_ACTIVITY_REQUEST_CODE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("TAG", "onActivityResult: >>>>>>> 1")
        if (requestCode == Constants.START_ADDRESS_ACTIVITY_REQUEST_CODE) {
            Log.i("TAG", "onActivityResult: >>>>>>> 2")
            if (resultCode == Activity.RESULT_OK) {
                Log.i("TAG", "onActivityResult: >>>>>>> 3")
                val address = data!!.getStringExtra(Constants.TRANS_ADDRESS)

                myAddresses.add(address!!.toString())

               showAddressList()
                adapter.notifyDataSetChanged()
               // Toast.makeText(this, address, Toast.LENGTH_SHORT).show()

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, getString(R.string.longPressToast), Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(position: Int) {
       confirmDeleteDialog(position,myAddresses)
    }

    private fun showAddressList(){
        if (myAddresses.isNotEmpty()){
            my_addresses_activity_no_address_txt.visibility = View.INVISIBLE

            my_addresses_recycler_view.adapter = adapter
            my_addresses_recycler_view.layoutManager = LinearLayoutManager(this)
            my_addresses_recycler_view.setHasFixedSize(true)

        }
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
            Log.i("TAG", "confirmDeleteDialog: try to remove <<<<<<<<<<<")
            val address = list[position]
            FirestoreService.removeLocation(address)
            USER_OBJECT?.locations?.removeAt(position)
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

         val positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
         with(positiveButton) {
             setTextColor(ContextCompat.getColor(context, R.color.red))
         }
         val negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
         with(negativeButton) {
             setTextColor(ContextCompat.getColor(context, R.color.green))
         }


     }

}


