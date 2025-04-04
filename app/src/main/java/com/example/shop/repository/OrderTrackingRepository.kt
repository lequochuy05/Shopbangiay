
package com.example.shop.repository

import com.example.shop.model.OrderModel
import com.google.firebase.database.*

class OrderTrackingRepository {
    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("orders")

    fun fetchOrders(userId: String, onResult: (List<OrderModel>) -> Unit) {
        databaseRef.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val orders = snapshot.children.mapNotNull { it.getValue(OrderModel::class.java) }
                    onResult(orders)
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(emptyList()) // Trả về danh sách rỗng nếu có lỗi
                }
            })
    }

    fun addOrder(order: OrderModel, onComplete: (Boolean) -> Unit) {
        val orderId = databaseRef.push().key ?: return
        order.orderId = orderId
        databaseRef.child(orderId).setValue(order).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun cancelOrder(orderId: String, onComplete: (Boolean) -> Unit) {
        databaseRef.child(orderId).removeValue().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }
}
