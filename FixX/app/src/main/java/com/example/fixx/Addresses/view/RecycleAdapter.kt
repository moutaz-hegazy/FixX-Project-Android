package com.example.fixx.Addresses.view

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.Addresses.MyAdresses
import com.example.fixx.R
import kotlinx.android.synthetic.main.address_row.view.*

class RecycleAdapter(private val addressList: MutableList<String>, private val listener: MyAdresses) :
    RecyclerView.Adapter<RecycleAdapter.AddressViewHolder>() {

    lateinit var context: Context

    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener,
        View.OnLongClickListener {
        val address = itemView.address_row_address_lbl
        val optionMenu = itemView.address_row_options_menu

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener (this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemLongClick(position)
            }
            return true
        }


    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onItemLongClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.address_row, parent, false)
        context = parent.context
        return AddressViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val currentItem = addressList[position]
        holder.address.text = currentItem
        holder.optionMenu.apply {

                setOnClickListener{
                    showPopupMenu(holder.optionMenu,position)

            }
        }


    }

    override fun getItemCount() = addressList.size

    private fun showPopupMenu(view : View , position : Int){
        val popupMenu = PopupMenu(context,view)
        popupMenu.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.address_options_menu_delete -> {
                        confirmDeleteDialog(position,addressList)
                        true
                    }
                    else -> false
                }
            }
            menuInflater.inflate(R.menu.address_options_menu, menu)
            show()
        }
    }

    fun confirmDeleteDialog(position: Int, list: MutableList<String>){
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Confirm Deleting Address")
        //set message for alert dialog
        builder.setMessage("Do you want to delete this address?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)


        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            list.removeAt(position)
            notifyDataSetChanged()
        }
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->

        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
        alertDialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.btn_border);

    }

}