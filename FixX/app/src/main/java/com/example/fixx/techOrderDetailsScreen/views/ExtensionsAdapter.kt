package com.example.fixx.techOrderDetailsScreen.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.POJOs.Extension
import com.example.fixx.R
import com.example.fixx.databinding.ExtensionItemBinding

class ExtensionsAdapter (val extensions : MutableList<Extension>,
                         val updatePriceHandler : (ext : Extension, onSuccess:()->Unit, onFail: ()->Unit)->Unit,
                         val cancelExtensionHandler : (ext:Extension, onSuccess:()->Unit, onFail: ()->Unit)->Unit)
    : RecyclerView.Adapter<ExtensionsAdapter.VH>() {
    lateinit var context: Context
    class VH(var binding: ExtensionItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context
        val binding =
            ExtensionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        val ext = extensions[position]
        if(ext.description?.isNullOrEmpty() != true){
            holder.binding.extensionItemDescriptionLbl.apply {
                text = ext.description
                visibility = View.VISIBLE
            }
        }
        if(ext.images.isNullOrEmpty() != true){
            holder.binding.extensionItemImagesRecycler.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(context).apply {
                    orientation = RecyclerView.HORIZONTAL
                }
                adapter = OrderImagesAdapter(ext.images!!.map { it.second }.toMutableList())
            }
        }

        if(ext.price != null){
            holder.binding.extensionItemPriceTitleLbl.text = context.getString(R.string.price)
            holder.binding.extensionItemPriceLbl.apply {
                text = ext.price.toString()
                visibility = View.VISIBLE
            }
        }else{
            holder.binding.extensionItemPriceTitleLbl.text = context.getString(R.string.Determine_Price)
            holder.binding.extensionItemPriceTxt.visibility = View.VISIBLE
            holder.binding.extensionItemAcceptBtn.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    val enteredPrice = holder.binding.extensionItemPriceTxt.text.toString()
                    if(enteredPrice.isNullOrEmpty()){
                        Toast.makeText(context,context.getString(R.string.EnterPrice),Toast.LENGTH_SHORT).show()
                    }else{
                        holder.binding.extensionItemAcceptBtn.visibility = View.INVISIBLE
                        holder.binding.extensionItemCancelBtn.visibility = View.INVISIBLE
                        ext.price = enteredPrice.toInt()
                        updatePriceHandler(ext,{
                            holder.binding.extensionItemPriceTxt.visibility = View.INVISIBLE
                            holder.binding.extensionItemPriceTitleLbl.text = context.getString(R.string.price)
                            holder.binding.extensionItemPriceLbl.apply {
                                text = enteredPrice
                                visibility = View.VISIBLE
                            }
                        },{
                            Toast.makeText(context,context.getString(R.string.ExtensionUpdataFail),Toast.LENGTH_SHORT).show()
                            holder.binding.extensionItemAcceptBtn.visibility = View.VISIBLE
                            holder.binding.extensionItemCancelBtn.visibility = View.VISIBLE
                        })
                    }
                }
            }
            holder.binding.extensionItemCancelBtn.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    cancelExtensionHandler(ext,{
                        extensions.removeAt(position)
                        notifyDataSetChanged()
                    },{
                        Toast.makeText(context,context.getString(R.string.ExtensionRemoveFail),Toast.LENGTH_SHORT).show()
                    })
                }
            }
        }
    }

    override fun getItemCount() = extensions.size
}