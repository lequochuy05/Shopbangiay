package com.example.shop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ViewholderItemOrderBinding
import com.example.shop.model.OrderModel

class OrderAdapter(
    private val onCancelOrder: (String) -> Unit,
    private val onUpdateStatus: (String, String) -> Unit
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    private var orderList: List<OrderModel> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<OrderModel>) {
        orderList = list
        notifyDataSetChanged()
    }

    /**
     * ViewHolder sử dụng View Binding
     */
    class ViewHolder(val binding: ViewholderItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderModel, onCancel: (String) -> Unit, onUpdate: (String, String) -> Unit) {
            with(binding) {
                textOrderId.text = "Mã đơn: ${order.orderId}"
                textOrderStatus.text = "Trạng thái: ${order.orderStatus}"
                textTotalPrice.text = "Tổng tiền: ${order.totalPrice} đ"

                buttonCancelOrder.setOnClickListener { onCancel(order.orderId) }

                buttonUpdateStatus.setOnClickListener {
                    val newStatus = when (order.orderStatus) {
                        "Chờ xác nhận" -> "Đang giao"
                        "Đang giao" -> "Hoàn thành"
                        else -> order.orderStatus
                    }
                    onUpdate(order.orderId, newStatus)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderList[position], onCancelOrder, onUpdateStatus)
    }

    override fun getItemCount(): Int = orderList.size
}
