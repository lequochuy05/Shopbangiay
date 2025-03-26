package com.example.shop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ViewholderItemAddressBinding

class AddressAdapter(
    private val addressList: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ViewholderItemAddressBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(address: String) {
            binding.tvAddress.text = address
            binding.root.setOnClickListener { onItemClick(address) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(addressList[position])
    }

    override fun getItemCount() = addressList.size
}
