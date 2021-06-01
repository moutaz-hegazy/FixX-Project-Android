package com.example.project

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.fixx.POJOs.ServiceItem
import com.example.fixx.R

class ServiceAdapter(var context : Context , var arrayList: ArrayList<ServiceItem>) : BaseAdapter(){

    override fun getItem(position: Int): Any {
        return arrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view: View = View.inflate(context, R.layout.grid_item_list,null)
        var icons:ImageView = view.findViewById(R.id.homefragment_serviceicons_image_view)
        var names : TextView = view.findViewById(R.id.homefragment_servicename_text_view)
        var serviceItem : ServiceItem = arrayList.get(position)
        icons.setImageResource(serviceItem.icons!!)
        names.text = context.getString(serviceItem.name?:0)
        return view
    }

}