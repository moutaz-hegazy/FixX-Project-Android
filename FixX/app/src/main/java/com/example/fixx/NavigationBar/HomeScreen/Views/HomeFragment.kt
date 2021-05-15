package com.example.project.bottom_navigation_fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.example.fixx.R

import com.example.project.ServiceAdapter
import com.example.project.ServiceItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var gridView: GridView? = null
    private var arrayList : ArrayList<ServiceItem> ? = null
    private var serviceAdapter : ServiceAdapter? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_home, container, false)
        gridView = rootView.findViewById(R.id.homefragment_services_grid_view)
        arrayList = ArrayList()
        arrayList = setDataList()
        serviceAdapter = context?.applicationContext?.let { ServiceAdapter(it, arrayList!!) }
        gridView?.adapter = serviceAdapter
        // Inflate the layout for this fragment

        return rootView
    }



    private fun setDataList() : ArrayList<ServiceItem>{
        var arrayList : ArrayList<ServiceItem> = ArrayList()

        arrayList.add(ServiceItem(R.drawable.painter,"Painter"))
        arrayList.add(ServiceItem(R.drawable.plumber,"Plumber"))
        arrayList.add(ServiceItem(R.drawable.electrician,"Electrician"))
        arrayList.add(ServiceItem(R.drawable.carpenter,"Carpenter"))
        arrayList.add(ServiceItem(R.drawable.tileshandyman,"Tiles Handyman"))
        arrayList.add(ServiceItem(R.drawable.parquet,"Parquet"))
        arrayList.add(ServiceItem(R.drawable.smith,"Smith"))
        arrayList.add(ServiceItem(R.drawable.masondecorationstones,"Decoration Stones"))
        arrayList.add(ServiceItem(R.drawable.alumetal,"Alumetal"))
        arrayList.add(ServiceItem(R.drawable.airconditioner,"Air Conditioner"))
        arrayList.add(ServiceItem(R.drawable.curtains,"Curtains"))
        arrayList.add(ServiceItem(R.drawable.glass,"Glass"))
        arrayList.add(ServiceItem(R.drawable.satellite,"Satellite"))
        arrayList.add(ServiceItem(R.drawable.gypsumworks,"Gypsum Works"))
        arrayList.add(ServiceItem(R.drawable.marbleandgranite,"Marble"))
        arrayList.add(ServiceItem(R.drawable.pestcontrol,"Pest Control"))
        arrayList.add(ServiceItem(R.drawable.woodpainter,"Wood Painter"))
        arrayList.add(ServiceItem(R.drawable.swimmingpool,"Swimming pool"))

        return arrayList
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}