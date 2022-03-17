package com.example.onlineshop.ui.entry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.example.onlineshop.MainActivity.MainActivity
import com.example.onlineshop.R
import com.example.onlineshop.databinding.ActivitySplashBinding

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity(){
    lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {

            var topAnimation = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.top_animation)
            AnimationUtils.loadAnimation(this@SplashActivity, R.anim.button_animation)

            binding.imageView.animation= topAnimation


            delay(3000)
            var intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }}