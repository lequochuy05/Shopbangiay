package com.example.shop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ViewholderItemOrderBinding
import com.example.shop.model.OrderModel

class OrderAdapter(
    private val onCancelOrder: (String) -> Unit,
    private val onOrderClick: (OrderModel) -> Unit  // Thêm sự kiện click vào đơn hàng
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var orderList: List<OrderModel> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<OrderModel>) {
        orderList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ViewholderItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orderList.size

    inner class OrderViewHolder(private val binding: ViewholderItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderModel) {
            binding.tvOrderId.text = "Mã đơn: ${order.orderId}"
            binding.tvOrderStatus.text = "Trạng thái: ${order.orderStatus}"
            binding.tvTotalPrice.text = "Tổng tiền: ${order.totalPrice} VND"

            binding.btnCancelOrder.setOnClickListener {
                onCancelOrder(order.orderId)
            }

            binding.root.setOnClickListener {
                onOrderClick(order)
            }
        }
    }
}