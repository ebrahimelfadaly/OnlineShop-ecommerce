package com.example.onlineshop.repository

import android.util.Log
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

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RepositoryImpl(
    override val remoteDataSource: RemoteDataIN,
    override val roomDataSourceImpl: RoomDataSourceImpl


   ):IRepository {
    override var prouductDetaild: MutableLiveData<ProductItem> = MutableLiveData()

    ////////////////////cart list////////////////
    override fun getAllCartList(): LiveData<List<ProductCartModule>> {
        return roomDataSourceImpl.getAllCartList()
    }

    override suspend fun saveCartList(withItem: ProductCartModule) {
        roomDataSourceImpl.saveCartList(withItem)
    }

    override suspend fun deleteOnCartItem(id: Long) {
        roomDataSourceImpl.deleteOnCartItem(id)
    }

    override suspend fun deleteAllFromCart() {
        roomDataSourceImpl.deleteAllFromCart()
    }

    override suspend fun deleteAllFromWish() {
       return roomDataSourceImpl.deleteAllFromWishlist()
    }

    override fun saveAllCartList(dataList: List<ProductCartModule>) {
        roomDataSourceImpl.saveAllCartList(dataList)
    }

    override suspend fun createCustomerAddress(id: String, customerAddress: CreateAddress) =
        remoteDataSource.createCustomerAddress(id, customerAddress)

    override suspend fun getCustomerAddresses(id: String)= remoteDataSource.getCustomerAddresses(id)

    override suspend fun updateCustomerAddresses(
        id: String,
        addressID: String,
        customerAddress: UpdateAddresse
    ) = remoteDataSource.updateCustomerAddresses(id, addressID, customerAddress)

    override suspend fun delCustomerAddresses(id: String, addressID: String) =
        remoteDataSource.delCustomerAddresses(id, addressID)


    override suspend fun setDafaultCustomerAddress(id: String, addressID: String)=
        remoteDataSource.setDafaultCustomerAddress(id, addressID)



    override suspend fun getCustomerAddress(id: String, addressID: String) =
        remoteDataSource.getCustomerAddress(id, addressID)


    override suspend fun fetchCustomersData() = remoteDataSource.fetchCustomersData()

    override suspend fun createCustomers(customer: CustomerX) = remoteDataSource.createCustomers(customer)

    override suspend fun getCustomer(id: String)= remoteDataSource.getCustomer(id)

    override suspend fun updateCustomer(id: String, customer: CustomerProfile)=
        remoteDataSource.updateCustomer(id, customer)


    override fun createOrder(order: Orders)  =
        remoteDataSource.createOrder(order)



    override fun getCreateOrderResponse()= remoteDataSource.getCreateOrderResponse()
    override fun getAllBrands(): MutableLiveData<Brands> {
        return remoteDataSource.getAllBrands()
    }
    ///////////////////products/////////////////////////

    override fun getWomanProductsList(): MutableLiveData<ProductsList> {
       return remoteDataSource.getWomanProductsList()
    }

    override fun getKidsProductsList(): MutableLiveData<ProductsList> {
        return remoteDataSource.getKidsProductsList()
    }

    override fun getMenProductsList(): MutableLiveData<ProductsList> {
        return remoteDataSource.getMenProductsList()
    }

    override fun getOnSaleProductsList(): MutableLiveData<ProductsList> {
        return remoteDataSource.getOnSaleProductsList()
    }

    override fun getAllProductsList(): MutableLiveData<AllProducts> {
        return remoteDataSource.getAllProductsList()
    }

    override fun getAllDiscountCodeList(): MutableLiveData<priceRules> {
       return remoteDataSource.getAllDiscountCodeList()
    }

    override fun getProuduct(id: Long) {
        remoteDataSource.getProuduct(id)
        Network.apiService.getOneProduct(id).enqueue(object : Callback<ProductItem?> {
            override fun onResponse(
                call: Call<ProductItem?>,
                response: Response<ProductItem?>
            ) {
                Log.d("TAG", "data here")
                prouductDetaild.value = response.body()
            }

            override fun onFailure(call: Call<ProductItem?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun fetchCatProducts(colID: Long): MutableLiveData<List<Product>> {
        return remoteDataSource.fetchCatProducts(colID)
    }

    override fun fetchAllProducts(): MutableLiveData<List<com.example.onlineshop.data.itemPojo.Product>> {
       return remoteDataSource.fetchAllProducts()
    }

    override fun getAllOrderList(): LiveData<List<OrderObject>> {
        return roomDataSourceImpl.getAllOrderList()
    }



    override fun getFourFromWishList(): LiveData<List<com.example.onlineshop.data.itemPojo.Product>> {
        return roomDataSourceImpl.getFourFromWishList()
    }

    override fun getAllWishList(): LiveData<List<com.example.onlineshop.data.itemPojo.Product>> {
        return roomDataSourceImpl.getAllWishList()
    }

    override suspend fun saveWishList(withItem: com.example.onlineshop.data.itemPojo.Product) {
        roomDataSourceImpl.saveWishList(withItem)
    }

    override suspend fun deleteOneWithItem(id: Long) {
        roomDataSourceImpl.deleteOneWithItem(id)
    }

    override fun getFourWishList() = roomDataSourceImpl.getFourWishList()

    override suspend fun deleteOneWishItem(id: Long)  = roomDataSourceImpl.deleteOneWithItem(id)

    override fun getOneWithItem(id: Long) = roomDataSourceImpl.getOneWithItem(id)

    override fun getAllOrders(): Observable<GetOrders> {
        return remoteDataSource.getAllOrders()
    }

    override fun deleteOrder(order_id: Long) {
        remoteDataSource.deleteOrder(order_id)
    }

    override fun observeDeleteOrder()= remoteDataSource.observeDeleteOrder()

    override fun getOneOrders(id: Long): Observable<OneOrderResponce> {
        return remoteDataSource.getOneOrders(id)
    }


}