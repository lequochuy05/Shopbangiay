package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.shop.databinding.ActivityRegisBinding
import com.example.shop.viewModel.RegisterViewModel

class RegisterActivity : ComponentActivity() {
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
                registerViewModel.registerUser(firstName, lastName, phone, email, password)
            }
        }

        registerViewModel.registerStatus.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        registerViewModel.errorMessage.observe(this, Observer { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                binding.btnSignup.isEnabled = true
            }
        })

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun checkAllFields(
        firstName: String, lastName: String, phone: String, email: String, password: String, confirmPassword: String
    ): Boolean {
        if (firstName.isEmpty()) {
            binding.txtFirstName.error = "Please enter first name"
            return false
        }
        if (lastName.isEmpty()) {
            binding.txtLastName.error = "Please enter last name"
            return false
        }
        if (phone.isEmpty()) {
            binding.txtPhoneNumber.error = "Please enter phone number"
            return false
        }
        if (!phone.matches(Regex("^\\d{10,11}$"))) {
            binding.txtPhoneNumber.error = "Invalid phone number"
            return false
        }
        if (email.isEmpty()) {
            binding.txtEmail.error = "Please enter email"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.txtEmail.error = "Invalid email format"
            return false
        }
        if (password.isEmpty()) {
            binding.txtPassword.error = "Please enter password"
            return false
        }
//        if (password.length < 6) {
//            binding.txtPassword.error = "Password must be at least 6 characters"
//            return false
//        }
        if (confirmPassword.isEmpty()) {
            binding.txtConfirmPassword.error = "Please enter confirm password"
            return false
        }
        if (password != confirmPassword) {
            binding.txtPassword.error = "Password does not match"
            binding.txtConfirmPassword.error = "Password does not match"
            return false
        }
        return true
    }
}
