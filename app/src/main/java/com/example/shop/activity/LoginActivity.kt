package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.shop.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("UserAccount")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.txtSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.txtEmail.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email hoặc mật khẩu", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }
    }

    /**
     * Đăng nhập bằng Firebase Authentication và kiểm tra xác thực email
     */
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        fetchUserData(user.uid)
                    } else {
                        Toast.makeText(this, "Vui lòng xác thực email trước khi đăng nhập", Toast.LENGTH_LONG).show()
                        auth.signOut()
                    }
                } else {
                    Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * Truy xuất thông tin người dùng từ Realtime Database dựa trên key là Firebase UID.
     */
    private fun fetchUserData(firebaseUid: String) {
        database.child(firebaseUid).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val uEmail = snapshot.child("uEmail").value?.toString() ?: ""
                    val uFName = snapshot.child("uFirstName").value?.toString() ?: ""
                    val uLName = snapshot.child("uLastName").value?.toString() ?: ""
                    val uPhone = snapshot.child("uPhoneNumber").value?.toString() ?: ""

                    val intent = Intent(this, DashboardActivity::class.java).apply {
                        putExtra("uId", firebaseUid)
                        putExtra("uEmail", uEmail)
                        putExtra("uFirstName", uFName)
                        putExtra("userName", "$uFName $uLName")
                        putExtra("uPhoneNumber", uPhone)

                    }
                    startActivity(intent)
//                    val intent1 = Intent(this@LoginActivity, SettingActivity::class.java).apply{
//                        putExtra("uFirstName", uFName)
//                    }
//                    startActivity(intent1)
                    finish()
                } else {
                    Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
    }
}
