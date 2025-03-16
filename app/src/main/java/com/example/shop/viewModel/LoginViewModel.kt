    package com.example.shop.viewModel

    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import com.example.shop.model.UserModel
    import com.example.shop.repository.LoginRepository

    class LoginViewModel: ViewModel() {
        private val loginRepository = LoginRepository()

        private val _loginStatus = MutableLiveData<Boolean>()
        val loginStatus: LiveData<Boolean> get() = _loginStatus

        private val _loginError = MutableLiveData<String?>()
        val loginError: LiveData<String?> get() = _loginError

        private val _userData = MutableLiveData<UserModel?>()
        val userData: LiveData<UserModel?> get() = _userData


        /**
         * Login with email/password
         */
        fun loginUserWithEmail(email: String, password: String) {
            loginRepository.loginWithEmail(email, password) { task ->
                if (task.isSuccessful) {
                    val user = loginRepository.getCurrentUser()
                    if (user != null && user.isEmailVerified) {
                        fetchUserData(user.uid)
                    } else {
                        _loginError.value = "Vui lòng xác thực email trước khi đăng nhập"
                        _loginStatus.value = false
                    }
                } else {
                    _loginError.value = "Email hoặc mật khẩu không đúng"
                    _loginStatus.value = false
                }
            }
        }

        /**
         * check email exists while login with google
         */
        fun loginWithGoogle(googleIdToken: String) {
            loginRepository.loginWithGoogle(googleIdToken) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = loginRepository.getCurrentUser()
                    firebaseUser?.let { user ->
                        val email = user.email ?: ""

                        loginRepository.checkIfEmailExists(email) { existingUser ->
                            if (existingUser != null) {
                                // Nếu email đã có trong database, đăng nhập với UID tương ứng
                                _userData.value = existingUser
                                _loginStatus.value = true
                            } else {
                                // Nếu email chưa có, tạo tài khoản mới trong database
                                val newUser = UserModel(
                                    uId = user.uid,
                                    uEmail = email,
                                    uFirstName = user.displayName?.split(" ")?.firstOrNull() ?: "",
                                    uLastName = user.displayName?.split(" ")?.lastOrNull() ?: "",
                                    uPhoneNumber = user.phoneNumber ?: ""
                                )
                                loginRepository.registerNewUser(newUser)
                                _userData.value = newUser
                                _loginStatus.value = true
                            }
                        }
                    }
                } else {
                    _loginError.value = "Đăng nhập Google thất bại"
                    _loginStatus.value = false
                }
            }
        }

        /**
         * Get data user
         */
        private fun fetchUserData(firebaseUid: String) {
            loginRepository.getUserData(firebaseUid) { user ->
                if (user != null) {
                    _userData.value = user
                    _loginStatus.value = true
                } else {
                    _loginError.value = "Không tìm thấy thông tin người dùng"
                    _loginStatus.value = false
                }
            }
        }
    }