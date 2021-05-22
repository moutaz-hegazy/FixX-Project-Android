package com.example.fixx.NavigationBar.OrdersScreen.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fixx.R
import com.example.fixx.databinding.FragmentOnGuaranteeOrdersBinding
import com.example.fixx.databinding.OnGuaranteeOrderRecyclerRowBinding


class OnGuaranteeOrdersFragment : Fragment() {
    private lateinit var binding : FragmentOnGuaranteeOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnGuaranteeOrdersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}