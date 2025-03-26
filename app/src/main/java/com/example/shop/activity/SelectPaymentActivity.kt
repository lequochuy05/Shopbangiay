package com.example.shop.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shop.databinding.ActivitySelectPaymentBinding

class SelectPaymentActivity : BaseActivity() {
    private lateinit var binding: ActivitySelectPaymentBinding
    private val addressList = mutableListOf("Địa chỉ 1", "Địa chỉ 2", "Địa chỉ 3") // Dùng MutableList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            cancelBtn.setOnClickListener { finish() }
            nextBtn.setOnClickListener { processPayment() }
            imageViewArrow.setOnClickListener {
                binding.spinnerAddress.performClick()
            }
        }

        // Khởi tạo Spinner với danh sách địa chỉ
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, addressList)
        binding.spinnerAddress.adapter = adapter

        binding.btnAddAddress.setOnClickListener {
            if (binding.etInputNewAddress.visibility == View.GONE) {
                // Ẩn Spinner, hiện EditText
                binding.layoutSpinner.visibility = View.GONE
                binding.etInputNewAddress.visibility = View.VISIBLE
                binding.btnAddAddress.text = "Lưu Địa Chỉ"
            } else {
                val newAddress = binding.etInputNewAddress.text.toString().trim()
                if (newAddress.isNotEmpty()) {
                    addressList.add(newAddress)

                    // Cập nhật Adapter của Spinner
                    val adapter = binding.spinnerAddress.adapter as ArrayAdapter<String>
                    adapter.clear()
                    adapter.addAll(addressList)
                    adapter.notifyDataSetChanged()

                    // Hiển thị lại Spinner, ẩn EditText
                    binding.etInputNewAddress.text.clear()
                    binding.etInputNewAddress.visibility = View.GONE
                    binding.layoutSpinner.visibility = View.VISIBLE
                    binding.btnAddAddress.text = "Thêm Địa Chỉ Mới"
                } else {
                    Toast.makeText(this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_ADDRESS && resultCode == Activity.RESULT_OK) {
            val newAddress = data?.getStringExtra("new_address") ?: return
            addressList.add(newAddress)

            // Cập nhật danh sách trong Adapter
            val adapter = binding.spinnerAddress.adapter as ArrayAdapter<String>
            adapter.clear()
            adapter.addAll(addressList)
            adapter.notifyDataSetChanged()
        }
    }



    private fun processPayment() {
        val selectedId = binding.radioGroupPayment.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show()
            return
        }

        val paymentMethod = when (selectedId) {
            binding.rbZaloPay.id -> "ZaloPay"
            binding.rbVnPay.id -> "VnPay"
            binding.rbCOD.id -> "COD"
            else -> ""
        }

        val selectedAddress = binding.spinnerAddress.selectedItem.toString()
        Toast.makeText(this, "Thanh toán bằng $paymentMethod tại $selectedAddress", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, PaymentDetailActivity::class.java).apply {
            putExtra("paymentMethod", paymentMethod)
            putExtra("selectedAddress", selectedAddress)
        }
        startActivity(intent)
    }



    companion object {
        private const val REQUEST_CODE_ADD_ADDRESS = 1001
    }
}
