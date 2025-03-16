package com.example.shop.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.shop.databinding.ActivityLoginBinding
import com.example.shop.viewModel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.example.shop.R

@Suppress("DEPRECATION")
class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var database: DatabaseReference



    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Event clicked forgot password
         */
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        /**
         * Event clicked Sign up
         */
        binding.txtSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        /**
         * Login with email/password
         */
        binding.btnLogin.setOnClickListener {
            val email = binding.txtEmail.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email hoặc mật khẩu", Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.loginUserWithEmail(email, password)
            }
        }

        /**
         * Set up google sign-in
         */
        setupGoogleSignIn()

        /**
         * Remove Google login version to show account selection when logging back in
         */
        googleSignInClient.signOut()
        /**
         * Event google button
         */
        binding.googleBtn.setOnClickListener {
            val signIn = googleSignInClient.signInIntent
            launcher.launch(signIn)
        }

        /**
         * Event facebook button
         */



        observeViewModel()
    }

    private fun setupGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount = task.result
                loginViewModel.loginWithGoogle(account.idToken!!)
            } else {
                Toast.makeText(this, "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /**
     * function get session data from login update dashboard (name, ...)
     */
    private fun observeViewModel() {
        loginViewModel.loginStatus.observe(this) { success ->
            if (success) {
                loginViewModel.userData.value?.let { user ->
                    val intent = Intent(this, DashboardActivity::class.java).apply {

                        putExtra("uId", user.uId)
                        putExtra("uEmail", user.uEmail)
                        putExtra("userName", "${user.uFirstName} ${user.uLastName}")
                        putExtra("uPhoneNumber", user.uPhoneNumber)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }
        loginViewModel.loginError.observe(this) { error ->
            error?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        loginViewModel.loginError.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

    }
}