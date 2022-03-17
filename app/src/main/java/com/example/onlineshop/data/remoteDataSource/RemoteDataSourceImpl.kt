package com.example.onlineshop.data.remoteDataSource

import android.util.Log
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
import com.example.onlineshop.data.itemPojo.ProductItem

import com.example.onlineshop.data.remoteDataSource.network.Network
import com.example.onlineshop.networkBase.SingleLiveEvent
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSourceImpl :RemoteDataIN {

    var womanProducts = MutableLiveData<ProductsList>()
    var kidsProducts = MutableLiveData<ProductsList>()
    var menProducts = MutableLiveData<ProductsList>()
    var onSaleProducts = MutableLiveData<ProductsList>()
    var allProductsListt = MutableLiveData<AllProducts>()
    var allBrand=  MutableLiveData<Brands>()


    var allDiscountCode = MutableLiveData<priceRules>()
    var prouductDetaild : MutableLiveData<ProductItem> = MutableLiveData()
    var deleteOrder : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var getCreateOrderResponse = SingleLiveEvent<OneOrderResponce?>()
    var catProducts = MutableLiveData<List<Product>>()
    var getProductBrand=MutableLiveData<ProductItem>()
    var allProducts = MutableLiveData<List<com.example.onlineshop.data.itemPojo.Product>>()





    override fun getWomanProductsList(): MutableLiveData<ProductsList> {
        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getWomanProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(
                    call: Call<ProductsList?>,
                    response: Response<ProductsList?>
                ) {
                    if (response.isSuccessful) {
                        womanProducts.postValue(response.body())
                        // Log.i("output", response.body().toString())

                    }
                }

                override fun onFailure(call: Call<ProductsList?>, t: Throwable) {
                    Log.i("output", t.message.toString())
                    t.printStackTrace()

                }
            })
        }
        return womanProducts

    }

    override fun getKidsProductsList(): MutableLiveData<ProductsList> {
        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getKidsProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(
                    call: Call<ProductsList?>,
                    response: Response<ProductsList?>
                ) {
                    if (response.isSuccessful) {
                        kidsProducts.postValue(response.body())
                         Log.i("output", response.body().toString())

                    }
                }

                override fun onFailure(call: Call<ProductsList?>, t: Throwable) {
                    Log.i("output", t.message.toString())
                    t.printStackTrace()

                }
            })
        }
        return kidsProducts
    }

    override fun getMenProductsList(): MutableLiveData<ProductsList> {
        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getMenProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(
                    call: Call<ProductsList?>,
                    response: Response<ProductsList?>
                ) {
                    if (response.isSuccessful) {
                        menProducts.postValue(response.body())
                        // Log.i("output", response.body().toString())

                    }
                }

                override fun onFailure(call: Call<ProductsList?>, t: Throwable) {
                    Log.i("output", t.message.toString())
                    t.printStackTrace()

                }
            })
        }
        return menProducts
    }

    override fun getOnSaleProductsList(): MutableLiveData<ProductsList> {
        CoroutineScope(Dispatchers.IO).launch {
         Network.apiService.getOnSaleProductsList().enqueue(object :Callback<ProductsList?>{
             override fun onResponse(call: Call<ProductsList?>, response: Response<ProductsList?>) {
              if (response.isSuccessful)
               {
               onSaleProducts.postValue(response.body())
               }
             }

             override fun onFailure(call: Call<ProductsList?>, t: Throwable) {
                 Log.i("output", t.message.toString())
                 t.printStackTrace()
             }
         })
        }
        return onSaleProducts

    }

    override fun getAllProductsList(): MutableLiveData<AllProducts> {
        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getAllProductsList().enqueue(object : Callback<AllProducts?> {

                override fun onResponse(
                    call: Call<AllProducts?>,
                    response: Response<AllProducts?>
                ) {
                    if (response.isSuccessful) {
                        allProductsListt.postValue(response.body())

                    }
                }

                override fun onFailure(call: Call<AllProducts?>, t: Throwable) {
                    t.printStackTrace()


                }
            })
        }
        return allProductsListt
    }

    override fun getAllDiscountCodeList(): MutableLiveData<priceRules> {
        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getAllDiscountCodeList().enqueue(object : Callback<priceRules?> {

                override fun onResponse(call: Call<priceRules?>, response: Response<priceRules?>) {
                    if (response.isSuccessful) {
                        allDiscountCode.postValue(response.body())

                    }
                }

                override fun onFailure(call: Call<priceRules?>, t: Throwable) {
                    t.printStackTrace()


                }
            })
        }
        return allDiscountCode
    }

    override fun getAllBrands(): MutableLiveData<Brands> {
        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getAllBrands().enqueue(object : Callback<Brands?> {

                override fun onResponse(
                    call: Call<Brands?>,
                    response: Response<Brands?>
                ) {
                    if (response.isSuccessful) {
                        allBrand.postValue(response.body())
                        Log.i("output", response.body().toString())

                    }
                }

                override fun onFailure(call: Call<Brands?>, t: Throwable) {
                    Log.i("output", t.message.toString())
                    t.printStackTrace()

                }
            })
        }
        return allBrand
    }

    override fun getProuduct(id: Long) {
        Network.apiService.getOneProduct(id).enqueue(object : Callback<ProductItem?> {
            override fun onResponse(call: Call<ProductItem?>, response: Response<ProductItem?>) {
                Log.d("TAG","data here")
                prouductDetaild.value = response.body()
            }
            override fun onFailure(call: Call<ProductItem?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun fetchCatProducts(colID: Long): MutableLiveData<List<Product>> {
        var categoryRetrofitApi = Network.apiService
        CoroutineScope(Dispatchers.IO).launch {
            val response = categoryRetrofitApi.getProducts(colID)
            if (response.isSuccessful) {
                catProducts.postValue(response.body()!!.products)
            } else {
                //error code
            }
        }
        return catProducts
    }

    override fun fetchAllProducts(): MutableLiveData<List<com.example.onlineshop.data.itemPojo.Product>> {
        var categoryRetrofitApi = Network.apiService
        CoroutineScope(Dispatchers.IO).launch {

            val response=categoryRetrofitApi.getAllProducts()
            if (response.isSuccessful){

                allProducts.postValue(response.body()!!)

            }
        }
        return allProducts
    }

    override fun getAllOrders(): Observable<GetOrders> {
        return Network.apiService.getAllOrders()
    }

    override fun deleteOrder(order_id: Long) {
//        CoroutineScope(Dispatchers.IO).launch {
//            Network.apiService.deleteOrder(order_id).enqueue(object : Callback<Delete?> {
//                override fun onResponse(call: Call<Delete?>, response: Response<Delete?>) {
////                    if (response.isSuccessful) {
//                    deleteOrder.postValue(true)
////                    }
//                }
//
//                override fun onFailure(call: Call<Delete?>, t: Throwable) {
//                }
//            })
//        }
    }

    override fun observeDeleteOrder(): MutableLiveData<Boolean> {
        return deleteOrder
    }

    override fun getOneOrders(id: Long): Observable<OneOrderResponce> {
        return Network.apiService.getOneOrders(id)
    }



    override suspend fun fetchCustomersData(): List<Customer>? {
        val response = Network.apiService.getCustomers()
        try {
            if (response.isSuccessful) {

                return response.body()?.customers
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null
    }

    override suspend fun createCustomers(customer: CustomerX): CustomerX? {
        val response = Network.apiService.createCustomer(customer)
        try {
            response?.let {
                if (response.isSuccessful) {
                    return response.body()
                } else {

                    Log.i("ebrahim", "createCustomers:,${response.body().toString()} ")

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun createCustomerAddress(
        id: String,
        customerAddress: CreateAddress
    ): CreateAddressX? {
        val response = Network.apiService.createCustomerAddress(id, customerAddress)
        try {
            if (response.isSuccessful) {
                return response.body()
            }
            else{
                Log.i("ebrahim", "createCustomers:,${response.body().toString()} ")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getCustomerAddresses(id: String): List<Addresse>? {
        val response = Network.apiService.getCustomerAddresses(id)
        try {
            if (response.isSuccessful) {
                return response.body()?.addresses
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null

    }

    override suspend fun updateCustomerAddresses(
        id: String,
        addressID: String,
        customerAddress: UpdateAddresse
    ): CreateAddressX? {
        val response =
            Network.apiService.updateCustomerAddresses(id, addressID, customerAddress)
        try {
            if (response.isSuccessful) {
                return response.body()
            } else {
                Log.i("ebrahim", "updateCustomerAddresses,${response.body().toString()} ")

            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null
    }

    override suspend fun getCustomerAddress(id: String, addressID: String): CreateAddressX? {
        val response = Network.apiService.getCustomerAddress(id,addressID)
        try {
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null
    }

    override suspend fun delCustomerAddresses(id: String, addressID: String) {
        val response = Network.apiService.delCustomerAddresses(id, addressID)
        try {
            if (response.isSuccessful) {
                Log.i("TAG", "delCustomerAddresses: is success")

            } else {
                Log.i("TAG", "delCustomerAddresses: is not success")
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    override suspend fun setDafaultCustomerAddress(id: String, addressID: String): CreateAddressX? {
        val response = Network.apiService.setDafaultCustomerAddress(id, addressID)
        try {
            if (response.isSuccessful) {

                return response.body()
            } else {
                Log.i("ebrahim", "setDafaultCustomerAddress:,${response.body().toString()} ")

            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null
    }

    override suspend fun getCustomer(id: String): CustomerX? {
        val response = Network.apiService.getCustomer(id)
        try {
            if (response.isSuccessful) {

                return response.body()
            } else {
                Log.i("ebrahim", "getCustomer:,${response.body().toString()} ")
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null
    }

    override suspend fun updateCustomer(id: String, customer: CustomerProfile): CustomerX? {
        val response = Network.apiService.updateCustomer(id,customer)
        try {
            if (response.isSuccessful) {

                return response.body()
            } else {
                Log.i("ebrahim", "updateCustomer:,${response.body().toString()} ")

            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null
    }

    override fun createOrder(order: Orders) {
        CoroutineScope(Dispatchers.IO).launch {

            val response = Network.apiService.createOrder(order)
            try {
                if (response.isSuccessful) {

                    getCreateOrderResponse.postValue(response.body())
                    // return response.body()
                } else {
                    getCreateOrderResponse.postValue(null)

                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
            // return null
        }
    }

    override fun getCreateOrderResponse()=getCreateOrderResponse


}