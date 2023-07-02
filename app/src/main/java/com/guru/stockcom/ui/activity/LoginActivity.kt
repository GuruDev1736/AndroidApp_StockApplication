package com.guru.stockcom.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.guru.stockcom.databinding.ActivityLoginBinding
import com.guru.stockcom.network.ApiHelper
import com.guru.stockcom.network.MainViewModel
import com.guru.stockcom.network.RetrofitBuilder
import com.guru.stockcom.network.ViewModelFactory
import com.guru.stockcom.utils.Constant
import com.guru.stockcom.utils.NetworkUtils
import com.guru.stockcom.utils.ShowProgressDialog
import com.guru.stockcom.utils.Status

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var username: String
    private lateinit var pin: String
    private lateinit var progressDialog: ShowProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController!!.hide(
                android.view.WindowInsets.Type.statusBars()
            )
        }

        setContentView(binding.root)

        initComponent()

    }

    private fun initComponent() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )[MainViewModel::class.java]

        progressDialog = ShowProgressDialog(this@LoginActivity)


        binding.btnLogin.setOnClickListener {
            if (valid()) {
                if (NetworkUtils.isNetworkAvailable(this@LoginActivity)) {
                    callLogin()
                } else {
                    NetworkUtils.isNetworkNotAvailable(this@LoginActivity)
                }
            }
        }

        binding.tvEN.setOnClickListener {
            changeLang("en")
            recreate()
        }

        binding.tvHindi.setOnClickListener {
            changeLang("hi")
            recreate()
        }
        binding.tvMarathi.setOnClickListener {
            changeLang("mr")
            recreate()
        }
    }

    private fun valid(): Boolean {
        username = binding.edtUserName.text.toString()
        pin = binding.edtPin.text.toString()

        if (username == "") {
            binding.edtUserName.error = "Enter Username"
            binding.edtUserName.requestFocus()
            return false
        } else if (pin == "") {
            binding.edtPin.error = "Enter Pin"
            binding.edtPin.requestFocus()
            return false
        }
        return true
    }

    private fun callLogin() {
        viewModel.callLogin(username, pin).observe(this@LoginActivity) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressDialog.dismissDialog()

                        if (resource.data?.code == "1") {

                            sharedUtils.boolPref(Constant.LOGIN, true)
                            resource.data.let {
                                sharedUtils.stringPref(Constant.ID,it.user_list[0].id)
                                sharedUtils.stringPref(Constant.NAME,it.user_list[0].name)
                                sharedUtils.stringPref(Constant.MOBILE,it.user_list[0].mobile_no)
                                sharedUtils.stringPref(Constant.TYPE,it.user_list[0].type)
                            }

                            Toast.makeText(
                                applicationContext,
                                "Login Successfully",
                                Toast.LENGTH_LONG
                            ).show()

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Invalid Username or Password",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    Status.ERROR -> {
                        progressDialog.dismissDialog()

                        Toast.makeText(
                            applicationContext,
                            "Invalid Username or Password",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    Status.LOADING -> {
                        progressDialog.showDialog()
                    }
                }
            }
        }

    }
}