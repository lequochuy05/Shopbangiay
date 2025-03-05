package com.example.shop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shop.databinding.ViewholderFavoriteBinding
import com.example.shop.helper.ChangeNumberItemsListener
import com.example.shop.helper.FavoriteManager
import com.example.shop.model.ItemsModel

class FavoriteAdapter(
    private val listItemSelected: ArrayList<ItemsModel>,
    context: Context,
    private var changeNumberItemsListener: ChangeNumberItemsListener? = null
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    class ViewHolder(val binding: ViewholderFavoriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderFavoriteBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItemSelected[position]
        with(holder.binding) {
            titleTxt.text = item.title
            tvSize.text = if (item.selectedSize.isNotEmpty()) "Size: ${item.selectedSize}" else "Size: Not selected"
            feeEachItem.text = "$${item.price}"

            // Hiển thị hình ảnh sản phẩm
            Glide.with(holder.itemView.context)
                .load(item.picUrl.getOrNull(0))
                .into(picCart)

            // Nút xóa khỏi favorites
            btnRemove.setOnClickListener {
                FavoriteManager.removeFavorite(item)
                listItemSelected.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, listItemSelected.size)
                changeNumberItemsListener?.onChanged()
            }
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}
