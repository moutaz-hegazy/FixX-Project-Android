package com.example.fixx.LoginScreen.Views
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.R
import kotlinx.android.synthetic.main.technician_address_row.view.*

class TechnicianAddressAdapter(private val addressList: MutableList<String>) :
    RecyclerView.Adapter<TechnicianAddressAdapter.JobViewHolder>() {

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val address = itemView.technician_address_row_address_lbl!!
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.technician_address_row, parent, false)
        return JobViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val currentItem = addressList[position]
        holder.address.text = currentItem


    }

    override fun getItemCount() = addressList.size
}
