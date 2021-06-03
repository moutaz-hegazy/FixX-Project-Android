package com.example.fixx.Addresses.view

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MySpinnerAdapter<T> (private val newContext: Context, private val resource: Int, private val objects: MutableList<T>)
    : ArrayAdapter<T>(newContext, resource, objects){

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val tv : TextView = view as TextView
        if(position == 0) {
            tv.setTextColor(Color.GRAY)
        }
        return view
    }
}