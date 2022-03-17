package com.example.onlineshop.data.remoteDataSource.network

import androidx.room.Delete
import com.example.onlineshop.data.entity.ads_discount_codes.AllCodes
import com.example.onlineshop.data.entity.allproducts.AllProducts
import com.example.onlineshop.data.entity.customProduct.ProductsList
import com.example.onlineshop.data.entity.customer.*
import com.example.onlineshop.data.entity.order.Orders
import com.example.onlineshop.data.entity.orderGet.GetOrders
import com.example.onlineshop.data.entity.orderGet.OneOrderResponce
import com.example.onlineshop.data.entity.priceRules.priceRules
import com.example.onlineshop.data.entity.smart_collection.Brands
import com.example.onlineshop.data.itemPojo.Product
import com.example.onlineshop.data.itemPojo.ProductItem
import com.example.onlineshop.data.itemPojo.ProductsBrand.ProductsModel

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface NetworkService {

    //women 272069066799
    //kids 272069099567
    // men 272069034031
    //onsale 272069132335
    @GET("smart_collections.json")
    fun getAllBrands():Call<Brands>
    @GET("collections/272069066799/products.json")
    fun getWomanProductsList(): Call<ProductsList>

    @GET("collections/272069099567/products.json")
    fun getKidsProductsList(): Call<ProductsList>

    @GET("collections/272069034031/products.json")
    fun getMenProductsList(): Call<ProductsList>

    @GET("collections/272069132335/products.json")
    fun getOnSaleProductsList(): Call<ProductsList>

    @GET("collections/{collection_id}/products.json")
    suspend fun getProducts(@Path("collection_id") collection_id:Long): Response<ProductsList>

    @GET("products.json")
    suspend fun getAllProducts(): Response<List<Product>>

    @GET("products.json")
    fun getAllProductsList(): Call<AllProducts>

    @GET("products.json")
    suspend fun getProductsByVendor(@Query("vendor") vendor: String): Response<ProductsModel>


  //get number
  @GET("price_rules.json")
    fun getAllDiscountCodeList(): Call<priceRules>


    @GET("price_rules.json")
    suspend fun getPriceRulesList(): Response<priceRules>


    @GET("customers.json?limit=250")
    suspend fun getCustomers(): Response<Customers>

    @POST("customers.json")
    suspend fun createCustomer(@Body customer: CustomerX): Response<CustomerX>?

    @POST("customers/{customer_id}/addresses.json")
    suspend fun createCustomerAddress(
        @Path("customer_id") id: String,
        @Body customerAddress: CreateAddress
    ): Response<CreateAddressX>

    @GET("customers/{customer_id}/addresses.json")
    suspend fun getCustomerAddresses(@Path("customer_id") id: String): Response<customerAddresses>

    @GET("customers/{customer_id}.json")
    suspend fun getCustomer(@Path("customer_id") id: String): Response<CustomerX>

    @PUT(" customers/{customer_id}.json")
    suspend fun updateCustomer(
        @Path("customer_id") id: String,
        @Body customer: CustomerProfile
    ): Response<CustomerX>

    @PUT(" customers/{customer_id}/addresses/{address_id}.json")
    suspend fun updateCustomerAddresses(
        @Path("customer_id") id: String,
        @Path("address_id") addressID: String,
        @Body customerAddress: UpdateAddresse
    ): Response<CreateAddressX>
    @GET("customers/{customer_id}/addresses/{address_id}.json")
    suspend fun getCustomerAddress(
        @Path("customer_id") id: String,
        @Path("address_id") addressID: String
    ): Response<CreateAddressX>

    @DELETE(" customers/{customer_id}/addresses/{address_id}.json")
    suspend fun delCustomerAddresses(
        @Path("customer_id") id: String,
        @Path("address_id") addressID: String
    ): Response<ResponseBody>

    @PUT("customers/{customer_id}/addresses/{address_id}/default.json")
    suspend fun setDafaultCustomerAddress(
        @Path("customer_id") id: String,
        @Path("address_id") addressID: String
    ): Response<CreateAddressX>

    @POST("orders.json")
    suspend fun createOrder(
        @Body order: Orders
    ): Response<OneOrderResponce>




    @GET("products/{id}.json")
    fun getOneProduct(@Path("id") id: Long) : Call<ProductItem>


    @GET("orders.json")
    fun getAllOrders() : Observable<GetOrders>

    @GET("orders/{id}.json")
    fun getOneOrders(@Path("id") id: Long) : Observable<OneOrderResponce>

    @DELETE("orders/{id}.json")
   fun deleteOrder(@Path("id")order_id : Long) : Call<Delete>

}