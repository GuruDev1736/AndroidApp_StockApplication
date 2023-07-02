package com.guru.stockcom.ui.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.guru.stockcom.databinding.ActivityAddProductBinding
import com.guru.stockcom.network.ApiHelper
import com.guru.stockcom.network.MainViewModel
import com.guru.stockcom.network.RetrofitBuilder
import com.guru.stockcom.network.ViewModelFactory
import com.guru.stockcom.utils.NetworkUtils
import com.guru.stockcom.utils.ShowProgressDialog


class AddProductActivity : BaseActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var progressDialog: ShowProgressDialog
    private var name = ""
    private var price = ""
    private var weight = ""
    private var stock = ""
    private var status = "1"
    private var profitMargin = ""
    private var expDate = ""
    private var profit = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddProductBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initComponent()
    }

    private fun initComponent() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )[MainViewModel::class.java]

        progressDialog = ShowProgressDialog(this@AddProductActivity)

        binding.toolbar.tvName.text = "Add Product"

        binding.toolbar.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.toolbar.ivHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        binding.btnSubmit.setOnClickListener {
            if (valid()) {
                if (NetworkUtils.isNetworkAvailable(this@AddProductActivity)) {
                    callAddProduct()

                } else {
                    NetworkUtils.isNetworkNotAvailable(this@AddProductActivity)
                }
            }
        }
    }

    private fun callAddProduct() {

        profit = profitMargin.toDouble() * stock.toDouble()
        Log.d("GURU",profit.toString())

        viewModel.callAddProduct(name, price, weight, stock, status,profit.toString(),expDate)
            .observe(this@AddProductActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        com.guru.stockcom.utils.Status.SUCCESS -> {
                            progressDialog.dismissDialog()

                            if (it.data?.code == "1") {
                                val sweetAlertDialog =
                                    SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                sweetAlertDialog.titleText = "Product Add Successfully"
                                sweetAlertDialog.confirmText = "okay"
                                sweetAlertDialog.showCancelButton(false)
                                sweetAlertDialog.setConfirmClickListener { sDialog ->
                                    sDialog.cancel()
                                    ProductListActivity.isRefresh = true
                                    onBackPressedDispatcher.onBackPressed()
                                }
                                sweetAlertDialog.show()
                            } else {
                                val sweetAlertDialog =
                                    SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                sweetAlertDialog.titleText = it.data?.message
                                sweetAlertDialog.confirmText = "okay"
                                sweetAlertDialog.showCancelButton(false)
                                sweetAlertDialog.setConfirmClickListener { sDialog ->
                                    sDialog.cancel()
                                }
                                sweetAlertDialog.show()
                            }
                        }

                        com.guru.stockcom.utils.Status.ERROR -> {
                            progressDialog.dismissDialog()

                        }

                        com.guru.stockcom.utils.Status.LOADING -> {
                            progressDialog.showDialog()
                        }
                    }
                }
            }
    }

    private fun valid(): Boolean {
        name = binding.etProdName.text.toString()
        price = binding.etPrice.text.toString()
        weight = binding.etQty.text.toString()
        stock = binding.prodStock.text.toString()
        profitMargin = binding.etProfitMargin.text.toString()
        expDate = binding.etExpDate.text.toString()

        if (name.isEmpty()) {
            binding.etProdName.error = "Enter Produdct Name"
            binding.etProdName.requestFocus()
            return false
        } else if (price.isEmpty()) {
            binding.etPrice.error = "Enter Price"
            binding.etPrice.requestFocus()
            return false
        } else if (stock.isEmpty()) {
            binding.prodStock.error = "Enter Stock"
            binding.prodStock.requestFocus()
            return false
        } else if (profitMargin.isEmpty()) {
            binding.etProfitMargin.error = "Enter Profit Margin"
            binding.etProfitMargin.requestFocus()
            return false
        } else if (expDate.isEmpty()) {
            binding.etExpDate.error = "Enter Expiry Date"
            binding.etExpDate.requestFocus()
            return false
        }

        return true

    }
}