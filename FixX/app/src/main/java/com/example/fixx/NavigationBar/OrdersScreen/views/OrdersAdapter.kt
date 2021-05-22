package com.example.fixx.NavigationBar.OrdersScreen.views

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.fixx.POJOs.Job
import com.example.fixx.R
import com.example.fixx.databinding.CompletedOrdersRecyclerRowBinding
import com.example.fixx.databinding.OnGuaranteeOrderRecyclerRowBinding
import com.example.fixx.databinding.OngoingOrderRecyclerRowBinding

class OrdersAdapter(val data: ArrayList<Job>, val type : Job.JobStatus) : RecyclerView.Adapter<OrdersAdapter.VH>() {
    lateinit var context: Context
    class VH(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context
        return when(type){
            Job.JobStatus.OnRequest -> VH(OngoingOrderRecyclerRowBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
            Job.JobStatus.Accepted -> VH(OngoingOrderRecyclerRowBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
            Job.JobStatus.OnGuarantee -> VH(OnGuaranteeOrderRecyclerRowBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
            Job.JobStatus.Completed -> VH(CompletedOrdersRecyclerRowBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
        }

    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        when(data[position].status) {
            Job.JobStatus.OnRequest -> {
                val mRoot = holder.binding as? OngoingOrderRecyclerRowBinding
                mRoot?.let { view ->
                    view.ongoingOrderDateLbl.text = data[position].date
                    view.ongoingOrderFromTimeLbl.text = context.getString(R.string.from) +
                            (data[position].fromTime ?: "--:--")
                    view.ongoingOrderToTimeLbl.text = context.getString(R.string.to) +
                            (data[position].toTime ?: "--:--")
                    view.ongoingOrderPriceLbl.text =
                        context.getString(R.string.price) + (data[position].price?.toString()
                            ?: context.getString(R.string.notDetermined))
                    view.ongoingOrderStatusLbl.apply {
                        text = context.getString(R.string.onRequest)
                        setTextColor(Color.GREEN)
                    }
                    view.ongoingOrdersJobTypeLbl.text = data[position].type
                    getImageResourse(data[position].type)?.let {
                        view.ongoingOrderJobImage.setImageResource(it)
                    }
                    view.ongoingOrderAddressLbl.text = data[position].location
                }
            }

            Job.JobStatus.Accepted -> {
                val mRoot = holder.binding as? OngoingOrderRecyclerRowBinding
                mRoot?.let { view ->
                    view.ongoingOrderDateLbl.text = data[position].date
                    view.ongoingOrderFromTimeLbl.text = context.getString(R.string.from) +
                            (data[position].fromTime ?: "--:--")
                    view.ongoingOrderToTimeLbl.text = context.getString(R.string.to) +
                            (data[position].toTime ?: "--:--")
                    view.ongoingOrderPriceLbl.text = """${context.getString(R.string.price)} ${
                        (data[position].price?.toString()
                            ?: context.getString(R.string.notDetermined))
                    } LE"""
                    view.ongoingOrderStatusLbl.apply {
                        text = context.getString(R.string.accepted)
                        setTextColor(Color.BLUE)
                    }
                    view.ongoingOrdersJobTypeLbl.text = data[position].type
                    getImageResourse(data[position].type)?.let {
                        view.ongoingOrderJobImage.setImageResource(it)
                    }
                    view.ongoingOrderAddressLbl.text = data[position].location
                }
            }

            Job.JobStatus.OnGuarantee -> {
                val mRoot = holder.binding as? OnGuaranteeOrderRecyclerRowBinding
                mRoot?.let{
                    view ->
                    view.onGuaranteeOrderCompletionDateLbl.text = data[position].completionDate
                    view.onGuaranteeOrdersJobTypeLbl.text = data[position].type
                    getImageResourse(data[position].type)?.let {
                        view.onGuaranteeOrderJobImage.setImageResource(it)
                    }
                }
            }
        }
    }

    private fun getImageResourse(type : String) : Int?{
        return when(type){
            "Painter" -> R.drawable.painter
            "Plumber" -> R.drawable.plumber
            "Electrician" -> R.drawable.electrician
            "Carpenter" -> R.drawable.carpenter
            "Tiles_Handyman" -> R.drawable.tileshandyman
            "Parquet" -> R.drawable.parquet
            "Smith" -> R.drawable.smith
            "Decoration_Stones" -> R.drawable.masondecorationstones
            "Alumetal" -> R.drawable.alumetal
            "Air_Conditioner" -> R.drawable.airconditioner
            "Curtains" -> R.drawable.curtains
            "Glass" -> R.drawable.glass
            "Satellite" -> R.drawable.satellite
            "Gypsum_Works" -> R.drawable.gypsumworks
            "Marble" -> R.drawable.marbleandgranite
            "Pest_Control" -> R.drawable.pestcontrol
            "Wood_Painter" -> R.drawable.woodpainter
            "Swimming_pool" -> R.drawable.swimmingpool
            else -> null
        }
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

    override fun getItemCount() = data.size
}