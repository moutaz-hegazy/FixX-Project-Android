package com.example.project.bottom_navigation_fragments

//import com.example.fixx.jobs.jobsFragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.fixx.NavigationBar.NavigationBarActivity.Companion.USER_OBJECT
import com.example.fixx.POJOs.ServiceItem
import com.example.fixx.R
import com.example.fixx.constants.Constants
import com.example.fixx.jobs.views.JobsActivity
import com.example.fixx.takeOrderScreen.views.CustomizeOrderActivity
import com.example.project.ServiceAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment(){


    private var gridView: GridView? = null
    private var arrayList = ArrayList<ServiceItem>()
    private var serviceAdapter : ServiceAdapter? =null
    var floatingActionButton: FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_home, container, false)
        gridView = rootView.findViewById(R.id.homefragment_services_grid_view)
        arrayList = ArrayList()
        arrayList = setDataList()
        serviceAdapter = context?.applicationContext?.let {
            ServiceAdapter(it, arrayList)
        }
        gridView?.apply {
            adapter = serviceAdapter
            setOnItemClickListener { parent, view, position, id ->
                startCustomizeOrderActivity(position)
            }
        }

        if (USER_OBJECT?.accountType == "Technician") {
            floatingActionButton = rootView.findViewById(R.id.home_jobs_floatingactionbutton)
            floatingActionButton?.visibility = View.VISIBLE
            floatingActionButton?.setOnClickListener {
                val intent = Intent(activity, JobsActivity::class.java)
                activity?.startActivity(intent)
            }
        }
        return rootView
    }

    private fun startCustomizeOrderActivity(position: Int){
        val customizeOrder = Intent(context,CustomizeOrderActivity::class.java)
        customizeOrder.putExtra(Constants.serviceName, arrayList[position].name)
        startActivity(customizeOrder)
    }

    private fun setDataList() : ArrayList<ServiceItem>{
        var arrayList : ArrayList<ServiceItem> = ArrayList()

        arrayList.add(ServiceItem(R.drawable.painter,R.string.Painter))
        arrayList.add(ServiceItem(R.drawable.plumber,R.string.Plumber))
        arrayList.add(ServiceItem(R.drawable.electrician,R.string.Electrician))
        arrayList.add(ServiceItem(R.drawable.carpenter,R.string.Carpenter))
        arrayList.add(ServiceItem(R.drawable.tileshandyman,R.string.Tiles_Handyman))
        arrayList.add(ServiceItem(R.drawable.parquet,R.string.Parquet))
        arrayList.add(ServiceItem(R.drawable.smith,R.string.Smith))
        arrayList.add(ServiceItem(R.drawable.masondecorationstones,R.string.Decoration_Stones))
        arrayList.add(ServiceItem(R.drawable.alumetal,R.string.Alumetal))
        arrayList.add(ServiceItem(R.drawable.airconditioner,R.string.Air_Conditioner))
        arrayList.add(ServiceItem(R.drawable.curtains,R.string.Curtains))
        arrayList.add(ServiceItem(R.drawable.glass,R.string.Glass))
        arrayList.add(ServiceItem(R.drawable.satellite,R.string.Satellite))
        arrayList.add(ServiceItem(R.drawable.gypsumworks,R.string.Gypsum_Works))
        arrayList.add(ServiceItem(R.drawable.marbleandgranite,R.string.Marble))
        arrayList.add(ServiceItem(R.drawable.pestcontrol,R.string.Pest_Control))
        arrayList.add(ServiceItem(R.drawable.woodpainter,R.string.Wood_Painter))
        arrayList.add(ServiceItem(R.drawable.swimmingpool,R.string.Swimming_pool))

        return arrayList
    }

    }

