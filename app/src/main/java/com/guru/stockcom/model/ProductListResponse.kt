package com.guru.stockcom.model

data class ProductListResponse(

    var message: String,
    var code: String,
    var product_list: ArrayList<ProductList>
)

data class ProductList(
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
    var avaliable_stock: String,
    var shopkeeper_id: String,
    var profit_margin: String,
    var exp_date: String,
)
