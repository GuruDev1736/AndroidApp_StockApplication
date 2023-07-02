package com.guru.stockcom.model

data class LoginResponse(
    var message: String,
    var code: String,
    var user_list: ArrayList<Login>
)
data class Login(
    var id: String,
    var name: String,
    var mobile_no: String,
    var type: String
)