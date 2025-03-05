package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shop.databinding.ActivitySelectPaymentBinding

class SelectPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply{
            cancelBtn.setOnClickListener {
                finish()
            }
            nextBtn.setOnClickListener {
                val selectedId = binding.radioGroupPayment.checkedRadioButtonId
                if (selectedId == -1) {
                    Toast.makeText(this@SelectPaymentActivity, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show()
                } else {
                    val paymentMethod = when (selectedId) {
                        binding.rbZaloPay.id -> "ZaloPay"
                        binding.rbVnPay.id -> "VnPay"
                        binding.rbCOD.id -> "COD"
                        else -> ""
                    }

                    val intent = Intent(this@SelectPaymentActivity, PaymentDetailActivity::class.java)
                    intent.putExtra("paymentMethod", paymentMethod)
                    startActivity(intent)
                }
            }
        }
    }
}
