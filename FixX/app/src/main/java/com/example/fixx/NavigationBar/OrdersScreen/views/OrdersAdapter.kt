package com.example.fixx.NavigationBar.OrdersScreen.views

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.fixx.JobDetailsDisplay.views.JobDetailsDisplayActivity
import com.example.fixx.POJOs.Job
import com.example.fixx.R
import com.example.fixx.addExtensionScreen.views.AddJobExtensionActivity
import com.example.fixx.constants.Constants
import com.example.fixx.databinding.CompletedOrdersRecyclerRowBinding
import com.example.fixx.databinding.OngoingOrderRecyclerRowBinding
import com.example.fixx.takeOrderScreen.views.CustomizeOrderActivity

class OrdersAdapter(val data: ArrayList<Job>, val type : Job.JobStatus) : RecyclerView.Adapter<OrdersAdapter.VH>() {
    lateinit var context: Context
    lateinit var deleteHandler : (Int)->Unit
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
                    val locName = data[position].location?.substringBefore("%")
                    if(!locName.isNullOrEmpty()){
                        view?.ongoingOrderAddressLbl?.text = locName
                    }else{
                        view?.ongoingOrderAddressLbl?.text = data[position].location?.substringAfter("%")
                    }
                    view?.ongoingOrderLayout?.setOnClickListener {
                        Intent(context, JobDetailsDisplayActivity::class.java).apply {
                            putExtra(Constants.TRANS_JOB_OBJECT, this@OrdersAdapter.data[position])
                        }.also {
                            context.startActivity(it)
                        }
                    }
                    view?.ongoingOrderMenuBtn?.setOnClickListener {
                        showPopupMenu(view?.ongoingOrderMenuBtn, position, Job.JobStatus.OnRequest)
                    }
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
                    val locName = data[position].location?.substringBefore("%")
                    if(!locName.isNullOrEmpty()){
                        view?.ongoingOrderAddressLbl?.text = locName
                    }else{
                        view?.ongoingOrderAddressLbl?.text = data[position].location?.substringAfter("%")
                    }
                    view.ongoingOrderLayout.setOnClickListener {
                        Intent(context, JobDetailsDisplayActivity::class.java).apply {
                            putExtra(Constants.TRANS_JOB_OBJECT, this@OrdersAdapter.data[position])
                        }.also {
                            context.startActivity(it)
                        }
                    }
                    view?.ongoingOrderMenuBtn?.setOnClickListener {
                        showPopupMenu(view?.ongoingOrderMenuBtn,position,Job.JobStatus.Accepted)
                    }
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
                    val locName = data[position].location?.substringBefore("%")
                    if(!locName.isNullOrEmpty()){
                        view?.completedOrderAddressLbl?.text = locName
                    }else{
                        view?.completedOrderAddressLbl?.text = data[position].location?.substringAfter("%")
                    }
                    view.completedOrderLayout.setOnClickListener {
                        Intent(context, JobDetailsDisplayActivity::class.java).apply {
                            putExtra(Constants.TRANS_JOB_OBJECT, this@OrdersAdapter.data[position])
                        }.also {
                            context.startActivity(it)
                        }
                    }
                    view?.completedOrderMenuBtn?.setOnClickListener {
                        showPopupMenu(view?.completedOrderMenuBtn,position,Job.JobStatus.Completed)
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

    private fun showPopupMenu(view : View , position : Int,type: Job.JobStatus){
        val popupMenu = PopupMenu(context,view)
        popupMenu.apply {
            menuInflater.inflate(
                            when(type){
                                Job.JobStatus.Completed ->R.menu.image_long_press_menu
                                Job.JobStatus.OnRequest ->R.menu.on_request_menu
                                Job.JobStatus.Accepted  ->R.menu.accepted_job_menu
                          }
                , menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.image_menu_delete,
                    R.id.accepted_job_menu_delete,
                    R.id.onRequest_menu_delete  -> {
                        confirmDeleteDialog(position)
                        true
                    }

                    R.id.onRequest_menu_edit ->{
                        Intent(context, CustomizeOrderActivity::class.java).apply{
                            putExtra(Constants.TRANS_JOB_OBJECT, this@OrdersAdapter.data[position])
                        }.also {
                            context?.startActivity(it)
                        }
                        true
                    }

                    R.id.accepted_job_menu_extend ->{
                        Intent(context, AddJobExtensionActivity::class.java).apply{
                            putExtra(Constants.TRANS_JOB, this@OrdersAdapter.data[position].jobId)
                        }.also {
                            context.startActivity(it)
                        }
                        true
                    }

                    else -> false
                }
            }
            show()
        }
    }

    private fun confirmDeleteDialog(position: Int){
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle(context.getString(R.string.deleteJobRequest))
        //set message for alert dialog
        builder.setMessage(context.getString(R.string.deleteJobRequestQuestion))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(context.getString(R.string.yes)){ _, _ ->
            Log.i("TAG", "confirmDeleteDialog: try to remove <<<<<<<<<<<")
            deleteHandler(position)
        }
        //performing negative action
        builder.setNegativeButton(context.getString(R.string.no)){ _, _ ->

        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
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
    }
    override fun getItemCount() = data.size
}