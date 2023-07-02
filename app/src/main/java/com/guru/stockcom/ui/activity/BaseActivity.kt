package com.guru.stockcom.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.avs.sudhangoldapp.utils.SharedUtils
import com.guru.stockcom.utils.Constant
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    lateinit var sharedUtils: SharedUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intiComponent()
    }

    private fun intiComponent() {
        sharedUtils = SharedUtils(this@BaseActivity)

        val lang = sharedUtils.getStringPref(Constant.LANGUAGE)
        changeLang(lang)
    }

    fun changeLang(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()

        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        sharedUtils.stringPref(Constant.LANGUAGE, lang)
    }

}