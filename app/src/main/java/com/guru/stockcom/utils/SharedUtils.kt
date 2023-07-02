package com.avs.sudhangoldapp.utils

import android.content.Context
import android.content.SharedPreferences

class SharedUtils {

    private val sharedPref = "SudhanGold"
    private var sharedPreferences: SharedPreferences


    constructor(context: Context) {
        sharedPreferences = context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
    }

    fun stringPref(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
        editor.commit()
    }

    fun boolPref(key: String, value: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
        editor.commit()
    }

    fun getStringPref(key: String): String {
        return sharedPreferences.getString(key, "").toString()
    }

    fun getBoolPref(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
    fun clearPref()
    {
        sharedPreferences.edit().clear().commit()
    }
}