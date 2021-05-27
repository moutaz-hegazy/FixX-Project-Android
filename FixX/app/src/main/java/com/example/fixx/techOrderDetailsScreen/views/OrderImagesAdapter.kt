package com.example.fixx.techOrderDetailsScreen.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.databinding.CustomizeOrderImageRecyclerItemBinding
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.ByteArrayOutputStream
import java.lang.Exception

class OrderImagesAdapter(var data : List<String>) : RecyclerView.Adapter<OrderImagesAdapter.VH>() {
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
                    setAction(android.content.Intent.ACTION_VIEW)
                    setDataAndType(imagePath, "image/*")
                }.also {
                    context.startActivity(it)
                }
            }
        }
        holder.binding.orderImageOptionsBtn.apply {
            visibility = View.GONE
        }
        Picasso.get().load(data[position]).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                images.add(bitmap)
                holder.binding.cutomizeOrderRecyclerImageView.setImageBitmap(bitmap)
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                Log.i("TAG", "onBitmapFailed: ")
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                Log.i("TAG", "onPrepareLoad: ")
            }
        })
    }

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    override fun getItemCount() = data.size
}