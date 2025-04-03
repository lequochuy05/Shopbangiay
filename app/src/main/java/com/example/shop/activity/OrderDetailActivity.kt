package com.example.shop.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.OrderItemAdapter
import com.example.shop.databinding.ActivityOrderDetailBinding
import com.example.shop.model.OrderModel

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var orderItemAdapter: OrderItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val order = intent.getSerializableExtra("order") as? OrderModel

        order?.let { displayOrderDetails(it) }

        binding.backBtn.setOnClickListener { finish() }
    }

    private fun displayOrderDetails(order: OrderModel) {
        binding.tvOrderId.text = "Mã đơn: ${order.orderId}"
        binding.tvOrderStatus.text = "Trạng thái: ${order.orderStatus}"
        binding.tvTotalPrice.text = "Tổng tiền: ${order.totalPrice} VND"
        binding.tvOrderDate.text = "Ngày đặt: ${order.orderDate}"
        binding.tvDeliveryAddress.text = "Địa chỉ: ${order.deliveryAddress}"

        // Hiển thị danh sách sản phẩm trong đơn hàng
        orderItemAdapter = OrderItemAdapter()
        orderItemAdapter.submitList(order.items)
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewItems.adapter = orderItemAdapter




    }

}
