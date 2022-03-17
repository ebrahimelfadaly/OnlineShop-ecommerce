package com.example.onlineshop.MainActivity

import android.content.IntentFilter
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavAction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.onlineshop.NavGraphDirections
import com.example.onlineshop.networkBase.NetworkChange
import com.example.onlineshop.R
import com.example.onlineshop.ViewModelFactory
import com.example.onlineshop.data.remoteDataSource.RemoteDataSourceImpl
import com.example.onlineshop.data.roomData.RoomDataSourceImpl
import com.example.onlineshop.data.roomData.RoomService
import com.example.onlineshop.databinding.ActivityMainBinding
import com.example.onlineshop.repository.RepositoryImpl
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var networkChange: NetworkChange
    private var navHostFragment: Fragment? = null
    private var navController: NavController? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        navController = navHostFragment?.findNavController()
        if (navController != null) {
            bottomNavigationView.setupWithNavController(navController!!)
        }


        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        networkChange = NetworkChange(this)

        this.registerReceiver(networkChange, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
        val wishListNotificationAdapter = WishListNotificationAdapter(findViewById(R.id.favourite))
        val cartIconAdapter = CartNotificationAdapter(findViewById(R.id.cartView))
        val viewModelFactory = ViewModelFactory(
            RepositoryImpl(
                RemoteDataSourceImpl(),
                RoomDataSourceImpl(RoomService.getInstance(this.application))
            ), this.application
        )
        val viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MainActivityViewModel::class.java]
        viewModel.getAllWishList().observe(this, {
            wishListNotificationAdapter.updateView(it.size)
        })
        wishListNotificationAdapter.favouriteButton.setOnClickListener {

            val action = NavGraphDirections.actionGlobalAllWishListFragment()
            navController?.navigate(action)
        }
        viewModel.getAllCartList().observe(this, {
            cartIconAdapter.updateView(it.size)
        })
        cartIconAdapter.favouriteButton.setOnClickListener {
            val action = NavGraphDirections.actionGlobalCartFragment2()
            navController?.navigate(action)

        }

        searchIcon.setOnClickListener {

            val action = NavGraphDirections.actionGlobalShopSearchFragment()
            navController?.navigate(action)

        }


    }
        override fun onDestroy() {
            super.onDestroy()
            this.unregisterReceiver(networkChange)
        }

}