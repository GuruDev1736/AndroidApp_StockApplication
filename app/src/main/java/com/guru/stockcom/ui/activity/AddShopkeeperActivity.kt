package com.guru.stockcom.ui.activity


import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.guru.stockcom.R
import com.guru.stockcom.databinding.ActivityAddShopkeeperBinding
import com.guru.stockcom.network.ApiHelper
import com.guru.stockcom.network.MainViewModel
import com.guru.stockcom.network.RetrofitBuilder
import com.guru.stockcom.network.ViewModelFactory
import com.guru.stockcom.utils.NetworkUtils
import com.guru.stockcom.utils.ShowProgressDialog


class AddShopkeeperActivity : BaseActivity() {

    private lateinit var binding: ActivityAddShopkeeperBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var progressDialog: ShowProgressDialog
    private var name = ""
    private var username = ""
    private var password = ""
    private var phoneNo = ""
    private var status = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddShopkeeperBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initComponent()
    }

    private fun initComponent() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )[MainViewModel::class.java]

        binding.toolbar.tvName.text = "Add Shopkeeper"
        progressDialog = ShowProgressDialog(this@AddShopkeeperActivity)

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
                if (NetworkUtils.isNetworkAvailable(this@AddShopkeeperActivity)) {
                    callAddShopkeeper()
                } else {
                    NetworkUtils.isNetworkNotAvailable(this@AddShopkeeperActivity)
                }
            }
        }
        binding.shopRadioGrp.setOnCheckedChangeListener { group, checkedId ->
            if (R.id.rbActive == checkedId) {
                status = "1"
            } else {
                status = "0"
            }
        }
    }

    private fun callAddShopkeeper() {
        viewModel.callRegisterShopkeeper(name, username, password, phoneNo, status)
            .observe(this@AddShopkeeperActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        com.guru.stockcom.utils.Status.SUCCESS -> {
                            progressDialog.dismissDialog()

                            if (it.data?.code == "1") {
                                val sweetAlertDialog =
                                    SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                sweetAlertDialog.titleText = "Shopkeeper Register Successfully"
                                sweetAlertDialog.confirmText = "okay"
                                sweetAlertDialog.showCancelButton(false)
                                sweetAlertDialog.setConfirmClickListener { sDialog ->
                                    sDialog.cancel()
                                    ShopkeeperListActivity.isRefresh = true
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
        name = binding.etName.text.toString()
        username = binding.etUsername.text.toString()
        password = binding.etPass.text.toString()
        phoneNo = binding.etPhoneNo.text.toString()

        if (name.isEmpty()) {
            binding.etName.error = "Enter Name"
            binding.etName.requestFocus()
            return false
        } else if (username.isEmpty()) {
            binding.etUsername.error = "Enter Username"
            binding.etUsername.requestFocus()
            return false
        } else if (password.isEmpty()) {
            binding.etPass.error = "Enter Password"
            binding.etPass.requestFocus()
            return false
        } else if (password.length <= 5) {
            binding.etPass.error = "Enter more then 6 char password"
            binding.etPass.requestFocus()
            return false
        } else if (password.isEmpty()) {
            binding.etPhoneNo.error = "Enter Mobile No"
            binding.etPhoneNo.requestFocus()
            return false
        } else if (phoneNo.length < 10) {
            binding.etPhoneNo.error = "Enter Valid Mobile No"
            binding.etPhoneNo.requestFocus()
            return false
        }

        return true

    }
}