package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.example.shop.R
import com.example.shop.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.introButton.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        binding.introSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}
