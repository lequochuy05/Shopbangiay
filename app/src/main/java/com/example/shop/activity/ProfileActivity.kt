package com.example.shop.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shop.databinding.ActivityProfileBinding
import com.example.shop.repository.ProfileRepository
import com.example.shop.viewModel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var selectedBitmap: Bitmap? = null

    // Khởi tạo ViewModel với Repository
    private val viewModel: ProfileViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(ProfileRepository()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadUserData()

        binding.backBtn.setOnClickListener { finish() }
        binding.tvChangePhoto.setOnClickListener { selectImage() }
        binding.btnSavePhoto.setOnClickListener { selectedBitmap?.let { viewModel.uploadProfileImage(it) } }
        binding.etBirthDate.setOnClickListener { showDatePickerDialog() }
        binding.btnSaveInfo.setOnClickListener { saveUserInfo() }
        checkLoginMethod()
        binding.btnSavePassword.setOnClickListener { changePassword() }

        observeViewModel()
    }

    /**
     * Cập nhật màn hình hiển thị thông tin
     */
    private fun observeViewModel() {
        viewModel.userData.observe(this, Observer { user ->
            binding.etFirstName.setText(user.firstName)
            binding.etLastName.setText(user.lastName)
            binding.etPhoneNumber.setText(user.phoneNumber)
            binding.etAddress.setText(user.address?.firstOrNull() ?: "")
            binding.etBirthDate.setText(user.dob ?: "")
            user.img?.let { binding.imgProfile.setImageBitmap(viewModel.decodeBase64Image(it)) }
        })

        viewModel.updateSuccess.observe(this, Observer { success ->
            if (success) Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show()
        })
    }

    /**
     * Lưu thông tin người dùng
     */
    private fun saveUserInfo() {
        val userUpdates = mapOf(
            "firstName" to binding.etFirstName.text.toString(),
            "lastName" to binding.etLastName.text.toString(),
            "phoneNumber" to binding.etPhoneNumber.text.toString(),
            "address" to listOf(binding.etAddress.text.toString()),
            "dob" to binding.etBirthDate.text.toString()
        )
        viewModel.saveUserInfo(userUpdates)
    }
    /**
     * Người dùng chọn ảnh để hiển thị
     */
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    /**
     * set ảnh người dùng đã chọn
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            val inputStream = contentResolver.openInputStream(imageUri!!)
            selectedBitmap = BitmapFactory.decodeStream(inputStream)
            binding.imgProfile.setImageBitmap(selectedBitmap)
        }
    }

    /**
     * Show lịch để người dùng chọn
     */
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                binding.etBirthDate.setText(String.format("%02d/%02d/%d", day, month + 1, year))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    /**
     * Cập nhật mật khẩu
     */
    private fun changePassword(){
        val oldPassword = binding.etOldPassword.text.toString()
        val newPassword = binding.etNewPassword.text.toString()

        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.changePassword(oldPassword, newPassword) { success, message ->
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                if (success) {
                    binding.etOldPassword.text.clear()
                    binding.etNewPassword.text.clear()
                }
            }
        }
    }
    /**
     * Check tài khoản đăng nhập
     */
    private fun checkLoginMethod() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            var isGoogleUser = false
            var hasEmailProvider = false

            for (profile in it.providerData) {
                if (profile.providerId == "google.com") {
                    isGoogleUser = true
                }
                if (profile.providerId == "password") {
                    hasEmailProvider = true
                }
            }

            // Nếu tài khoản chỉ đăng nhập bằng Google, vô hiệu hóa đổi mật khẩu
            if (isGoogleUser && !hasEmailProvider) {
                binding.etOldPassword.isEnabled = false
                binding.etNewPassword.isEnabled = false
                binding.btnSavePassword.isEnabled = false
                Toast.makeText(this, "Bạn đã đăng nhập bằng Google, vui lòng đặt lại mật khẩu qua email!", Toast.LENGTH_LONG).show()
            }
        }
    }

}
