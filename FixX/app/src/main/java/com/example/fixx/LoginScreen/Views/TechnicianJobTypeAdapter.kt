package com.example.fixx.LoginScreen.Views
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fixx.R
import kotlinx.android.synthetic.main.job_row.view.*

class TechnicianJobTypeAdapter(private val jobList: MutableList<String>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<TechnicianJobTypeAdapter.JobViewHolder>() {

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val job = itemView.job_row_job_lbl

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }


    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.job_row, parent, false)
        return JobViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val currentItem = jobList[position]
        holder.job.text = currentItem


    }

    override fun getItemCount() = jobList.size
}
