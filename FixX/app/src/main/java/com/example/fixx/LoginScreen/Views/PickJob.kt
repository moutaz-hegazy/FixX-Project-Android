package com.example.fixx.LoginScreen.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.example.fixx.Addresses.view.MySpinnerAdapter
import com.example.fixx.R
import com.example.fixx.constants.Constants
import kotlinx.android.synthetic.main.fragment_pick_job.*
import kotlinx.android.synthetic.main.fragment_sign_up_on_boarding.*

class PickJob : Fragment() {

    var jobs = mutableListOf<String>()
    var technicianJob = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        jobs.add(getString(R.string.Job))
        jobs.add(getString(R.string.Plumber))
        jobs.add(getString(R.string.Painter))
        jobs.add(getString(R.string.Electrician))
        jobs.add(getString(R.string.Carpenter))
        jobs.add(getString(R.string.Tiles_Handyman))
        jobs.add(getString(R.string.Parquet))
        jobs.add(getString(R.string.Smith))
        jobs.add(getString(R.string.Decoration_Stones))
        jobs.add(getString(R.string.Alumetal))
        jobs.add(getString(R.string.Air_Conditioner))
        jobs.add(getString(R.string.Curtains))
        jobs.add(getString(R.string.Glass))
        jobs.add(getString(R.string.Satellite))
        jobs.add(getString(R.string.Gypsum_Works))
        jobs.add(getString(R.string.Marble))
        jobs.add(getString(R.string.Pest_Control))
        jobs.add(getString(R.string.Wood_Painter))
        jobs.add(getString(R.string.Swimming_pool))
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
            if(!technicianJob.isNullOrEmpty()){
                arguments?.putString(Constants.TRANS_JOB, technicianJob)
                TechnicianAddressFragment().apply {
                    this.arguments = this@PickJob.arguments
                }.also {
                    fragmentManager?.beginTransaction()?.replace(R.id.pick_job_fragment, it)?.commit()
                }
            }else{
                Toast.makeText(context,R.string.select_job,Toast.LENGTH_SHORT).show()
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
                technicianJob = getJobEnglishName( pick_job_fragment_job_spinner.selectedItem.toString())
            }

        }
    }

    private fun getJobEnglishName(job: String): String {
        var myJob = job
        for (iterator in Constants.jobInArabic.indices) {
            if (job.equals(Constants.jobInArabic[iterator])) {
                myJob = Constants.jobs[iterator]
            }
        }
        return myJob
    }

}