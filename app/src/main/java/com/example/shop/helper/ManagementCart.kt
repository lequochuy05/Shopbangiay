package com.example.shop.helper

import android.content.Context
import android.widget.Toast
import com.example.shop.model.ItemsModel


class ManagementCart(val context: Context, val userId: String) {

    private val tinyDB = TinyDB(context)
    // Định dạng khóa lưu giỏ hàng theo userId
    private fun getCartKey(): String {
        return "CartList_$userId"
    }

    fun insertItems(item: ItemsModel) {
        val listFood = getListCart()
        val existAlready = listFood.any { it.title == item.title && it.selectedSize == item.selectedSize }
        val index = listFood.indexOfFirst { it.title == item.title && it.selectedSize == item.selectedSize }

        if (existAlready) {
            listFood[index].numberInCart += item.numberInCart
        } else {
            listFood.add(item)
        }
        tinyDB.putListObject(getCartKey(), listFood)
        Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): ArrayList<ItemsModel> {
        return tinyDB.getListObject(getCartKey()) ?: arrayListOf()
    }

    fun clearCart() {
        tinyDB.remove(getCartKey()) // Xóa giỏ hàng chỉ của user hiện tại
    }

    fun minusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (listItems[position].numberInCart == 1) {
            listItems.removeAt(position)
        } else {
            listItems[position].numberInCart--
        }
        tinyDB.putListObject("CartList", listItems)
        listener.onChanged()
    }

    fun plusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        listItems[position].numberInCart++
        tinyDB.putListObject("CartList", listItems)
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listFood = getListCart()
        var fee = 0.0
        for (item in listFood) {
            fee += item.price * item.numberInCart
        }
        return fee
    }

}