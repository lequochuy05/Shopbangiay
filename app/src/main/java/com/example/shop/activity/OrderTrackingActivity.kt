package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.OrderAdapter
import com.example.shop.databinding.ActivityOrderTrackingBinding
import com.example.shop.viewModel.OrderTrackingViewModel

class OrderTrackingActivity : BaseActivity() {
    private lateinit var binding: ActivityOrderTrackingBinding
    private val orderViewModel: OrderTrackingViewModel by viewModels()
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lấy userId từ SharedPreferences
        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        val userId = sharedPreferences.getString("uId", "") ?: ""

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        setupRecyclerView()
        observeOrders()

        // Gọi ViewModel để lấy danh sách đơn hàng
        orderViewModel.fetchOrders(userId)
    }
    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(
            onCancelOrder = { orderId ->
                orderViewModel.cancelOrder(orderId)
                Toast.makeText(this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show()
            },
            onOrderClick = { order -> // Khi nhấn vào đơn hàng
                val intent = Intent(this, OrderDetailActivity::class.java)
                intent.putExtra("order", order)
                startActivity(intent)
            }
        )

        binding.recyclerViewOrders.apply {
            layoutManager = LinearLayoutManager(this@OrderTrackingActivity)
            adapter = orderAdapter
        }
    }


    private fun observeOrders() {
        orderViewModel.orders.observe(this) { orderList ->
            if (orderList.isEmpty()) {
                binding.recyclerViewOrders.visibility = View.GONE
                binding.tvEmptyCart.visibility = View.VISIBLE
            } else {
                binding.recyclerViewOrders.visibility = View.VISIBLE
                binding.tvEmptyCart.visibility = View.GONE
                orderAdapter.submitList(orderList)
            }
        }
    }
}