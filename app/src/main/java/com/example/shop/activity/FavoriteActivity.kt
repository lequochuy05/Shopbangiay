package com.example.shop.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.FavoriteAdapter
import com.example.shop.databinding.ActivityFavoriteBinding
import com.example.shop.helper.ChangeNumberItemsListener
import com.example.shop.helper.FavoriteManager

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }

        setupRecyclerView()
        updateFavVisibility()
    }

    private fun setupRecyclerView() {
        val favoriteList = FavoriteManager.getFavorites().toMutableList()
        adapter = FavoriteAdapter(favoriteList, this, object : ChangeNumberItemsListener {
            override fun onChanged() {
                updateFavVisibility()
            }
        })

        binding.favoriteRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = this@FavoriteActivity.adapter
        }
    }

    private fun updateFavVisibility() {
        if (FavoriteManager.getFavorites().isEmpty()) {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.scrollView3.visibility = View.GONE
        } else {
            binding.tvEmptyCart.visibility = View.GONE
            binding.scrollView3.visibility = View.VISIBLE
        }
    }
}
