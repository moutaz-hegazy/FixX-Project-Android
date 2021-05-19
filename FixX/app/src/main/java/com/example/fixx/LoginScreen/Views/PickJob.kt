package com.example.fixx.LoginScreen.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixx.R
import kotlinx.android.synthetic.main.fragment_pick_job.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PickJob.newInstance] factory method to
 * create an instance of this fragment.
 */
class PickJob : Fragment(),TechnicianJobTypeAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var jobs = mutableListOf<String>()

    private val adapter = TechnicianJobTypeAdapter(jobs,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

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

    override fun onItemClick(position: Int) {
        fragmentManager?.beginTransaction()?.replace(R.id.signup_onboarding_fragment_id, SignUpTabFragment())?.commit()

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
        showJobList()
    }

    fun showJobList(){
        technician_job_type_activity_recycler_view.adapter = adapter
        technician_job_type_activity_recycler_view.layoutManager = LinearLayoutManager(context)
        technician_job_type_activity_recycler_view.setHasFixedSize(true)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PickJob.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PickJob().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}