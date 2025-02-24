package com.example.shop.model

data class UserModel(
    val userId: Int = 0,
    val userFirstName: String,
    val userLastName: String,
    val userPhoneNumber: String,
    val userEmail: String
)
