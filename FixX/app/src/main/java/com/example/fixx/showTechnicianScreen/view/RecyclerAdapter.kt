package com.example.fixx.showTechnicianScreen.view

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Technician
import com.example.fixx.databinding.TechnicianItemBinding


class RecyclerAdapter(val arrayList: MutableList<Technician>, val context: Context) : RecyclerView.Adapter<RecyclerAdapter.TechViewHolder>() {
    lateinit var bookTechnician : ()->Unit
    lateinit var showTechProfileHandler : (Int)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechViewHolder {
        var root = TechnicianItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TechViewHolder(root)
    }

    override fun getItemCount(): Int {
        if (arrayList.size==0){
            Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show()
        }
        return arrayList.size
    }

    override fun onBindViewHolder(holder: TechViewHolder, position: Int) {
        var name :TextView = holder.binding.technicianItemNameLbl
        name.text = arrayList[position].name
        var image : ImageView = holder.binding.technicianItemImg
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image.clipToOutline = true
        }
        holder.itemView.setOnClickListener{
            Toast.makeText(context,"clicked ${name.text}",Toast.LENGTH_SHORT).show()
            showTechProfileHandler(position)
        }

        holder.binding.technicianItemBookBtn.setOnClickListener{
            bookTechnician()
        }
    }

    inner class TechViewHolder (var binding : TechnicianItemBinding) : RecyclerView.ViewHolder(binding.root)
}