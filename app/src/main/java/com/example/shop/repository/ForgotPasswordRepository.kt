package com.example.shop.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class ForgotPasswordRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun sendResetEmail(email: String): Result<String> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success("Đã gửi email đặt lại mật khẩu đến $email")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
