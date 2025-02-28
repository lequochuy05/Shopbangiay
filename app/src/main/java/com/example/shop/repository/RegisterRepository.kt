package com.example.shop.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("UserAccount")

    fun registerUser(firstName: String, lastName: String, phone: String, email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                    val uid = auth.currentUser?.uid
                    uid?.let {
                        val userData = hashMapOf(
                            "uId" to it,
                            "uFirstName" to firstName,
                            "uLastName" to lastName,
                            "uPhoneNumber" to phone,
                            "uEmail" to email
                        )
                        database.child(it).setValue(userData)
                    }
                    callback(true, "Vui lòng kiểm tra email để xác thực tài khoản.")
                }?.addOnFailureListener { e ->
                    callback(false, "Lỗi khi gửi email xác thực: ${e.message}")
                }
            } else {
                callback(false, task.exception?.message ?: "Đăng kí thất bại")
            }
        }
    }
}