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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }
        initFavoriteList()
    }

    private fun initFavoriteList() {
        val favoriteList = FavoriteManager.favoriteList
        if (favoriteList.isEmpty()) {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.scrollView3.visibility = View.GONE
        } else {
            binding.tvEmptyCart.visibility = View.GONE
            binding.scrollView3.visibility = View.VISIBLE

            binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.favoriteRecyclerView.adapter = FavoriteAdapter(favoriteList, this, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    updateFavVisibility()
                }
            })
        }
    }

    private fun updateFavVisibility() {
        val favoriteList = FavoriteManager.favoriteList
        if (favoriteList.isEmpty()) {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.scrollView3.visibility = View.GONE
        } else {
            binding.tvEmptyCart.visibility = View.GONE
            binding.scrollView3.visibility = View.VISIBLE
        }
    }
}
