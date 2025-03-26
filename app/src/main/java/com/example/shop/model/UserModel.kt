package com.example.shop.model

data class UserModel(
    val uId: String ="",
    val uFirstName: String ="",
    val uLastName: String ="",
    val uPhoneNumber: String ="",
    val uEmail: String ="",
    val uAddress: String? = null,
    val dob: String? = null
)
