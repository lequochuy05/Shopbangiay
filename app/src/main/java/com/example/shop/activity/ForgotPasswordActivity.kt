package com.example.shop.activity

import android.os.Bundle
import android.widget.Toast
import com.example.shop.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnResetPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (email.isEmpty()) {
                binding.etEmail.error = "Email không được để trống"
                binding.etEmail.requestFocus()
            } else {
                resetPassword(email)
            }
        }
    }

    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Đã gửi email đặt lại mật khẩu đến $email", Toast.LENGTH_SHORT).show()
                    finish()  // Quay lại màn hình trước sau khi gửi email thành công
                } else {
                    Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
