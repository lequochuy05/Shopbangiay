package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.shop.databinding.ActivityRegisBinding
import com.example.shop.viewModel.RegisterViewModel

class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            val firstName = binding.txtFirstName.text.toString()
            val lastName = binding.txtLastName.text.toString()
            val phone = binding.txtPhoneNumber.text.toString()
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            val confirmPassword = binding.txtConfirmPassword.text.toString()

            if (checkAllFields(firstName, lastName, phone, email, password, confirmPassword)) {
                registerViewModel.registerUser(firstName, lastName, phone, email, password, listOf(), null, null)
            }
        }

        registerViewModel.registerStatus.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Đăng kí thành công", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        registerViewModel.errorMessage.observe(this, Observer { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun checkAllFields(firstName: String, lastName: String, phone: String, email: String, password: String, confirmPassword: String): Boolean {
        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Tất cả các trường đều phải được điền", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!phone.matches(Regex("^\\d{10,11}$"))) {
            binding.txtPhoneNumber.error = "Số điện thoại không hợp lệ"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.txtEmail.error = "Định dạng email không hợp lệ"
            return false
        }
        if (password.length < 6) {
            binding.txtPassword.error = "Mật khẩu phải có ít nhất 6 ký tự"
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
