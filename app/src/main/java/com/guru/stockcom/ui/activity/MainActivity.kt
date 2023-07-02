package com.guru.stockcom.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.guru.stockcom.R
import com.guru.stockcom.databinding.ActivityMainBinding
import com.guru.stockcom.ui.fragment.HomeFragment
import com.guru.stockcom.utils.Constant


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initComponent()

    }

    private fun initComponent() {
        drawerLayout = binding.drawerLayout
        setSupportActionBar(binding.toolbar)

        binding.navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment()).commit()

        binding.navView.setCheckedItem(R.id.nav_home)

        val headerView = binding.navView.getHeaderView(0)
        val tvName = headerView.findViewById<TextView>(R.id.tvName)
        tvName.text = sharedUtils.getStringPref(Constant.NAME)

        val role = sharedUtils.getStringPref(Constant.TYPE)

        if (role == "1") {
            binding.tvTitle.text = "Admin App"
            FirebaseMessaging.getInstance().subscribeToTopic("Admin")
        } else {
            binding.tvTitle.text = "Shopkeeper App"
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            R.id.nav_logout -> logout()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        val mBuilder = AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want Logout?")
            .setPositiveButton("Yes", null)
            .setNegativeButton("No", null)
            .setCancelable(false)
            .show()

        val mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
        val mNegativeButton = mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE)
        mPositiveButton.setOnClickListener {
            sharedUtils.clearPref()
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Admin")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        mNegativeButton.setOnClickListener {
            mBuilder.hide()
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackPressLogic()
        }
    }

    // ============ End Double tab back press logic =================
    private fun doubleBackPressLogic() {
        if (!doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click back again to exit !!", Toast.LENGTH_SHORT).show()
            Handler(Looper.myLooper()!!).postDelayed(Runnable {
                doubleBackToExitPressedOnce = false
            }, 1000)
        } else {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}
