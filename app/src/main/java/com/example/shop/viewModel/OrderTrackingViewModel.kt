package com.example.shop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.model.OrderModel
import com.example.shop.repository.OrderTrackingRepository

class OrderTrackingViewModel : ViewModel() {
    private val repository = OrderTrackingRepository()

    private val _orders = MutableLiveData<List<OrderModel>>()
    val orders: LiveData<List<OrderModel>> get() = _orders

    /**
     * Lấy danh sách đơn hàng của user
     */
    fun fetchOrders(userId: String) {
        repository.getOrdersByUser(userId) { orderList ->
            _orders.value = orderList
        }
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    fun updateOrderStatus(orderId: String, newStatus: String) {
        repository.updateOrderStatus(orderId, newStatus) { success ->
            if (success) fetchOrders(orderId) // Load lại danh sách sau khi cập nhật
        }
    }

    /**
     * Hủy đơn hàng
     */
    fun cancelOrder(orderId: String) {
        repository.cancelOrder(orderId) { success ->
            if (success) fetchOrders(orderId)
        }
    }
}
