package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import com.example.shop.databinding.ActivityIntroBinding
import com.example.shop.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.introBtn.setOnClickListener {
            val loginRepository = LoginRepository()
            loginRepository.getCurrentUser()?.let { FirebaseAuth.getInstance().signOut() }
            val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        binding.signInBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}
