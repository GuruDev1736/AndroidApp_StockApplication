package com.guru.stockcom.utils

import java.text.SimpleDateFormat
import java.util.Date

class DateUtils {

    companion object {

        fun changeDateFormat(date: String): String {
            var spf = SimpleDateFormat("yyyy-MM-dd")
            val newDate: Date = spf.parse(date)
            spf = SimpleDateFormat("dd-MM-yyyy")
            return spf.format(newDate)
        }

    }
}