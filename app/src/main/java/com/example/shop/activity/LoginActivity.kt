package com.example.shop.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.shop.databinding.ActivityLoginBinding
import com.example.shop.viewModel.LoginViewModel
import com.google.android.gms.auth.api.signin.*
import com.example.shop.R

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            setupUI()
            setupGoogleSignIn()
            googleSignInClient.signOut()
            observeViewModel()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khởi tạo: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI() {
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.txtSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            try {
                val email = binding.txtEmail.text.toString().trim()
                val password = binding.txtPassword.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập email hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                } else {
                    loginViewModel.loginUserWithEmail(email, password)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Lỗi đăng nhập: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.googleBtn.setOnClickListener {
            try {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Lỗi Google Sign-In", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupGoogleSignIn() {
        try {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    loginViewModel.loginWithGoogle(account.idToken!!)
                } else {
                    Toast.makeText(this, "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi xử lý Google Sign-In", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        loginViewModel.loginStatus.observe(this) { success ->
            if (success) {
                loginViewModel.userData.value?.let { user ->
                    saveUserData(user.id, user.email, "${user.firstName} ${user.lastName}")
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }
            }
        }

        loginViewModel.loginError.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(id: String, email: String, name: String) {
        try {
            val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
            sharedPreferences.edit().apply {
                putString("uId", id)
                putString("uEmail", email)
                putString("userName", name)
                apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Không thể lưu thông tin người dùng", Toast.LENGTH_SHORT).show()
        }
    }
}
