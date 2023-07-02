package com.guru.stockcom.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.guru.stockcom.databinding.ActivityShopkeeperListBinding
import com.guru.stockcom.model.ShopKeeperList
import com.guru.stockcom.network.ApiHelper
import com.guru.stockcom.network.MainViewModel
import com.guru.stockcom.network.RetrofitBuilder
import com.guru.stockcom.network.ViewModelFactory
import com.guru.stockcom.ui.adapter.ShopkeeperListAdapter
import com.guru.stockcom.utils.NetworkUtils
import com.guru.stockcom.utils.ShowProgressDialog
import com.guru.stockcom.utils.Status
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder.MultiPermissionListener
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener

class ShopkeeperListActivity : BaseActivity(), HandlerShopkeeper {

    private lateinit var binding: ActivityShopkeeperListBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var shopkeeperAdapter: ShopkeeperListAdapter
    private lateinit var progressDialog: ShowProgressDialog

    companion object CompanionObject {
        var isRefresh = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShopkeeperListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initComponent()
    }

    private fun initComponent() {

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )[MainViewModel::class.java]

        progressDialog = ShowProgressDialog(this@ShopkeeperListActivity)

        binding.toolbar.tvName.text = "Shopkeeper List"

        binding.toolbar.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.fbAddShop.setOnClickListener {
            val intent = Intent(this, AddShopkeeperActivity::class.java)
            startActivity(intent)
        }
        if (NetworkUtils.isNetworkAvailable(this@ShopkeeperListActivity)) {
            callShopkepper()
        } else {
            NetworkUtils.isNetworkNotAvailable(this@ShopkeeperListActivity)
        }

    }

    private fun callShopkepper() {
        viewModel.callShopkeeperList().observe(this@ShopkeeperListActivity) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressDialog.dismissDialog()

                        if (resource.data?.code == "1") {
                            shopkeeperAdapter = ShopkeeperListAdapter(this)
                            binding.rvShopkeeper.apply {
                                layoutManager =
                                    LinearLayoutManager(
                                        this@ShopkeeperListActivity,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                binding.rvShopkeeper.adapter = shopkeeperAdapter
                            }
                            shopkeeperAdapter.submitList(it.data?.shopkeper_list)

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

    private fun callDeleteShopkeeper(id: String) {
        viewModel.deleteShopkeeper(id).observe(this@ShopkeeperListActivity) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressDialog.dismissDialog()

                        if (resource.data?.code == "1") {
                            val sweetAlertDialog =
                                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            sweetAlertDialog.titleText = "Delete Successfully"
                            sweetAlertDialog.confirmText = "Okay"
                            sweetAlertDialog.showCancelButton(false)
                            sweetAlertDialog.setConfirmClickListener { sDialog ->
                                sDialog.dismiss()
                                callShopkepper()
                            }
                            sweetAlertDialog.show()
                        } else {
                            val sweetAlertDialog =
                                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            sweetAlertDialog.titleText = "Something went to wrong"
                            sweetAlertDialog.confirmText = "Okay"
                            sweetAlertDialog.showCancelButton(false)
                            sweetAlertDialog.setConfirmClickListener { sDialog ->
                                sDialog.dismiss()
                                callShopkepper()
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

    private fun onClick(shopkeeper: ShopKeeperList) {
        Toast.makeText(this, "${shopkeeper.name} clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        if (isRefresh) {
            callShopkepper()
            isRefresh = false
        }
    }

    override fun onClickEdit(shopkeeper: ShopKeeperList) {
        val sweetAlertDialog =
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        sweetAlertDialog.titleText = "Delete Shopkeeper"
        sweetAlertDialog.contentText = "Are you sure.You want to delete Shopkeeper"
        sweetAlertDialog.confirmText = "Yes"
        sweetAlertDialog.cancelText = "No"
        sweetAlertDialog.showCancelButton(true)
        sweetAlertDialog.setConfirmClickListener { sDialog ->
            sDialog.dismiss()
            callDeleteShopkeeper(shopkeeper.id)
        }
        sweetAlertDialog.setCancelClickListener { sDialog ->
            sDialog.dismiss()
        }
        sweetAlertDialog.show()
    }

    override fun onClickReport(shopkeeper: ShopKeeperList) {
        val intent = Intent(this@ShopkeeperListActivity, CompleteOrderActivity::class.java)
        intent.putExtra("ID", shopkeeper.id)
        startActivity(intent)
    }

    override fun onClickCall(shopkeeper: ShopKeeperList) {

        // Check if permissions are already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {

            val dialIntent = Intent(Intent.ACTION_CALL)
            dialIntent.data = Uri.parse("tel:" + shopkeeper.mobile_no)
            startActivity(dialIntent)

        } else {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.CALL_PHONE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (report?.areAllPermissionsGranted() == true) {

                            val dialIntent = Intent(Intent.ACTION_CALL)
                            dialIntent.data = Uri.parse("tel:" + shopkeeper.mobile_no)
                            startActivity(dialIntent)

                        } else {

                            Toast.makeText(
                                this@ShopkeeperListActivity,
                                "Need All Permissions",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {

                        token?.continuePermissionRequest()
                    }
                })
                .check()
        }

    }
}

interface HandlerShopkeeper {

    fun onClickEdit(shopkeeper: ShopKeeperList)
    fun onClickReport(shopkeeper: ShopKeeperList)
    fun onClickCall(shopkeeper: ShopKeeperList)
}