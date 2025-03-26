package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.SettingAdapter
import com.example.shop.databinding.ActivitySettingBinding
import com.example.shop.model.SettingModel
import com.example.shop.viewModel.SettingViewModel

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var settingAdapter: SettingAdapter

    private lateinit var settings: SettingModel

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingAdapter = SettingAdapter(
            emptyList(),
            onItemClick = { position -> settingViewModel.onSettingClicked(position) },
            onSwitchToggle = { position, isChecked -> settingViewModel.onSwitchToggle(position, isChecked) }
        )

        binding.rvSettings.apply {
            layoutManager = LinearLayoutManager(this@SettingActivity)
            adapter = settingAdapter
        }

        binding.tvEditPersonalInfo.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.backBtn.setOnClickListener {
            finish()
        }


        observeViewModel()
    }

    private fun observeViewModel() {
        settingViewModel.settings.observe(this) { settings ->
            settingAdapter.updateSettings(settings)
        }

        settingViewModel.logoutEvent.observe(this) { shouldLogout ->
            if (shouldLogout) {
                Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        settingViewModel.notificationState.observe(this) { isEnabled ->
            Toast.makeText(this, "Thông báo: ${if (isEnabled) "Bật" else "Tắt"}", Toast.LENGTH_SHORT).show()
        }

        settingViewModel.navigateToOrderTracking.observe(this) { shouldNavigate ->
            if (shouldNavigate) {
                val intent = Intent(this, OrderTrackingActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
