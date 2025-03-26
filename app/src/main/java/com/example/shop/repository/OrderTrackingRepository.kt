package com.example.shop.repository

import com.example.shop.model.OrderModel
import com.google.firebase.database.*

class OrderTrackingRepository {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("orders")

    /**
     * Lưu đơn hàng vào Realtime Database
     */
    fun saveOrder(order: OrderModel, onComplete: (Boolean) -> Unit) {
        val orderId = database.push().key // Tạo ID tự động
        order.orderId = orderId ?: return

        database.child(orderId).setValue(order).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    /**
     * Lấy danh sách đơn hàng theo User ID
     */
    fun getOrdersByUser(userId: String, onResult: (List<OrderModel>) -> Unit) {
        database.orderByChild("uId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = mutableListOf<OrderModel>()
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(OrderModel::class.java)
                    order?.let { orderList.add(it) }
                }
                onResult(orderList)
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(emptyList())
            }
        })
    }

    /**
     * Cập nhật trạng thái đơn hàng (VD: Chờ xác nhận -> Đang giao)
     */
    fun updateOrderStatus(orderId: String, newStatus: String, onComplete: (Boolean) -> Unit) {
        database.child(orderId).child("orderStatus").setValue(newStatus).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    /**
     * Hủy đơn hàng (Xóa khỏi database)
     */
    fun cancelOrder(orderId: String, onComplete: (Boolean) -> Unit) {
        database.child(orderId).removeValue().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }
}
