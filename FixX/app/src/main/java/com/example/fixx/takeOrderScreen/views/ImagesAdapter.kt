package com.example.fixx.takeOrderScreen.views

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.R
import com.example.fixx.databinding.CustomizeOrderImageRecyclerItemBinding

class ImagesAdapter(var data: ArrayList<Bitmap>,
                    val deleteImageHandler: (position:Int)->Unit) : RecyclerView.Adapter<ImagesAdapter.VH>() {
    lateinit var scrollToPositionHandler : (position : Int)->Unit
    lateinit var imagePickerHandler : ()->Unit
    lateinit var context: Context
    class VH(var binding: CustomizeOrderImageRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context
        val binding =
            CustomizeOrderImageRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.customizeOrderLayout.apply {
            setOnClickListener {
                if (position == 0) {
                    // add image
                    Log.i("TAG", "onBindViewHolder: ADD IMAGE !")
                    imagePickerHandler()
                }
            }
            setOnLongClickListener {
                view ->
                if(position != 0){
                    Log.i("TAG", "onBindViewHolder: SHOULD DELETE")
                    scrollToPositionHandler(position)
                    showPopupMenu(holder.binding.orderImageOptionsBtn,position)
                    true
                }else{
                    false
                }
            }
        }
        holder.binding.orderImageOptionsBtn.apply {
            if(position == 0){
                visibility = View.GONE
            }else{
                setOnClickListener{
                    showPopupMenu(holder.binding.orderImageOptionsBtn,position)
                }
            }
        }
        holder.binding.cutomizeOrderRecyclerImageView.setImageBitmap(data[position])
    }

    private fun showPopupMenu(view : View , position : Int){
        val popupMenu = PopupMenu(context,view)
        popupMenu.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.image_menu_delete -> {

                        val builder = AlertDialog.Builder(context)
                        builder.setTitle(R.string.deleteImageDialogTitle)
                        builder.setMessage(R.string.deleteImageDialogMsg)
                        builder.setIcon(android.R.drawable.ic_dialog_alert)

                        builder.setPositiveButton(R.string.yes){dialogInterface, which ->
                            Log.i("TAG", "showPopupMenu: Delete Pressed !!")
                            deleteImageHandler(position)

                        }

                        builder.setNegativeButton(R.string.no){dialogInterface, which ->

                        }

                        val alertDialog: AlertDialog = builder.create()
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


                        true


                    }
                    else -> false
                }
            }
            menuInflater.inflate(R.menu.image_long_press_menu, menu)
            show()
        }
    }

    override fun getItemCount() = data.size
}