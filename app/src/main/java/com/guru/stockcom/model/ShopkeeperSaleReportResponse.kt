package com.guru.stockcom.model

data class ShopkeeperSaleReportResponse(
    var message: String,
    var code: String,
    var product_list: ArrayList<SaleReport>
)

data class SaleReport(
    var id: String,
    var name: String,
    var weight: String,
    var qty: String,
    var price: String,
    var stock: String,
    var status: String,
    var stock_status: String,
    var out_prod_id: String,
    var in_date: String,
    var out_date: String,
    var shopkeeper_id: String
)
