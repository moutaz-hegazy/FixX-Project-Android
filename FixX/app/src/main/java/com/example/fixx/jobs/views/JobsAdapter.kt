package com.example.fixx.jobs.views

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.fixx.JobDetailsDisplay.views.JobDetailsDisplayActivity
import com.example.fixx.POJOs.Job
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.CompletedOrdersRecyclerRowBinding
import com.example.fixx.databinding.OngoingOrderRecyclerRowBinding
import com.example.fixx.takeOrderScreen.views.CustomizeOrderActivity
import com.example.fixx.techOrderDetailsScreen.views.TechViewOrderScreen

class JobsAdapter(val data: ArrayList<Job>, val type : Job.JobStatus) : RecyclerView.Adapter<JobsAdapter.VH>() {
    lateinit var context: Context
    class VH(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context
        return when(type){
            Job.JobStatus.OnRequest -> VH(OngoingOrderRecyclerRowBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
            Job.JobStatus.Accepted -> VH(OngoingOrderRecyclerRowBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
            Job.JobStatus.Completed -> VH(CompletedOrdersRecyclerRowBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        when(data[position].status) {
            Job.JobStatus.OnRequest -> {
                val mRoot = holder.binding as? OngoingOrderRecyclerRowBinding
                mRoot.let { view ->
                    view?.ongoingOrderDateLbl?.text = data[position].date
                    view?.ongoingOrderFromTimeLbl?.text = context.getString(R.string.from) +
                            (data[position].fromTime ?: "--:--")
                    view?.ongoingOrderToTimeLbl?.text = context.getString(R.string.to) +
                            (data[position].toTime ?: "--:--")
                    view?.ongoingOrderPriceLbl?.text =
                        context.getString(R.string.price) + (data[position].price?.toString()
                            ?: context.getString(R.string.notDetermined))
                    view?.ongoingOrderStatusLbl?.apply {
                        text = context.getString(R.string.onRequest)
                        setTextColor(Color.GREEN)
                    }
                    view?.ongoingOrdersJobTypeLbl?.text = context.getString(getStringResourse(data[position].type)!!)
                    getImageResourse(data[position].type)?.let {
                        view?.ongoingOrderJobImage?.setImageResource(it)
                    }
                    view?.ongoingOrderAddressLbl?.text = data[position].location?.substringAfter("%")
                    view?.ongoingOrderLayout?.setOnClickListener {
                        Intent(context, TechViewOrderScreen::class.java).apply {
                            putExtra(Constants.TRANS_JOB_OBJECT, this@JobsAdapter.data[position])
                        }.also {
                            context.startActivity(it)
                        }
                    }
                    view?.ongoingOrderMenuBtn?.visibility = View.INVISIBLE
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
                    view.ongoingOrdersJobTypeLbl.text = context.getString(getStringResourse(data[position].type)!!)
                    getImageResourse(data[position].type)?.let {
                        view.ongoingOrderJobImage.setImageResource(it)
                    }
                    view.ongoingOrderAddressLbl.text = data[position].location?.substringAfter("%")
                    view.ongoingOrderLayout.setOnClickListener {
                        Intent(context, TechViewOrderScreen::class.java).apply {
                            putExtra(Constants.TRANS_JOB_OBJECT, this@JobsAdapter.data[position])
                        }.also {
                            context.startActivity(it)
                        }
                    }
                    view?.ongoingOrderMenuBtn?.visibility = View.INVISIBLE
                }
            }

            Job.JobStatus.Completed -> {
                val mRoot = holder.binding as? CompletedOrdersRecyclerRowBinding
                mRoot?.let { view ->
                    view.completedOrderDateLbl.text = data[position].completionDate
                    view.completedOrderPriceLbl.text = data[position].price.toString()
                    view.completedOrdersJobTypeLbl.text = context.getString(getStringResourse(data[position].type)!!)
                    getImageResourse(data[position].type)?.let {
                        view.completedOrderJobImage.setImageResource(it)
                    }
                    view.completedOrderAddressLbl.text = data[position].location?.substringAfter("%")
                    view.completedOrderLayout.setOnClickListener {
                        Intent(context, TechViewOrderScreen::class.java).apply {
                            putExtra(Constants.TRANS_JOB_OBJECT, this@JobsAdapter.data[position])
                        }.also {
                            context.startActivity(it)
                        }
                    }
                    view?.completedOrderMenuBtn?.visibility = View.INVISIBLE
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

    private fun getStringResourse(type : String) : Int?{
        return when(type){
            "Painter" -> R.string.Painter
            "Plumber" -> R.string.Plumber
            "Electrician" -> R.string.Electrician
            "Carpenter" -> R.string.Carpenter
            "Tiles_Handyman" -> R.string.Tiles_Handyman
            "Parquet" -> R.string.Parquet
            "Smith" -> R.string.Smith
            "Decoration_Stones" -> R.string.Decoration_Stones
            "Alumetal" -> R.string.Alumetal
            "Air_Conditioner" -> R.string.Air_Conditioner
            "Curtains" -> R.string.Curtains
            "Glass" -> R.string.Glass
            "Satellite" -> R.string.Satellite
            "Gypsum_Works" -> R.string.Gypsum_Works
            "Marble" -> R.string.Marble
            "Pest_Control" -> R.string.Pest_Control
            "Wood_Painter" -> R.string.Wood_Painter
            "Swimming_pool" -> R.string.Swimming_pool
            else -> null
        }
    }

    override fun getItemCount() = data.size
}