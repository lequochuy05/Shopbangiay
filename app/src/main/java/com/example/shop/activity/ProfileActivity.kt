package com.example.shop.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shop.databinding.ActivityProfileBinding

class ProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener{
            finish()
        }
    }
}