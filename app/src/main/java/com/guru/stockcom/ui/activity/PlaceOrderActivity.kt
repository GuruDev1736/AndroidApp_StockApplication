package com.guru.stockcom.ui.activity

import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.guru.stockcom.databinding.ActivityPlaceOrderBinding
import com.guru.stockcom.model.ProductList
import com.guru.stockcom.network.ApiHelper
import com.guru.stockcom.network.MainViewModel
import com.guru.stockcom.network.RetrofitBuilder
import com.guru.stockcom.network.ViewModelFactory
import com.guru.stockcom.utils.Constant
import com.guru.stockcom.utils.NetworkUtils
import com.guru.stockcom.utils.ShowProgressDialog
import com.guru.stockcom.utils.Status



class PlaceOrderActivity : BaseActivity() {
    private lateinit var binding: ActivityPlaceOrderBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var progressDialog: ShowProgressDialog
    private lateinit var strProductList: ArrayList<String>
    private lateinit var productList: ArrayList<ProductList>
    private var prodId: String = ""
    private var prodName: String = ""
    private var prodPrice: Int = 0
    private var prodQty: String = ""
    private var availableStock: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlaceOrderBinding.inflate(layoutInflater)

        setContentView(binding.root)

        intiComponent()
    }

    private fun intiComponent() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )[MainViewModel::class.java]

        progressDialog = ShowProgressDialog(this@PlaceOrderActivity)

        binding.toolbar.tvName.text = "Place Order"

        binding.toolbar.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.toolbar.ivHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        if (NetworkUtils.isNetworkAvailable(this@PlaceOrderActivity)) {
            callProductList()
        } else {
            NetworkUtils.isNetworkNotAvailable(this@PlaceOrderActivity)
        }

        binding.spPlaceOrder.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == "Select Product") {
                    prodId = ""
                    prodName = ""
                    binding.tvAvailableStock.visibility = View.GONE
                } else {
                    val product = productList[position - 1]
                    prodId = product.id
                    prodName = product.name
                    prodPrice = product.price.toInt()
                    binding.tvAvailableStock.visibility = View.VISIBLE
                    binding.tvExpDate.visibility = View.VISIBLE
                    binding.tvAvailableStock.text =
                        "Available Stock=" + product.avaliable_stock + " KG"

                    binding.tvExpDate.text = "Expiry Date=" + product.exp_date
                    availableStock = product.avaliable_stock.toInt()

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        binding.etQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val qty = binding.etQuantity.text
                if (qty.toString() != "") {
                    if (qty.toString().toInt() > availableStock) {
                        Toast.makeText(
                            this@PlaceOrderActivity,
                            "Quantity can't more then Available Stock ",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val finalPrice = qty.toString().toInt() * prodPrice
                        binding.etPrice.setText(finalPrice.toString())
                    }
                } else {
                    binding.etPrice.setText("")
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.submitOrder.setOnClickListener {
            if (valid()) {
                if (NetworkUtils.isNetworkAvailable(this@PlaceOrderActivity)) {
                    callPlaceOrder()
                } else {
                    NetworkUtils.isNetworkNotAvailable(this@PlaceOrderActivity)
                }
            }
        }

    }

    private fun valid(): Boolean {

        prodQty = binding.etQuantity.text.toString()


        if (prodId == "") {
            Toast.makeText(this@PlaceOrderActivity, "Select Product", Toast.LENGTH_LONG).show()
            return false
        } else if (prodQty == "") {
            Toast.makeText(
                this@PlaceOrderActivity,
                "Enter Quantity",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (prodQty.toInt() > availableStock) {
            Toast.makeText(
                this@PlaceOrderActivity,
                "Quantity can't more then Available Stock ",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    private fun callProductList() {
        viewModel.callProductList().observe(this@PlaceOrderActivity) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressDialog.dismissDialog()

                        if (resource.data?.code == "1") {
                            productList = ArrayList()
                            strProductList = ArrayList()

                            productList = resource.data.product_list

                            strProductList.add("Select Product")

                            for (item in productList) {
                                strProductList.add(item.name)
                            }

                            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                this,
                                android.R.layout.simple_spinner_item, strProductList
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spPlaceOrder.adapter = adapter
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


    private fun callPlaceOrder() {
        val id = sharedUtils.getStringPref(Constant.ID)
        val name = sharedUtils.getStringPref(Constant.NAME)

        viewModel.callPlaceOrder(
            prodId,
            prodName,
            binding.etPrice.text.toString(),
            binding.etQuantity.text.toString(),
            id,
            name
        )
            .observe(this@PlaceOrderActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        com.guru.stockcom.utils.Status.SUCCESS -> {
                            progressDialog.dismissDialog()

                            if (it.data?.code == "1") {

                                val smsManager = SmsManager.getDefault()

                                try {

                                    val destination = "8983271332"
                                    val message = "Order Has Been Placed \n\n" +
                                            " Name of ShopKeeper : "+name+"\n"+
                                            " Product Name : "+prodName+"\n"+
                                            " Quantity : "+binding.etQuantity.text.toString()+"\n"+
                                            " Price : "+binding.etPrice.text.toString()

                                    smsManager.sendTextMessage(destination, null, message, null, null)
                                    Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show()

                                } catch (e: Exception) {

                                    Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show()

                                }

                                val sweetAlertDialog =
                                    SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                sweetAlertDialog.titleText = "Place Order Successfully"
                                sweetAlertDialog.confirmText = "okay"
                                sweetAlertDialog.showCancelButton(false)
                                sweetAlertDialog.setConfirmClickListener { sDialog ->
                                    sDialog.cancel()
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
}