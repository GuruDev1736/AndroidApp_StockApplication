package com.guru.stockcom.network

import com.guru.stockcom.model.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    @POST("ws_login.php")
    @FormUrlEncoded
    suspend fun callLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("ws_product_list.php")
    suspend fun callProductList(): ProductListResponse

    @GET("ws_shopkeeper.php")
    suspend fun callShopkeeperList(): ShopKeeperResponse

    @POST("ws_product_list_shopkeeper.php")
    @FormUrlEncoded
    suspend fun callShopkeeperReport(@Field("id") id: String): ShopkeeperSaleReportResponse

    @POST("ws_register_shopkepper.php")
    @FormUrlEncoded
    suspend fun callRegisterShopkeeper(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("mobile_no") mobile_no: String,
        @Field("status") status: String,
    ): CommonResponse

    @POST("ws_add_product.php")
    @FormUrlEncoded
    suspend fun callAddProduct(
        @Field("name") name: String,
        @Field("price") price: String,
        @Field("weight") weight: String,
        @Field("stock") stock: String,
        @Field("status") status: String,
        @Field("profitMargin") profitMargin: String,
        @Field("expDate") expDate: String,
    ): CommonResponse

    @POST("ws_place_order.php")
    @FormUrlEncoded
    suspend fun callPlaceOrder(
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("price") price: String,
        @Field("stock") stock: String,
        @Field("shopkeeper_id") shopkeeper_id: String,
        @Field("shopkeeper_name") shopkeeper_name: String,
    ): CommonResponse


    @POST("ws_delete_shopkeeper.php")
    @FormUrlEncoded
    suspend fun deleteShopkeeper(
        @Field("id") id: String
    ): CommonResponse


}