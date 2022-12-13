package com.example.marvel.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.marvel.R
import com.example.marvel.databinding.ActivitySplashBinding
import com.example.marvel.ui.fragment.details.MainActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSplash()
        supportActionBar?.hide()
    }

    private fun setupSplash()= with(binding) {
        tvSplash.alpha = 0f
        tvSplash.animate().setDuration(1000).alpha(1f).withEndAction{
            val intent  = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}