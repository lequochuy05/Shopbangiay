package com.example.shop.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shop.R
import com.example.shop.databinding.ActivityPaymentDetailBinding

class PaymentDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val paymentMethod = intent.getStringExtra("paymentMethod")

        when (paymentMethod) {
            "ZaloPay" -> {
                binding.paymentTitle.text = "Thanh toán qua ZaloPay"
                binding.paymentDesc.text = "Bạn sẽ được chuyển đến ứng dụng ZaloPay để hoàn tất thanh toán."

            }
            "VnPay" -> {
                binding.paymentTitle.text = "Thanh toán qua VnPay"
                binding.paymentDesc.text = "Bạn sẽ được chuyển đến cổng thanh toán VnPay để tiếp tục."
            }
            "COD" -> {
                binding.paymentTitle.text = "Thanh toán khi nhận hàng"
                binding.paymentDesc.text = "Bạn sẽ thanh toán khi nhận hàng từ đơn vị vận chuyển."
            }
            else -> {
                Toast.makeText(this, "Không xác định phương thức thanh toán", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Nút xác nhận thanh toán
        binding.confirmPaymentBtn.setOnClickListener {
            Toast.makeText(this, "Thanh toán thành công với $paymentMethod", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
