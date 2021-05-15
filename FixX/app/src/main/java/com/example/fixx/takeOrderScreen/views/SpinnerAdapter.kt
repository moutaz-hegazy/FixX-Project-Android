package com.example.fixx.takeOrderScreen.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class SpinnerAdapter<T>(private val newContext: Context, private val resource: Int, private val objects: MutableList<T>)
    : ArrayAdapter<T>(newContext, resource, objects) {
    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }


    fun addValue(value : T){
        objects.add(objects.size -1,value)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val tv : TextView = view as TextView
        if(position == 0) {
            tv.setTextColor(Color.GRAY)
            tv.background = getBorders(Color.WHITE,Color.GRAY,0,0,0,5)
        }else if (position == objects.size -1){
            tv.setTextColor(Color.BLUE)
            tv.background = getBorders(Color.WHITE,Color.GRAY,0,5,0,0)
        }else{
            tv.setTextColor(Color.BLACK)
        }
        return view
    }

    private  fun getBorders(bgColor: Int, borderColor: Int,
                             left: Int, top: Int, right: Int, bottom: Int): LayerDrawable {
        // Initialize new color drawables
        val borderColorDrawable = ColorDrawable(borderColor)
        val backgroundColorDrawable = ColorDrawable(bgColor)

        // Initialize a new array of drawable objects
        val drawables = arrayOf<Drawable>(
                borderColorDrawable,
                backgroundColorDrawable
        )

        // Initialize a new layer drawable instance from drawables array
        val layerDrawable = LayerDrawable(drawables)

        // Set padding for background color layer
        layerDrawable.setLayerInset(
                1,  // Index of the drawable to adjust [background color layer]
                left,  // Number of pixels to add to the left bound [left border]
                top,  // Number of pixels to add to the top bound [top border]
                right,  // Number of pixels to add to the right bound [right border]
                bottom // Number of pixels to add to the bottom bound [bottom border]
        )
        // Finally, return the one or more sided bordered background drawable
        return layerDrawable
    }
}