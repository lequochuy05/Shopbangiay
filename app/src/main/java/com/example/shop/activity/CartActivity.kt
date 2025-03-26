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

        managementCart = ManagementCart(this)

        binding.paymentBtn.setOnClickListener{
            startActivity(Intent(this, SelectPaymentActivity::class.java))
        }


        setVariables()
        initCartList()
        calculateCart()
    }

    private fun initCartList() {
        val listCart = managementCart.getListCart()
        if (listCart.isEmpty()) {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.cartView.visibility = View.GONE
            binding.salesLayout.visibility = View.GONE
            binding.priceLayout.visibility = View.GONE
            binding.paymentBtn.visibility = View.GONE
        } else {
            binding.tvEmptyCart.visibility = View.GONE
            binding.scrollView3.visibility = View.VISIBLE

            binding.cartView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.cartView.adapter = CartAdapter(listCart, this, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    calculateCart()
                    updateCartVisibility() // Cập nhật hiển thị giỏ hàng
                }
            })
        }
    }

    private fun updateCartVisibility() {
        val listCart = managementCart.getListCart()
        if (listCart.isEmpty()) {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.cartView.visibility = View.GONE
            binding.salesLayout.visibility = View.GONE
            binding.priceLayout.visibility = View.GONE
            binding.paymentBtn.visibility = View.GONE
        } else {
            binding.tvEmptyCart.visibility = View.GONE
            binding.scrollView3.visibility = View.VISIBLE
        }
    }

    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 10
        tax = (managementCart.getTotalFee() * percentTax).roundToInt().toDouble()
        val total = (managementCart.getTotalFee() + tax + delivery).roundToInt().toInt()
        val itemTotal = managementCart.getTotalFee().roundToInt().toInt()

        with(binding) {
            totalFeeTxt.text = "$$itemTotal"
            taxTxt.text = "$$tax"
            deliveryTxt.text = "$$delivery"
            totalTxt.text = "$$total"
        }
    }

    private fun setVariables() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}
