package com.example.shop.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.OrderAdapter
import com.example.shop.databinding.ActivityOrderTrackingBinding
import com.example.shop.model.OrderModel
import com.example.shop.viewModel.OrderTrackingViewModel

class OrderTrackingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderTrackingBinding
    private val orderViewModel: OrderTrackingViewModel by viewModels()
    private lateinit var orderAdapter: OrderAdapter
    private var userId: String = "user_001"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener{
            finish()
        }

        setupRecyclerView()
        observeOrders()

        // Load danh sách đơn hàng của user
        orderViewModel.fetchOrders(userId)
    }

    /**
     * Thiết lập RecyclerView
     */
    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(
            onCancelOrder = { orderId ->
                orderViewModel.cancelOrder(orderId)
                Toast.makeText(this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show()
            },
            onUpdateStatus = { orderId, newStatus ->
                orderViewModel.updateOrderStatus(orderId, newStatus)
                Toast.makeText(this, "Cập nhật trạng thái đơn hàng", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerViewOrders.apply {
            layoutManager = LinearLayoutManager(this@OrderTrackingActivity)
            adapter = orderAdapter
        }
    }

    /**
     * Lắng nghe dữ liệu từ ViewModel
     */
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
