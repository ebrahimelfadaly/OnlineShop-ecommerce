package com.example.onlineshop.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.onlineshop.data.entity.ads_discount_codes.AllCodes
import com.example.onlineshop.data.entity.allproducts.AllProducts
import com.example.onlineshop.data.entity.customProduct.Product
import com.example.onlineshop.data.entity.customProduct.ProductsList
import com.example.onlineshop.data.entity.customer.*
import com.example.onlineshop.data.entity.order.Orders
import com.example.onlineshop.data.entity.orderGet.GetOrders
import com.example.onlineshop.data.entity.orderGet.OneOrderResponce
import com.example.onlineshop.data.entity.priceRules.priceRules
import com.example.onlineshop.data.entity.smart_collection.Brands
import com.example.onlineshop.data.itemPojo.OrderObject
import com.example.onlineshop.data.itemPojo.ProductCartModule
import com.example.onlineshop.data.itemPojo.ProductItem

import com.example.onlineshop.data.remoteDataSource.RemoteDataIN
import com.example.onlineshop.data.remoteDataSource.network.Network

import com.example.onlineshop.data.roomData.RoomDataSourceImpl
import com.example.onlineshop.networkBase.SingleLiveEvent
import io.reactivex.Observable

interface IRepository {
   val remoteDataSource:RemoteDataIN
   val roomDataSourceImpl:RoomDataSourceImpl
   var  prouductDetaild: MutableLiveData<ProductItem>


   ////////////////////cart list////////////////

   fun getAllCartList(): LiveData<List<ProductCartModule>>

   suspend fun saveCartList(withItem: ProductCartModule)

   suspend fun deleteOnCartItem(id: Long)

   suspend fun deleteAllFromCart()

   suspend fun deleteAllFromWish()
   fun saveAllCartList(dataList: List<ProductCartModule>)

   /////////////customer address/////////////////
   suspend fun createCustomerAddress(id: String, customerAddress: CreateAddress): CreateAddressX?

   suspend fun getCustomerAddresses(id: String): List<Addresse>?

   suspend fun updateCustomerAddresses(
      id: String,
      addressID: String,
      customerAddress: UpdateAddresse
   ): CreateAddressX?

   suspend fun delCustomerAddresses(id: String, addressID: String)

   suspend fun setDafaultCustomerAddress(id: String, addressID: String): CreateAddressX?

   suspend fun getCustomerAddress(id: String, addressID: String): CreateAddressX?

   ////////////////////customer///////////////////
   suspend fun fetchCustomersData(): List<Customer>?

   suspend fun createCustomers(customer: CustomerX): CustomerX?

   suspend fun getCustomer(id: String): CustomerX?

   suspend fun updateCustomer(id: String, customer: CustomerProfile): CustomerX?

   /////////////////////create order/////////////////////////////
   fun createOrder(order: Orders)
   fun getCreateOrderResponse(): SingleLiveEvent<OneOrderResponce?>

   ///////////////////products/////////////////////////
   fun getAllBrands():MutableLiveData<Brands>
   fun getWomanProductsList(): MutableLiveData<ProductsList>?
   fun getKidsProductsList(): MutableLiveData<ProductsList>
   fun getMenProductsList(): MutableLiveData<ProductsList>
   fun getOnSaleProductsList(): MutableLiveData<ProductsList>
   fun getAllProductsList(): MutableLiveData<AllProducts>
   fun getAllDiscountCodeList(): MutableLiveData<priceRules>
   fun getProuduct(id: Long)
   fun fetchCatProducts(colID: Long): MutableLiveData<List<Product>>
   fun fetchAllProducts(): MutableLiveData<List<com.example.onlineshop.data.itemPojo.Product>>
   fun getAllOrderList(): LiveData<List<OrderObject>>

   // wish list
   fun getFourFromWishList(): LiveData<List<com.example.onlineshop.data.itemPojo.Product>>
   fun getAllWishList(): LiveData<List<com.example.onlineshop.data.itemPojo.Product>>

   suspend fun saveWishList(withItem: com.example.onlineshop.data.itemPojo.Product)
   suspend fun getProductBrand(vendor: String) = Network.apiService.getProductsByVendor(vendor)

   suspend fun deleteOneWithItem(id: Long)
   fun getFourWishList(): LiveData<List<com.example.onlineshop.data.itemPojo.Product>>

   suspend fun deleteOneWishItem(id: Long)
   fun getOneWithItem(id: Long): LiveData<com.example.onlineshop.data.itemPojo.Product>

   fun getAllOrders(): Observable<GetOrders>
   fun deleteOrder(order_id: Long)
   fun observeDeleteOrder(): MutableLiveData<Boolean>
   fun getOneOrders(id: Long): Observable<OneOrderResponce>
}