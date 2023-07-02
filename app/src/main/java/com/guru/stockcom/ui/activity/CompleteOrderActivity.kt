package com.guru.stockcom.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.guru.stockcom.databinding.ActivityCompleteOrderBinding
import com.guru.stockcom.model.SaleReport
import com.guru.stockcom.network.ApiHelper
import com.guru.stockcom.network.MainViewModel
import com.guru.stockcom.network.RetrofitBuilder
import com.guru.stockcom.network.ViewModelFactory
import com.guru.stockcom.ui.adapter.ShopKeeperReportAdapter
import com.guru.stockcom.utils.Constant
import com.guru.stockcom.utils.NetworkUtils
import com.guru.stockcom.utils.ShowProgressDialog
import com.guru.stockcom.utils.Status

class CompleteOrderActivity : BaseActivity() {
    private lateinit var binding: ActivityCompleteOrderBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var shopKeeperReportAdapter: ShopKeeperReportAdapter
    private lateinit var progressDialog: ShowProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCompleteOrderBinding.inflate(layoutInflater)

        setContentView(binding.root)
        intiComponent()
    }

    private fun intiComponent() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )[MainViewModel::class.java]

        progressDialog = ShowProgressDialog(this@CompleteOrderActivity)


        binding.toolbar.tvName.text = "Completed Order Details"

        binding.toolbar.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.toolbar.ivHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }


        if (NetworkUtils.isNetworkAvailable(this@CompleteOrderActivity)) {
            completedOrderList()
        } else {
            NetworkUtils.isNetworkNotAvailable(this@CompleteOrderActivity)
        }
    }

    private fun completedOrderList() {
        val intent = intent.extras
        var id = intent?.getString("ID")
        if (id!!.isEmpty()) {
            id = sharedUtils.getStringPref(Constant.ID)
        }

        viewModel.callShopkeeperReport(id).observe(this@CompleteOrderActivity) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressDialog.dismissDialog()

                        if (resource.data?.code == "1") {
                            shopKeeperReportAdapter = ShopKeeperReportAdapter {
                                onClick(it)
                            }
                            binding.rvCompletedOrders.apply {
                                layoutManager =
                                    LinearLayoutManager(
                                        this@CompleteOrderActivity,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                binding.rvCompletedOrders.adapter = shopKeeperReportAdapter
                            }
                            shopKeeperReportAdapter.submitList(it.data?.product_list)

                        }
                    }
                    Status.ERROR -> {
                        progressDialog.dismissDialog()

                    }
                    Status.LOADING -> {
                        progressDialog.showDialog()
                    }
                }
            }
        }
    }


    private fun onClick(saleReport: SaleReport) {
        Toast.makeText(this, "${saleReport.name} clicked", Toast.LENGTH_SHORT).show()
    }
}