package com.guru.stockcom.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avs.sudhangoldapp.utils.SharedUtils
import com.guru.stockcom.databinding.FragmentHomeBinding
import com.guru.stockcom.ui.activity.CompleteOrderActivity
import com.guru.stockcom.ui.activity.PlaceOrderActivity
import com.guru.stockcom.ui.activity.ProductListActivity
import com.guru.stockcom.ui.activity.ShopkeeperListActivity
import com.guru.stockcom.utils.Constant

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var role: String
    lateinit var sharedUtils: SharedUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initComponent()

        return binding.root
    }

    private fun initComponent() {
        sharedUtils = SharedUtils(requireContext())
        role = sharedUtils.getStringPref(Constant.TYPE)

        if (role == "1") {
            binding.llAdminHome.visibility = View.VISIBLE
            binding.llShopKeeperHome.visibility = View.GONE
        } else {
            binding.llAdminHome.visibility = View.GONE
            binding.llShopKeeperHome.visibility = View.VISIBLE
        }


        binding.CvAddProduct.setOnClickListener {
            val intent = Intent(context, ProductListActivity::class.java)
            startActivity(intent)
        }

        binding.cvShopkeeper.setOnClickListener {
            val intent = Intent(context, ShopkeeperListActivity::class.java)
            startActivity(intent)
        }

        binding.CvPlaceOrder.setOnClickListener {
            val intent = Intent(context, PlaceOrderActivity::class.java)
            startActivity(intent)
        }

        binding.cvCompletedOrder.setOnClickListener {
            val intent = Intent(context, CompleteOrderActivity::class.java)
            intent.putExtra("ID","")
            startActivity(intent)
        }
    }
}