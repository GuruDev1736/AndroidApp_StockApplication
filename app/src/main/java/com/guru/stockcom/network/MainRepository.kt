package com.guru.stockcom.network

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun callLogin(username: String, pin: String) = apiHelper.callLogin(username, pin)

    suspend fun callProductList() = apiHelper.callProductList()
    suspend fun callShopkeeperList() = apiHelper.callShopkeeperList()
    suspend fun callShopkeeperReport(id: String) = apiHelper.callShopkeeperReport(id)
    suspend fun callRegisterShopkeeper(
        name: String,
        username: String,
        password: String,
        mobile_no: String,
        status: String
    ) = apiHelper.callRegisterShopkeeper(name, username, password, mobile_no, status)

    suspend fun callAddProduct(
        name: String,
        price: String,
        weight: String,
        stock: String,
        status: String,
        profitMargin: String,
        expDate: String
    ) = apiHelper.callAddProduct(name, price, weight, stock, status,profitMargin,expDate)


    suspend fun callPlaceOrder(
        id: String,
        name: String,
        price: String,
        stock: String,
        shopkeeper_id: String,
        shopkeeper_name: String
    ) = apiHelper.callPlaceOrder(id,name, price, stock, shopkeeper_id,shopkeeper_name)

    suspend fun deleteShopkeeper(
        id: String,
    ) = apiHelper.deleteShopkeeper(id)

}