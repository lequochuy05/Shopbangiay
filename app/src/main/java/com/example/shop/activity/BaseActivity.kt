package com.example.shop.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.shop.R
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

        )
    }
    fun getUserName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("UserData", MODE_PRIVATE)
        return sharedPreferences.getString("userName", "Khách") ?: "Khách"
    }
    fun isUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }
    fun showLoginDialog(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Yêu cầu đăng nhập")
            .setMessage("Bạn cần đăng nhập để sử dụng tính năng này. Vui lòng đăng nhập để tiếp tục!")
            .setIcon(R.drawable.ic_warning)
            .setBackground(context.getDrawable(R.drawable.bg_dialog)) // Nền tùy chỉnh
            .setPositiveButton("Đăng nhập") { _, _ ->
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

}
