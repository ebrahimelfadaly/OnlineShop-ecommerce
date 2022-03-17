package com.example.onlineshop

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.onlineshop.MainActivity.MainActivityViewModel
import com.example.onlineshop.repository.IRepository
import com.example.onlineshop.ui.AllWishList.AllWishListViewModel
import com.example.onlineshop.ui.Payment.PaymentViewModel
import com.example.onlineshop.ui.ProductDetalis.ProductDetailsVM
import com.example.onlineshop.ui.ShopTap.ShopViewModel
import com.example.onlineshop.ui.ShowOneOrderDetails.ShowOneOrderDetailsVM
import com.example.onlineshop.ui.address.AddressViewModel
import com.example.onlineshop.ui.cart.OrderViewModel
import com.example.onlineshop.ui.category.CategoryViewModel
import com.example.onlineshop.ui.displayOrder.DisplayOrderViewModel
import com.example.onlineshop.ui.login_register.ui.login.LoginViewModel
import com.example.onlineshop.ui.meScreen.MeViewModel
import com.example.onlineshop.ui.profile.ProfileViewModel
import com.example.onlineshop.ui.settings.SettingViewModel


class ViewModelFactory(private val repositoryImpl: IRepository,private val application: Application):ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ShopViewModel::class.java) -> {
                ShopViewModel(repositoryImpl, application) as T
            }
            modelClass.isAssignableFrom(AllWishListViewModel::class.java) -> {
                AllWishListViewModel(repositoryImpl, application) as T
            }
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> {
                MainActivityViewModel(repositoryImpl, application) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repositoryImpl, application) as T
            }
            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> {
                CategoryViewModel(repositoryImpl, application) as T

            }
            modelClass.isAssignableFrom(MeViewModel::class.java)-> {
               MeViewModel(repositoryImpl,application) as T
            }

            modelClass.isAssignableFrom(SettingViewModel::class.java)-> {
                SettingViewModel(repositoryImpl, application) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java)-> {
                ProfileViewModel(repositoryImpl, application) as T
            }
            modelClass.isAssignableFrom(ProductDetailsVM::class.java)-> {
                ProductDetailsVM(repositoryImpl, application) as T
            }
            modelClass.isAssignableFrom(OrderViewModel::class.java)-> {
                OrderViewModel(repositoryImpl, application) as T
            }
            modelClass.isAssignableFrom(ShowOneOrderDetailsVM::class.java)-> {
                ShowOneOrderDetailsVM(repositoryImpl, application) as T

            }
            modelClass.isAssignableFrom( PaymentViewModel::class.java)-> {
               PaymentViewModel(repositoryImpl,application) as T
            }
            modelClass.isAssignableFrom(AddressViewModel::class.java)-> {
                AddressViewModel(repositoryImpl,application) as T
            }
            modelClass.isAssignableFrom(DisplayOrderViewModel::class.java)-> {
                DisplayOrderViewModel(repositoryImpl, application) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
      }
    }



