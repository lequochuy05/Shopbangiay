package com.example.shop.model

data class OrderModel(
    var orderId: String = "",
    var userId: String = "",
    var items: List<ItemsModel> = emptyList(),
    var totalPrice: Double = 0.0,
    var orderStatus: String = "Chờ xác nhận",
    var orderDate: String = "",
    var deliveryAddress: String = ""
)