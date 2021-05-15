package com.example.fixx.takeOrderScreen.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.R
import com.example.fixx.databinding.CustomizeOrderImageRecyclerItemBinding

class ImagesAdapter(var data: MutableList<Bitmap>) : RecyclerView.Adapter<ImagesAdapter.VH>() {
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
                        Log.i("TAG", "showPopupMenu: Delete Pressed !!")
                        data.removeAt(position)
                        notifyDataSetChanged()
                        true
                    }
                    else -> false
                }
            }
            menuInflater.inflate(R.menu.image_long_press_menu, menu)
            show()
        }
    }

//    private fun getImageFromUri(dataStr: String): Bitmap {
//        val data: Uri = Uri.parse(dataStr)
//        return MediaStore.Images.Media.getBitmap(context.contentResolver, data)
//    }

    override fun getItemCount() = data.size
}