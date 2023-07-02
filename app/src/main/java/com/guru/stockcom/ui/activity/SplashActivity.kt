package com.guru.stockcom.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.guru.stockcom.databinding.ActivitySplashBinding
import com.guru.stockcom.utils.Constant
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController!!.hide(
                android.view.WindowInsets.Type.statusBars()
            )
        }

        setContentView(binding.root)
        initComponent()
    }

    private fun initComponent() {

        Handler(Looper.getMainLooper()).postDelayed({

                Dexter.withContext(this)
                    .withPermissions(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.CALL_PHONE
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            if (report?.areAllPermissionsGranted() == true) {

                                val intent = Intent(this@SplashActivity,LoginActivity::class.java)
                                startActivity(intent)
                                finishAffinity()

                            } else {

                                Toast.makeText(
                                    this@SplashActivity,
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

        }, 3000)
    }
}