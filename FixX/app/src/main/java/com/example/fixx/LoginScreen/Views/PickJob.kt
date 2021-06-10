package com.example.fixx.LoginScreen.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.fixx.Addresses.view.MySpinnerAdapter
import com.example.fixx.R
import com.example.fixx.constants.Constants
import kotlinx.android.synthetic.main.fragment_pick_job.*

class PickJob : Fragment() {

    var jobs = mutableListOf<String>()
    var technicianJob = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        jobs.add(getString(R.string.Job))
        jobs.add(getJobType(R.string.Plumber))
        jobs.add(getJobType(R.string.Painter))
        jobs.add(getJobType(R.string.Electrician))
        jobs.add(getJobType(R.string.Carpenter))
        jobs.add(getJobType(R.string.Tiles_Handyman))
        jobs.add(getJobType(R.string.Parquet))
        jobs.add(getJobType(R.string.Smith))
        jobs.add(getJobType(R.string.Decoration_Stones))
        jobs.add(getJobType(R.string.Alumetal))
        jobs.add(getJobType(R.string.Air_Conditioner))
        jobs.add(getJobType(R.string.Curtains))
        jobs.add(getJobType(R.string.Glass))
        jobs.add(getJobType(R.string.Satellite))
        jobs.add(getJobType(R.string.Gypsum_Works))
        jobs.add(getJobType(R.string.Marble))
        jobs.add(getJobType(R.string.Pest_Control))
        jobs.add(getJobType(R.string.Wood_Painter))
        jobs.add(getJobType(R.string.Swimming_pool))
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pick_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setJobSpinner()

        pick_job_fragment_next_btn.setOnClickListener {
            arguments?.putString(Constants.TRANS_JOB, technicianJob)
            TechnicianAddressFragment().apply {
                this.arguments = this@PickJob.arguments
            }.also {
                fragmentManager?.beginTransaction()?.replace(R.id.pick_job_fragment, it)?.commit()
            }
        }
    }

    fun setJobSpinner(){
        pick_job_fragment_job_spinner.adapter = MySpinnerAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_dropdown_item,
            jobs
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            pick_job_fragment_job_spinner.adapter = adapter
        }

        pick_job_fragment_job_spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                technicianJob = pick_job_fragment_job_spinner.selectedItem.toString()
            }

        }
    }


    private fun getJobType(id: Int) = when(id){
            R.string.Painter ->  "Painter"
            R.string.Plumber ->  "Plumber"
            R.string.Electrician -> "Electrician"
            R.string.Carpenter -> "Carpenter"
            R.string.Tiles_Handyman -> "Tiles_Handyman"
            R.string.Parquet -> "Parquet"
            R.string.Smith -> "Smith"
            R.string.Decoration_Stones -> "Decoration_Stones"
            R.string.Alumetal -> "Alumetal"
            R.string.Air_Conditioner -> "Air_Conditioner"
            R.string.Curtains -> "Curtains"
            R.string.Glass -> "Glass"
            R.string.Satellite -> "Satellite"
            R.string.Gypsum_Works -> "Gypsum_Works"
            R.string.Marble -> "Marble"
            R.string.Pest_Control -> "Pest_Control"
            R.string.Wood_Painter -> "Wood_Painter"
            R.string.Swimming_pool -> "Swimming_pool"
            else -> ""
    }
}