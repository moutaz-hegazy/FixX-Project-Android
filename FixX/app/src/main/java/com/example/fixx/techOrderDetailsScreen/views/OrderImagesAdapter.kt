package com.example.fixx.techOrderDetailsScreen.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.provider.SyncStateContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.CustomizeOrderImageRecyclerItemBinding
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.lang.Exception

class OrderImagesAdapter(var data : MutableList<String>) : RecyclerView.Adapter<OrderImagesAdapter.VH>() {
    lateinit var context: Context
    private var images = ArrayList<Bitmap?>()
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
                val imagePath = images[position]?.let {getImageUriFromBitmap(context, it) }
                Intent().apply {
                    action = android.content.Intent.ACTION_VIEW
                    setDataAndType(imagePath, "image/*")
                }.also {
                    context.startActivity(it)
                }
            }
        }
        holder.binding.orderImageOptionsBtn.apply {
            visibility = View.GONE
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bitMap = Picasso.get().load(data[position]).get()
                CoroutineScope(Dispatchers.Main).launch {
                    images.add(bitMap)
                    holder.binding.cutomizeOrderRecyclerImageView.setImageBitmap(bitMap)
                }
            }catch (error : IllegalArgumentException){
                Log.i("TAG", "onBindViewHolder: "+ error.localizedMessage)
            }
        }
    }

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    override fun getItemCount() = data.size
}