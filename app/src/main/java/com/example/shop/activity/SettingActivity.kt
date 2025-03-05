package com.example.shop.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.R
import com.example.shop.adapter.SettingAdapter
import com.example.shop.databinding.ActivitySettingBinding
import com.example.shop.model.SettingModel

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var settingAdapter: SettingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userFirstName = intent.getStringExtra("uFirstName") ?: "Khách"
        binding.tvUsername.text = userFirstName

        val settingList = listOf(

            SettingModel(R.drawable.search_icon, "Theo dõi đơn hàng"),
            SettingModel(R.drawable.ic_address, "Thanh toán và địa chỉ"),
            SettingModel(R.drawable.ic_thongbao, "Thông báo", hasSwitch = true),
            SettingModel(R.drawable.ic_world, "Ngôn ngữ"),
            SettingModel(R.drawable.ic_logout, "Đăng xuất")
        )

        settingAdapter = SettingAdapter(
            settingList,
            onItemClick = { position ->
                handleItemClick(position, settingList)
            },
            onSwitchToggle = { position, isChecked ->
                handleSwitchToggle(position, isChecked, settingList)
            }
        )

        binding.rvSettings.apply {
            layoutManager = LinearLayoutManager(this@SettingActivity)
            adapter = settingAdapter
        }
        binding.tvEditPersonalInfo.setOnClickListener {
            Toast.makeText(this, "Edit personal info clicked", Toast.LENGTH_SHORT).show()

        }
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun handleItemClick(position: Int, settingList: List<SettingModel>) {
        val item = settingList[position]
        when (item.title) {
            "Theo dõi đơn hàng" -> {
                Toast.makeText(this, "Track Order clicked", Toast.LENGTH_SHORT).show()
                // ...
            }
            "Thanh toán và địa chỉ" -> {
                Toast.makeText(this, "Billing & Addresses clicked", Toast.LENGTH_SHORT).show()
                // ...
            }

            "Ngôn ngữ" -> {
                Toast.makeText(this, "Language clicked", Toast.LENGTH_SHORT).show()
                // ...
            }
            "Đăng xuất" -> {
                Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show()
                // Xử lý logout
            }
            else -> {}
        }
    }

    private fun handleSwitchToggle(position: Int, isChecked: Boolean, settingList: List<SettingModel>) {
        val item = settingList[position]
        if (item.title == "Thông báo") {
            Toast.makeText(this, "Notifications switched: $isChecked", Toast.LENGTH_SHORT).show()
            // Xử lý tắt/bật notifications
        }
    }
}
