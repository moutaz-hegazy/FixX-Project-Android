package com.example.project.bottom_navigation_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.fixx.LoginScreen.Views.PickJob
import com.example.fixx.NavigationBar.HomeScreen.NotificationCounter
import com.example.fixx.NavigationBar.notification.NotificationFragment
import com.example.fixx.R
import com.example.fixx.takeOrderScreen.views.CustomizeOrderActivity

import com.example.project.ServiceAdapter
import com.example.fixx.POJOs.ServiceItem
import com.example.fixx.constants.Constants

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var gridView: GridView? = null
    private var arrayList = ArrayList<ServiceItem>()
    private var serviceAdapter : ServiceAdapter? =null
    var button: Button? = null
    var notificationbtn : ImageView? = null
    var notificationCounter: NotificationCounter? = null




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
        serviceAdapter = context?.applicationContext?.let {
            ServiceAdapter(it, arrayList)
        }
        gridView?.apply {
            adapter = serviceAdapter
            setOnItemClickListener { parent, view, position, id ->
                startCustomizeOrderActivity(position)
            }
        }


        button = rootView.findViewById(R.id.homefragment_notificationcounter_button)
        notificationbtn = rootView.findViewById(R.id.homefragment_notification_image_view)

        notificationbtn?.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.home_fragment_id, NotificationFragment())?.commit()
        }

       notificationCounter =
              NotificationCounter(rootView.findViewById(R.id.homefragment_notificationcounter_card_view))
        // Inflate the layout for this fragment

        button?.setOnClickListener(View.OnClickListener {
            notificationCounter!!.increaseNumber()
        })
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