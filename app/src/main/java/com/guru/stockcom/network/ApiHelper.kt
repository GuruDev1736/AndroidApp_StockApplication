package com.guru.stockcom.network


class ApiHelper(private val apiService: ApiService) {

    suspend fun callLogin(username: String, pin: String) = apiService.callLogin(username, pin)
    suspend fun callProductList() = apiService.callProductList()
    suspend fun callShopkeeperList() = apiService.callShopkeeperList()
    suspend fun callShopkeeperReport(id: String) = apiService.callShopkeeperReport(id)
    suspend fun callRegisterShopkeeper(
        name: String, username: String, password: String, mobile_no: String, status: String
    ) = apiService.callRegisterShopkeeper(name, username, password, mobile_no, status)

    suspend fun callAddProduct(
        name: String,
        price: String,
        weight: String,
        stock: String,
        status: String,
        profitMargin: String,
        expDate: String
    ) = apiService.callAddProduct(name, price, weight, stock, status,profitMargin,expDate)

    suspend fun callPlaceOrder(
        id: String,
        name: String,
        price: String,
        stock: String,
        shopkeeper_id: String,
        shopkeeper_name: String
    ) = apiService.callPlaceOrder(id, name, price, stock, shopkeeper_id, shopkeeper_name)

    suspend fun deleteShopkeeper(
        id: String,
    ) = apiService.deleteShopkeeper(id)


}