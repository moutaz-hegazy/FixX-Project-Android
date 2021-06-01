package com.example.fixx.jobs

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.fixx.NavigationBar.OrdersScreen.views.OrdersAdapter
import com.example.fixx.POJOs.Job
import com.example.fixx.R
import com.example.fixx.databinding.*

class JobsAdapter (val data: ArrayList<Job>, val type : Job.JobStatus) : RecyclerView.Adapter<JobsAdapter.VH>() {

    lateinit var context: Context
    class VH(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context
        return when(type){
            Job.JobStatus.OnRequest -> VH(
                AvailableJobRecyclerRowBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
            Job.JobStatus.Accepted -> VH(
                OngoingJobRecyclerRowBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))

            Job.JobStatus.Completed -> VH(
                CompletedJobRecyclerRowBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
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

    private fun showPopupMenu(view : View, position : Int){
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
    override fun onBindViewHolder(holder: VH, position: Int) {
        when(data[position].status) {
            Job.JobStatus.OnRequest -> {
                val mRoot = holder.binding as? AvailableJobRecyclerRowBinding
                mRoot?.let { view ->
                    view.availableJobDateLbl.text = data[position].date
                    view.availableJobFromTimeLbl.text = context.getString(R.string.from) +
                            (data[position].fromTime ?: "--:--")
                    view.availableJobToTimeLbl.text = context.getString(R.string.to) +
                            (data[position].toTime ?: "--:--")
                    view.availableJobPriceLbl.text =
                        context.getString(R.string.price) + (data[position].price?.toString()
                            ?: context.getString(R.string.notDetermined))
                    view.availableJobStatusLbl.apply {
                        text = context.getString(R.string.onRequest)
                        setTextColor(Color.GREEN)
                    }
                    view.availableJobsJobTypeLbl.text = data[position].type
                    getImageResourse(data[position].type)?.let {
                        view.availableJobImage.setImageResource(it)
                    }
                    view.availableJobAddressLbl.text = data[position].location
                }
            }

            Job.JobStatus.Accepted -> {
                val mRoot = holder.binding as? OngoingJobRecyclerRowBinding
                mRoot?.let { view ->
                    view.ongoingJobDateLbl.text = data[position].date
                    view.ongoingJobFromTimeLbl.text = context.getString(R.string.from) +
                            (data[position].fromTime ?: "--:--")
                    view.ongoingJobToTimeLbl.text = context.getString(R.string.to) +
                            (data[position].toTime ?: "--:--")
                    view.ongoingJobPriceLbl.text = """${context.getString(R.string.price)} ${
                    (data[position].price?.toString()
                        ?: context.getString(R.string.notDetermined))
                    } LE"""
                    view.ongoingJobStatusLbl.apply {
                        text = context.getString(R.string.accepted)
                        setTextColor(Color.BLUE)
                    }
                    view.ongoingJobsJobTypeLbl.text = data[position].type
                    getImageResourse(data[position].type)?.let {
                        view.ongoingJobImage.setImageResource(it)
                    }
                    view.ongoingJobAddressLbl.text = data[position].location
                }
            }

            Job.JobStatus.Completed -> {
                val mRoot = holder.binding as? CompletedJobRecyclerRowBinding
                mRoot?.let { view ->
                    view.completedJobDateLbl.text = data[position].date
                    view.completedJobFromTimeLbl.text = context.getString(R.string.from) +
                            (data[position].fromTime ?: "--:--")
                    view.completedJobToTimeLbl.text = context.getString(R.string.to) +
                            (data[position].toTime ?: "--:--")
                    view.completedJobPriceLbl.text = """${context.getString(R.string.price)} ${
                    (data[position].price?.toString()
                        ?: context.getString(R.string.notDetermined))
                    } LE"""
                    view.completedJobStatusLbl.apply {
                        text = context.getString(R.string.completed)
                        setTextColor(Color.BLUE)
                    }
                    view.completedJobsJobTypeLbl.text = data[position].type
                    getImageResourse(data[position].type)?.let {
                        view.completedJobImage.setImageResource(it)
                    }
                    view.completedJobAddressLbl.text = data[position].location
                }
            }



        }
    }


}