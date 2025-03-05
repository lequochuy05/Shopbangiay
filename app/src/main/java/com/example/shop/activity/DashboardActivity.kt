package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.BestSellerAdapter
import com.example.shop.adapter.CategoryAdapter
import com.example.shop.databinding.ActivityDashboardBinding
import com.example.shop.viewModel.DashboardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DashboardActivity : BaseActivity() {

    private val viewModel = DashboardViewModel()
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userFirstName = intent.getStringExtra("uFirstName") ?: "Khách"
        binding.tvName.text = userFirstName

        initCategories()
        initBestSeller()
        bottomNavigation()
    }

    private fun bottomNavigation() {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))

        }
        binding.favBtn.setOnClickListener{
            startActivity(Intent(this, FavoriteActivity::class.java))
        }

        binding.settingBtn.setOnClickListener{
            startActivity(Intent(this, SettingActivity::class.java))
        }
    }

    private fun initCategories() {
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.category.observe(this, Observer {
            binding.viewCategory.layoutManager =
                LinearLayoutManager(this@DashboardActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewCategory.adapter = CategoryAdapter(it)
            binding.progressBarCategory.visibility = View.GONE
        })
    }

    private fun initBestSeller() {
        binding.progressBarBestSeller.visibility = View.VISIBLE
        viewModel.bestSeller.observe(this, Observer {
            binding.viewBestSeller.layoutManager =
                LinearLayoutManager(this@DashboardActivity, LinearLayoutManager.VERTICAL, false)
            binding.viewBestSeller.adapter = BestSellerAdapter(it)
            binding.progressBarBestSeller.visibility = View.GONE
        })
    }
}
