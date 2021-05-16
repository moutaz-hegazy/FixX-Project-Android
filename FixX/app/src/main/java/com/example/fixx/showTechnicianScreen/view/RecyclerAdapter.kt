package com.example.fixx.showTechnicianScreen.view

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Technician
import com.example.fixx.R
import com.example.fixx.showTechnicianScreen.viewModel.RecyclerActivityViewModel


class RecyclerAdapter(val viewModel: RecyclerActivityViewModel, val arrayList: MutableList<Technician>, val context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.TechViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechViewHolder {
        var root = LayoutInflater.from(parent.context).inflate(R.layout.technician_item, parent, false)
        return TechViewHolder(root)
    }

    override fun getItemCount(): Int {
        if (arrayList.size==0){
            Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show()
        }
        return arrayList.size
    }

    override fun onBindViewHolder(holder: TechViewHolder, position: Int) {
        holder.bind(arrayList[position].name)
    }

    inner class TechViewHolder (private val binding : View) : RecyclerView.ViewHolder(binding){

        fun bind(tech: String){
            var name :TextView = binding.findViewById(R.id.technician_item_name_lbl)
            name.text = tech
            var image : ImageView = binding.findViewById(R.id.technician_item_img)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setClipToOutline(true)
            }
            binding.setOnClickListener{

                Toast.makeText(context,"clicked ${name.text}",Toast.LENGTH_SHORT).show()
                //context.startActivity(Intent(context, TechnicianProfileActivity::class.java))
            }

        }
    }
}