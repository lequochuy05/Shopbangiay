package com.example.shop.helper

import android.content.Context
import android.widget.Toast
import com.example.shop.model.ItemsModel


class ManagementCart(val context: Context) {

    private val tinyDB = TinyDB(context)

    fun insertItems(item: ItemsModel) {
        val listFood = getListCart()
        // Kiểm tra xem đã có sản phẩm nào với cùng title và selectedSize chưa
        val existAlready = listFood.any { it.title == item.title && it.selectedSize == item.selectedSize }
        val index = listFood.indexOfFirst { it.title == item.title && it.selectedSize == item.selectedSize }

        if (existAlready) {
            // Nếu sản phẩm đã tồn tại, tăng số lượng
            listFood[index].numberInCart += item.numberInCart
        } else {
            // Nếu chưa có, thêm sản phẩm mới
            listFood.add(item)
        }
        tinyDB.putListObject("CartList", listFood)
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()
    }


    fun getListCart(): ArrayList<ItemsModel> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
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