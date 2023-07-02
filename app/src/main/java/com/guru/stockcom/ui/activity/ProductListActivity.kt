package com.guru.stockcom.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.guru.stockcom.databinding.ActivityProductListBinding
import com.guru.stockcom.model.ProductList
import com.guru.stockcom.network.ApiHelper
import com.guru.stockcom.network.MainViewModel
import com.guru.stockcom.network.RetrofitBuilder
import com.guru.stockcom.network.ViewModelFactory
import com.guru.stockcom.ui.adapter.ProductListAdapter
import com.guru.stockcom.utils.NetworkUtils
import com.guru.stockcom.utils.ShowProgressDialog
import com.guru.stockcom.utils.Status

class ProductListActivity : BaseActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var productAdapter: ProductListAdapter
    private lateinit var progressDialog: ShowProgressDialog

    companion object CompanionObject {
        var isRefresh = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initComponent()
    }

    private fun initComponent() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )[MainViewModel::class.java]

        progressDialog = ShowProgressDialog(this@ProductListActivity)

        binding.toolbar.tvName.text = "Product List"

        binding.toolbar.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.floatAddProduct.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }


        if (NetworkUtils.isNetworkAvailable(this@ProductListActivity)) {
            callProductList()
        } else {
            NetworkUtils.isNetworkNotAvailable(this@ProductListActivity)
        }

    }

    private fun callProductList() {
        viewModel.callProductList().observe(this@ProductListActivity) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressDialog.dismissDialog()

                        if (resource.data?.code == "1") {
                            productAdapter = ProductListAdapter {
                                onClick(it)
                            }
                            binding.rvProductList.apply {
                                layoutManager =
                                    LinearLayoutManager(
                                        this@ProductListActivity,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                binding.rvProductList.adapter = productAdapter
                            }
                            productAdapter.submitList(it.data?.product_list)

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
    override fun onResume() {
        super.onResume()
        if (isRefresh) {
            callProductList()
            isRefresh = false
        }
    }

    private fun onClick(product: ProductList) {
        Toast.makeText(this, "${product.name} clicked", Toast.LENGTH_SHORT).show()
    }
}