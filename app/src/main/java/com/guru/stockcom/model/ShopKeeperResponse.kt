package com.guru.stockcom.model

data class ShopKeeperResponse(
    var message: String,
    var code: String,
    var shopkeper_list: ArrayList<ShopKeeperList>
)

data class ShopKeeperList(
    var id: String,
    var name: String,
    var username: String,
    var password: String,
    var mobile_no: String,
    var status: String,
    var type: String
)
