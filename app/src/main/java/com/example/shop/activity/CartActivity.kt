package com.example.shop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.CartAdapter
import com.example.shop.helper.ChangeNumberItemsListener
import com.example.shop.databinding.ActivityCartBinding
import com.example.shop.helper.ManagementCart
import kotlin.math.roundToInt

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managementCart: ManagementCart
    private var tax: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        val uId = sharedPreferences.getString("uId", "Unknown").toString()
        managementCart = ManagementCart(this, uId)  // Truyền userId vào

        setVariables()
        initCartList()
        calculateCart()

        binding.paymentBtn.setOnClickListener {
            if (!isUserLoggedIn()) {
                showLoginDialog(this)
            } else {
                val totalAmount = calculateCart() // Lấy tổng tiền từ hàm
                val intent = Intent(this, SelectPaymentActivity::class.java)
                intent.putExtra("totalAmount", totalAmount) // Truyền giá trị tổng tiền
                startActivity(intent)
            }
        }

    }

    private fun initCartList() {
        val listCart = managementCart.getListCart()
        if (listCart.isNotEmpty()) {
            binding.tvEmptyCart.visibility = View.GONE
            binding.scrollView3.visibility = View.VISIBLE

            binding.cartView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.cartView.adapter = CartAdapter(listCart, this, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    calculateCart()
                    updateCartVisibility() // Cập nhật hiển thị giỏ hàng
                }
            })
        } else {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.cartView.visibility = View.GONE
            binding.salesLayout.visibility = View.GONE
            binding.priceLayout.visibility = View.GONE
            binding.paymentBtn.visibility = View.GONE

        }
    }

    private fun updateCartVisibility() {
        val listCart = managementCart.getListCart()
        if (listCart.isNotEmpty()) {
            binding.tvEmptyCart.visibility = View.GONE
            binding.scrollView3.visibility = View.VISIBLE
        } else {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.cartView.visibility = View.GONE
            binding.salesLayout.visibility = View.GONE
            binding.priceLayout.visibility = View.GONE
            binding.paymentBtn.visibility = View.GONE
        }
    }

    private fun calculateCart():Int {
        val percentTax = 0.02
        val delivery = 10000
        tax = (managementCart.getTotalFee() * percentTax).roundToInt().toDouble()
        val total = (managementCart.getTotalFee() + tax + delivery).roundToInt().toInt()
        val itemTotal = managementCart.getTotalFee().roundToInt().toInt()

        with(binding) {
            totalFeeTxt.text = "$itemTotal VND"
            taxTxt.text = "$tax VND"
            deliveryTxt.text = "$delivery VND"
            totalTxt.text = "$total VND"
        }
        return total
    }

    private fun setVariables() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}
